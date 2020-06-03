package com.idgindigo.pms.service.pms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.plan.Plan;
import com.idgindigo.pms.domain.extranet.roomtype.BaseRoom;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.VirtualRoom;
import com.idgindigo.pms.domain.extranet.service.Living;
import com.idgindigo.pms.domain.pms.*;
import com.idgindigo.pms.price.LivingPriceResolver;
import com.idgindigo.pms.repository.extranet.BaseRoomRepository;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.extranet.VirtualRoomRepository;
import com.idgindigo.pms.repository.extranet.plan.PlanRepository;
import com.idgindigo.pms.repository.extranet.roomuse.BaseGroupRoomUseRepository;
import com.idgindigo.pms.repository.pms.*;
import com.idgindigo.pms.restutils.exception.RoomNotAvailableException;
import com.idgindigo.pms.restutils.exception.RoomUseException;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.service.admin.SettingsService;
import com.idgindigo.pms.service.approve.BillApproveService;
import com.idgindigo.pms.service.extranet.LivingService;
import com.idgindigo.pms.service.validation.ApprovedValidationService;
import com.idgindigo.pms.service.validation.GroupRoomUseValidationService;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import com.idgindigo.pms.web.controller.pms.RoomUseController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.idgindigo.pms.domain.extranet.BaseGroupRoomUse.Status.*;

/**
 * @author valentyn_vakatsiienko
 * @since 11/20/13 10:43 AM
 */
@Service
public class RoomUseService {
    public static final HashSet<RoomUse.Status> REFUSABLE_STATUSES = new HashSet<>(Arrays.asList(BOOKING_FREE, BOOKING_WARRANTY, LIVING));
    @Inject
    private RoomUseRepository roomUseRepository;
    @Inject
    private BaseGroupRoomUseRepository baseGroupRoomUseRepository;
    @Inject
    private CustomerGroupService customerGroupService;
    @Inject
    private QuotaService quotaService;
    @Inject
    private BillService billService;
    @Inject
    private LivingPriceResolver priceResolver;
    @Inject
    private RoomRepository roomRepository;
    @Inject
    private TouristTaxService touristTaxService;
    @Inject
    private PeriodRoomTypeInfoService periodRoomTypeInfoService;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private BaseRoomRepository baseRoomRepository;
    @Inject
    private LivingService livingService;
    @Inject
    private CustomerGroupRepository customerGroupRepository;
    @Inject
    private SettingsService settingsService;
    @Inject
    private RefundService refundService;
    @Inject
    private PlanRepository planRepository;
    @Inject
    private PenaltyService penaltyService;
    @Inject
    private LivingUseRepository livingUseRepository;
    @Inject
    private BillApproveService billApproveService;
    @Inject
    private BaseServiceUseService baseServiceUseService;
    @Inject
    private GroupMemberToRoomUseRepository groupMemberToRoomUseRepository;
    @Inject
    private VirtualRoomRepository virtualRoomRepository;
    @Inject
    private GroupRoomUseValidationService groupRoomUseValidationService;
    @Inject
    private ApprovedValidationService approvedValidationService;

    public RoomUse create(@Valid RoomUse entity, boolean updateChannels) {
        return create(entity, Collections.<LocalDate, Long>emptyMap(), updateChannels);
    }

    public RoomUse create(@Valid RoomUse entity, Map<LocalDate, Long> prices, boolean updateChannels) {
        return create(entity, prices, null, updateChannels);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    private RoomUse create(@Valid RoomUse entity, Map<LocalDate, Long> prices, Bill bill, boolean updateChannels) {
        validateRoomUseCreate(entity);
        entity.setRoom(roomRepository.findOne(entity.getRoom().getId()));

        //Room management
        entity = roomUseRepository.save(entity);
        RoomType roomType = roomRepository.findOne(entity.getRoom().getId()).getRoomType();
        quotaService.updateQuota(entity.getStartDate(), entity.getEndDate(), roomType, -1, updateChannels);

        //ServiceUses management
        if (bill == null) {
            bill = billService.getBill(entity, entity.isCustomerPays());
        }
        generateServiceUses(entity.getStartDate(), entity.getEndDate(), entity, bill, prices);
        billService.updateTotal(bill, null);

        return entity;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public List<RoomUse> create(@Valid GroupRoomUseDto dto, boolean updateChannels) {
        CustomerGroup group = customerGroupService.create(dto.group);
        Bill groupBill = null;
        List<RoomUse> result = new ArrayList<>(dto.getRoomUses().size());
        for (RoomUseWithOverrides roomUseWithOverrides : dto.getRoomUses()) {
            RoomUse roomUse = roomUseWithOverrides.roomUse;

            roomUse.setCustomerGroup(group);

            boolean customerPays = roomUse.isCustomerPays();
            if (customerPays && groupBill == null) {
                groupBill = billService.getBill(group);
            }
            result.add(create(roomUse, roomUseWithOverrides.prices, customerPays ? groupBill : null, updateChannels));

            roomUseRepository.flush();
        }
        return result;
    }

    private void validateRoomUseCreate(RoomUse roomUse) {
        groupRoomUseValidationService.validateRoomUseCreate(roomUse);
        approvedValidationService.validateApproved(roomUse.getRoom(), roomRepository, getExceptionGenerator(RoomUseException.ROOM_NOT_APPROVED, Room.ROOM));
        validateBaseRoom(roomUse.getRoom().getId(), roomUse.getBaseRoom().getId());
        confirmAvailability(roomUse, roomUse.getStartDate(), roomUse.getEndDate());
    }

    public void confirm(RoomUse roomUse) {
        if (roomUse.getStatus() != BOOKING_FREE) {
            throw new RoomUseException(RoomUseException.CONFIRM_ONLY_FOR_BOOKING_FREE, "status");
        }
        roomUseRepository.setStatus(BOOKING_WARRANTY, roomUse.getId(), SecurityUtils.getCurrentUser(), LocalDateTime.now());
        handleConfirm(roomUse);
    }

    private void handleConfirm(RoomUse roomUse) {
        Iterable<Bill> billsToApprove = livingUseRepository.findBillsByRoomUse(roomUse);//TODO test
        for (Bill bill : billsToApprove) {
            billApproveService.approve(bill.getId());
        }
        RoomUse movedTo = roomUseRepository.getMovedTo(roomUse);
        if (movedTo != null && movedTo.getStatus() == BOOKING_FREE) {
            confirm(movedTo);
        }
    }

    public void checkIn(RoomUse roomUse, LocalDateTime time, boolean tryHandleEarlyCheckIn) {
        validateCheckIn(roomUse);
        roomUseRepository.setStatus(LIVING, roomUse.getId(), SecurityUtils.getCurrentUser(), LocalDateTime.now());
        roomUseRepository.setCheckInTime(time, roomUse.getId());
        if (tryHandleEarlyCheckIn) {
            penaltyService.handleEarlyCheckIn(roomUse, time);
        }
    }

    private void validateCheckIn(RoomUse roomUse) {
        if (baseGroupRoomUseRepository.getStatus(roomUse.getId()) != BOOKING_WARRANTY) {
            throw new RoomUseException(RoomUseException.INCOME_ONLY_AFTER_BOOKING, "status");
        }
        if (settingsService.getHotelDate().isBefore(roomUse.getStartDate())) {
            throw new RoomUseException(RoomUseException.INCOME_TOO_EARLY, "startDate");
        }
        if (!groupMemberToRoomUseRepository.existsByRoomUse(roomUse)) {
            throw new RoomUseException(RoomUseException.CHECK_IN_EMPTY_ROOM_USE, "groupMembers");
        }
    }

    public void checkOut(RoomUse roomUse, LocalDateTime time) {
        validateCheckOut(roomUse, time.toLocalDate());
        roomUseRepository.setCheckOutTime(time, roomUse.getId());
        roomUseRepository.setStatus(OUTGO, roomUse.getId(), SecurityUtils.getCurrentUser(), LocalDateTime.now());
        penaltyService.handleLateCheckOut(roomUse, time);
    }

    private void validateCheckOut(RoomUse roomUse, LocalDate date) {
        if (roomUse.getStatus() != LIVING) {
            throw new RoomUseException(RoomUseException.OUTGO_ONLY_AFTER_LIVING, "status");
        }
        if (!roomUse.getEndDate().isEqual(date)) {
            throw new RoomUseException(RoomUseException.OUTGO_ONLY_ON_END_DATE, "endDate");
        }
        if (!roomUseRepository.isFullyPaid(roomUse)) {
            throw new RoomUseException(RoomUseException.OUTGO_FOR_FULLY_PAID, "totalPaid");
        }
    }

    public void setNotArrived(RoomUse roomUse) {
        roomUseRepository.setStatus(NOT_ARRIVED, roomUse.getId(), SecurityUtils.getCurrentUser(), LocalDateTime.now());
        quotaService.updateQuota(roomUse.getStartDate(), roomUse.getEndDate(), roomUse.getRoom().getRoomType(), 1, true);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Collection<RoomUse> refuse(CustomerGroup group, LocalDate date, boolean updateChannels, BankDetails details) {
        Collection<RoomUse> notified = new ArrayList<>();
        for (RoomUse roomUse : roomUseRepository.findByCustomerGroupIdAndStatusIn(group.getId(), REFUSABLE_STATUSES)) {
            if (canBeRefusedByGroup(roomUse, date)) {
                notified.add(roomUse);
                refuse(roomUse, !date.isBefore(roomUse.getStartDate()) ? date : roomUse.getStartDate(), updateChannels, details);
            }
        }
        return notified;
    }

    private boolean canBeRefusedByGroup(RoomUse roomUse, LocalDate date) {
        return roomUse.getEndDate().isAfter(date) && !getRepository().isMoved(roomUse);
    }

    public List<RoomUse> refuse(RoomUse roomUse, LocalDate date, boolean updateChannels, BankDetails details) {
        List<RoomUse> refused = new ArrayList<>();
        //Manage roomUse
        RoomUse.Status status = roomUse.getStatus();
//        RoomUse refuse;
        switch (status) {
            case BOOKING_FREE:
            case BOOKING_WARRANTY:
            case LIVING:
                performRefuse(roomUse, date, updateChannels, details);
                break;
            default:
                throw new RoomUseException(RoomUseException.REFUSE_INVALID_STATUS, "status");
        }
        refused.add(roomUse);

//        roomUseRepository.flush();

        //Recursively manage moved parts of room use
        RoomUse movedTo = roomUseRepository.getMovedTo(roomUse);
        if (movedTo != null && REFUSABLE_STATUSES.contains(movedTo.getStatus())) {
            refused.addAll(refuse(movedTo, movedTo.getStartDate(), updateChannels, details));
        }
        return refused;
    }

    private RoomUse performRefuse(RoomUse roomUse, LocalDate refuseDate, boolean updateChannels, BankDetails details) {
        RoomUse refuse;

        //manage bills and create refund
        refundService.refund(baseServiceUseRepository.findByRoomUseAndAfterDateIncluding(roomUse, refuseDate), details);

        if (refuseDate.isAfter(roomUse.getStartDate()) && refuseDate.isBefore(roomUse.getEndDate())) {
            //Split
            refuse = roomUseRepository.saveAndFlush(getRefuse(roomUse, refuseDate));
            baseServiceUseService.move(roomUse, refuse, refuseDate, true, details);
            roomUseRepository.save(shorten(roomUse, refuse.getStartDate(), updateChannels));
        } else if (refuseDate.isEqual(roomUse.getStartDate())) {
            roomUseRepository.setStatus(REFUSE, roomUse.getId(), SecurityUtils.getCurrentUser(), LocalDateTime.now());
            refuse = roomUse;
            quotaService.updateQuota(refuse.getStartDate(), refuse.getEndDate(), refuse.getRoom().getRoomType(), 1, updateChannels);
        } else {
            throw new RoomUseException(RoomUseException.REFUSE_INVALID_DATE, "dateStart/End");
        }

        return refuse;
    }

    public void move(RoomUse roomUse, RoomUseController.MoveDto moveDto, boolean updateChannels, BankDetails details) {
        RoomUse move;
        RoomUse.Status status;

        boolean upgrade = moveDto.isUpgrade();
        move = getMove(roomUse, moveDto.getSinceDate(), moveDto.getRoom(), moveDto.getBaseRoom(), upgrade ? roomUse.getPlan() : moveDto.getPlan());
        move.setCustomerPays(moveDto.isCustomerPays());
        validateMove(move, roomUse, upgrade);
        status = move.getStatus();
        move.setStatus(BOOKING_FREE);

        move = upgrade || roomUse.isFromChannel() ?
                create(move, getPriceOverridesForUpgrade(roomUse, moveDto), updateChannels) :
                create(move, updateChannels);

        performRefuse(roomUse, moveDto.getSinceDate(), updateChannels, details);

        handleGroupMembers(roomUse, move);
        if (status != BOOKING_FREE) {
            confirm(move);
        }
        if (status == LIVING) {
            checkIn(move, move.getStartDate().toDateTimeAtStartOfDay(settingsService.getTimeZone()).toLocalDateTime(), false);
        }
    }

    private void handleGroupMembers(RoomUse roomUse, RoomUse move) {
        List<GroupMemberToRoomUse> oldRoomMembers = groupMemberToRoomUseRepository.findByRoomUse(roomUse);
        List<GroupMemberToRoomUse> toSave = new ArrayList<>(oldRoomMembers.size());
        for (GroupMemberToRoomUse member : oldRoomMembers) {
            GroupMemberToRoomUse roomMember = new GroupMemberToRoomUse(member.getGroupMember(), move);
            toSave.add(roomMember);
        }
        groupMemberToRoomUseRepository.save(toSave);
    }

    private Map<LocalDate, Long> getPriceOverridesForUpgrade(RoomUse roomUse, RoomUseController.MoveDto moveDto) {
        Map<LocalDate, Long> overrides = new HashMap<>();
        for (LivingUse livingUse : livingUseRepository.findByRoomUseAndAfterDateIncluding(roomUse, moveDto.getSinceDate())) {
            overrides.put(livingUse.getDate(), livingUse.getRawTotal());
        }
        return overrides;
    }

    private void validateBaseRoom(Long roomId, Long baseRoomId) {
        RoomType roomType = roomRepository.findOne(roomId).getRoomType();
        BaseRoom baseRoom = baseRoomRepository.findOne(baseRoomId);
        VirtualRoom virtualRoom = virtualRoomRepository.findOne(baseRoomId);
        if (!roomType.equals(baseRoom) && virtualRoom != null && !roomType.equals(virtualRoom.roomType())) {
            throw new RoomUseException(RoomUseException.INVALID_VIRTUAL_ROOM, BaseRoom.BASE_ROOM);
        }
    }

    private void validateMove(RoomUse move, RoomUse roomUse, boolean upgrade) {
        if (/*move.getStartDate().isBefore(settingsService.getHotelDate())
                ||*/ move.getStartDate().isAfter(roomUse.getEndDate().minusDays(1))
                || move.getStartDate().isBefore(roomUse.getStartDate())) {
            throw new RoomUseException(RoomUseException.INVALID_MOVE_DATE, "sinceDate");
        }
        approvedValidationService.validateApproved(move.getRoom(), roomRepository, getExceptionGenerator(RoomUseException.ROOM_NOT_APPROVED, Room.ROOM));
        approvedValidationService.validateApproved(move.getBaseRoom(), baseRoomRepository, getExceptionGenerator(RoomUseException.BASE_ROOM_NOT_APPROVED, BaseRoom.BASE_ROOM));
        if (!upgrade) {
            approvedValidationService.validateApproved(move.getPlan(), planRepository, getExceptionGenerator(RoomUseException.PLAN_NOT_APPROVED, Plan.PLAN));
        }
        if (!roomUse.getRoom().getRoomType().equals(move.getRoom().getRoomType())) {
            confirmAvailability(move, move.getStartDate(), roomUse.getEndDate());
        }
        if (!upgrade && move.getPlan() == null) {
            throw new RoomUseException(RoomUseException.MOVE_PLAN_NOT_SET, Plan.PLAN);
        }
        validateRoomUseCreate(move);
    }

    private ApprovedValidationService.ExceptionGenerator<RoomUseException> getExceptionGenerator(final String code, final String message) {
        return new ApprovedValidationService.ExceptionGenerator<RoomUseException>() {
            @Override
            public RoomUseException generate() {
                return new RoomUseException(code, message);
            }
        };
    }

    /*private <T extends ApprovableEntity> void validateApproved(T dependency, ApprovableRepository<T> repository, String message, String source) {
        if (dependency == null || dependency.getId() == null || !repository.isApproved(dependency.getId())) {
            throw new RoomUseException(message, source);
        }
    }*/

    /*public RoomUse changeEndDate(RoomUse roomUse, LocalDate oldEndDate, LocalDate newEndDate) {
        if (newEndDate.isBefore(roomUse.getStartDate().plusDays(1))) {
            throw new IllegalArgumentException("new end date must after startDate + 1");
        }
        if (oldEndDate.isBefore(newEndDate)) {
            return widen(roomUse, oldEndDate, newEndDate);
        } else if (oldEndDate.isAfter(newEndDate)) {
            return shorten(roomUse, oldEndDate, newEndDate);
        } else {
            return roomUse;
        }
    }*/

    public RoomUse shorten(RoomUse roomUse, LocalDate newEndDate, boolean updateChannels) {
        return shorten(roomUse, roomUse.getEndDate(), newEndDate, updateChannels);
    }

    public RoomUse shorten(RoomUse roomUse, LocalDate oldEndDate, LocalDate newEndDate, boolean updateChannels) {
        if (newEndDate.isAfter(oldEndDate) || newEndDate.isBefore(roomUse.getStartDate().plusDays(1))) {
            throw new IllegalArgumentException("new end date must be in range startDate + 1 : endDate");
        }
        roomUse.setEndDate(newEndDate);
        quotaService.updateQuota(newEndDate, oldEndDate, roomUse.getRoom().getRoomType(), 1, updateChannels);
        return roomUseRepository.save(roomUse);
    }

    /*public RoomUse widen(RoomUse roomUse, LocalDate newEndDate) {
        return widen(roomUse, roomUse.getEndDate(), newEndDate);
    }*/

    public RoomUse widen(RoomUse roomUse, LocalDate oldEndDate, LocalDate newEndDate) {
        if (newEndDate.isBefore(oldEndDate)) {
            throw new IllegalArgumentException("new end date must be after old end date");
        }
        confirmAvailability(roomUse, oldEndDate, newEndDate);
        roomUse.setEndDate(newEndDate);

        Bill bill = billService.getBill(roomUse);
        generateServiceUses(oldEndDate, newEndDate, roomUse, bill, Collections.<LocalDate, Long>emptyMap());
        billService.updateTotal(bill, null);

        quotaService.updateQuota(oldEndDate, newEndDate, roomUse.getRoom().getRoomType(), -1, true);
        return roomUseRepository.save(roomUse);
    }

    public RoomUse getRefuse(RoomUse roomUse, LocalDate date) {
        RoomUse refuse = new RoomUse();
        refuse.setStatus(REFUSE);
        refuse.setStartDate(date);
        refuse.setEndDate(roomUse.getEndDate());
        refuse.setCustomerGroup(roomUse.getCustomerGroup());
        refuse.setBaseRoom(roomUse.getBaseRoom());
        refuse.setPlan(roomUse.getPlan());
        refuse.setRoom(roomUse.getRoom());
        refuse.setRcode(roomUse.getRcode());
        refuse.setAcode(roomUse.getAcode());
        refuse.setCustomerPays(roomUse.isCustomerPays());
        return refuse;
    }

    public RoomUse getMove(RoomUse roomUse, LocalDate sinceDate, Room room, BaseRoom baseRoom, Plan plan) {
        RoomUse move = new RoomUse();
        move.setStatus(getStatus(roomUse, sinceDate));
        move.setStartDate(sinceDate);
        move.setEndDate(roomUse.getEndDate());
        move.setCustomerGroup(roomUse.getCustomerGroup());
        move.setBaseRoom(baseRoom);
        move.setPlan(plan);
        move.setRoom(room);
        if (sinceDate.isAfter(roomUse.getStartDate())) {
            move.setMovedFrom(roomUse);
        }
        move.setSource(roomUse.getSource());
        move.setRcode(roomUse.getRcode());
        move.setAcode(roomUse.getAcode());
        move.setCustomerPays(roomUse.isCustomerPays());
        return move;
    }

    private RoomUse.Status getStatus(RoomUse roomUse, LocalDate sinceDate) {
        RoomUse.Status status = roomUse.getStatus();
        switch (status) {
            case LIVING:
                if (sinceDate.isEqual(settingsService.getHotelDate())) {
                    return status;
                } else {
                    return BOOKING_WARRANTY;
                }
            case BOOKING_WARRANTY:
                return status;
            default:
                return BOOKING_FREE;
        }
    }

    /**
     * Generates livingUses for dates: startDate - endDate, excluding last day
     *
     * @param startDate
     * @param endDate
     * @param roomUse
     * @param bill
     * @param prices    Prices overrides
     * @return
     */
    public List<BaseServiceUse> generateServiceUses(LocalDate startDate, LocalDate endDate, RoomUse roomUse, Bill bill, @NotNull Map<LocalDate, Long> prices) {
        List<BaseServiceUse> serviceUses = new LinkedList<>();
        CustomerGroup customerGroup = customerGroupRepository.findOne(roomUse.getCustomerGroup().getId());
        for (int i = 0; i < Days.daysBetween(startDate, endDate).getDays(); i++) {
            LocalDate date = startDate.plusDays(i);

            LivingUse livingUse = getLivingUse(roomUse, bill, prices, date, livingService.get(roomUse.getBaseRoom(), roomUse.getPlan(), date));
            setLivingAmount(livingUse, date, roomUse.getRoom().getRoomType());
            setTotal(livingUse, customerGroup.getDiscount(), bill.isForCustomer());

            serviceUses.add(livingUse);
            //Create additional bed uses
//            List<GroupMember> members = groupMemberRepository.findByCustomerGroup(customerGroup);
//            VirtualRoom virtualRoom = baseRoomRepository.findOne(roomUse.getVirtualRoom().getId());
//            int maxAdults = virtualRoom.getAdults();
//            int maxChildren = virtualRoom.getChildren();
//
//            int adults = getPersonQuantity(members, Adult.ADULT);
//            int children = getPersonQuantity(members, Child.CHILD);
//            if (adults < maxAdults) {
//                adults = 0;
//            } else {
//                adults = adults - maxAdults;
//            }
//            if (children < maxChildren) {
//                children = 0;
//            } else {
//                children = children - maxChildren;
//            }
//            if (adults > 0) {
//                SimpleServiceUse adultBed = new SimpleServiceUse();
//                adultBed.setQuantity(adults);
//                adultBed.setBill(bill);
//                adultBed.setDate(date);
//                adultBed.setService(systemServiceService.getAdultBedService());
//                adultBed.setTotal(adultBed.getQuantity() * systemServiceService.getAdultBedPrice(living, date));
//                adultBed.setRawTotal(adultBed.getTotal());
//                serviceUses.add(adultBed);
//            }
//            if (children > 0) {
//                SimpleServiceUse childBed = new SimpleServiceUse();
//                childBed.setQuantity(children);
//                childBed.setBill(bill);
//                childBed.setDate(date);
//                childBed.setService(systemServiceService.getChildBedService());
//                childBed.setTotal(childBed.getQuantity() * systemServiceService.getChildBedPrice(living, date));
//                childBed.setRawTotal(childBed.getTotal());
//                serviceUses.add(childBed);
//            }
        }
        serviceUses = baseServiceUseRepository.save(serviceUses);

        return serviceUses;
    }

    private void setTotal(LivingUse livingUse, int groupDiscount, boolean applyDiscount) {
        long discount = 0;
        if (applyDiscount) {
            discount = livingUse.getRawTotal() * groupDiscount / 100;
        }
        livingUse.setTotal(livingUse.getRawTotal() - discount + touristTaxService.getTaxAmount(livingUse));
    }

    private void setLivingAmount(LivingUse livingUse, LocalDate date, RoomType roomType) {
        long livingAmount = periodRoomTypeInfoService.getLivingAmount(roomType, date);
        livingUse.setLivingAmount(livingAmount);
    }

    private LivingUse getLivingUse(RoomUse roomUse, Bill bill, Map<LocalDate, Long> prices, LocalDate date, Living living) {
        LivingUse livingUse = new LivingUse();
        livingUse.setRoomUse(roomUse);
        livingUse.setQuantity(1);
        livingUse.setBill(bill);
        livingUse.setDate(date);
        livingUse.setService(living);
        livingUse.setTourismTax(settingsService.getTourismTax());

        //Calculate total with discount and tourism tax
        Long price = prices.get(date);
        Long rawTotal;
        if (price != null) {
            rawTotal = price;
        } else {
            rawTotal = priceResolver.getPrice(living, date);
        }
        livingUse.setRawTotal(rawTotal);
        return livingUse;
    }

    /*
    private int getPersonQuantity(Iterable<GroupMember> members, String personType) {
        int i = 0;
        for (GroupMember member : members) {
            if (member.getPerson().getType().equals(personType)) {
                i++;
            }
        }
        return i;
    }
    */

    public void updateTotal(RoomUse roomUse) {
        roomUseRepository.updateTotal(roomUse);
        customerGroupService.updateTotal(roomUse.getCustomerGroup());
    }

    public void updateTotalPaid(RoomUse roomUse) {
        roomUseRepository.updateTotalPaid(roomUse);
        customerGroupService.updateTotalPaid(roomUse.getCustomerGroup());
    }

    public RoomUseRepository getRepository() {
        return roomUseRepository;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GroupRoomUseDto {
        @JsonIgnore
        private String rcode;
        @NotNull
        private CustomerGroup group;
        @NotEmpty
        private List<RoomUseWithOverrides> roomUses;

        @Override
        public String toString() {
            return new SmartToStringBuilder(this, SmartToStringBuilder.SHORT_PREFIX_STYLE)
                    .append("rcode", rcode)
                    .append("group", group)
                    .append("roomUses", roomUses, true)
                    .toString();
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomUseWithOverrides {
        private RoomUse roomUse;
        private Map<LocalDate, Long> prices = new HashMap<>();

        public RoomUseWithOverrides(RoomUse roomUse) {
            this.roomUse = roomUse;
        }

        @Override
        public String toString() {
            return new SmartToStringBuilder(this, SmartToStringBuilder.NO_FIELD_NAMES_STYLE)
                    .append("roomUse", roomUse)
                    .append("prices", prices)
                    .toString();
        }
    }

    public void confirmAvailability(PmsRoomUse roomUse, LocalDate start, LocalDate end) {
        if (roomUseRepository.isBooked(roomUse.getRoom(), start, end)) {
            throw new RoomNotAvailableException(roomUse);
        }
    }
}