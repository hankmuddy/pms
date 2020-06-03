package com.idgindigo.pms.web.controller.pms.reports;

import com.idgindigo.pms.domain.extranet.BaseGroupRoomUse;
import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.extranet.roomtype.RoomTypeValue;
import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.extranet.RoomTypeRepository;
import com.idgindigo.pms.repository.extranet.rate.RoomTypeValueRepository;
import com.idgindigo.pms.service.channels.ChannelService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by misha on 24.04.14.
 */
@Controller
public class InfoStatController extends ReportController {
    @Inject
    protected RoomTypeValueRepository roomTypeValueRepository;
    @Inject
    protected RoomTypeRepository roomTypeRepository;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FreeRoomItem {
        private String roomType;
        private String date;
        private String rooms;
    }

    @RequestMapping("freeRooms")
    public void freeRooms(@RequestParam(value = "periodStart", required = false) final Long periodStart, @RequestParam(value = "periodEnd", required = false) final Long periodEnd, HttpServletResponse response) throws JRException, IOException {
        final LocalDate startDate = periodStart == null ? settingsService.getHotelDate() : new LocalDate(periodStart * 1000, DateTimeZone.UTC);
        final LocalDate endDate = periodEnd == null ? startDate.plusDays(1) : new LocalDate(periodEnd * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("freeRooms", "jasper"), getReport("freeRooms", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "startDate", startDate.toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "endDate", endDate.toString(ChannelService.FORMATTER));

                List<RoomTypeValue> rooms = getRooomValues(startDate, endDate);

                List<FreeRoomItem> dataCollection = new ArrayList<>();
                for (RoomTypeValue room : rooms) {
                    dataCollection.add(new FreeRoomItem(
                            room.getRoomType().getName(),
                            room.getDate().toString(ChannelService.FORMATTER),
                            String.valueOf(room.getRoomsAvailable())
                    ));
                }

                parameters.put("dataCollection", dataCollection);
            }
        }.createReport();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomUseItem {
        private String roomType;
        private Integer rooms;
        private Integer customers;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomUseMapItem {
        private Integer rooms;
        private Integer customers;
    }

    @RequestMapping("income")
    public void income(@RequestParam(value = "periodStart", required = false) final Long periodStart, @RequestParam(value = "periodEnd", required = false) final Long periodEnd, HttpServletResponse response) throws JRException, IOException {
        final LocalDate startDate = periodStart == null ? settingsService.getHotelDate() : new LocalDate(periodStart * 1000, DateTimeZone.UTC);
        final LocalDate endDate = periodEnd == null ? startDate.plusDays(1) : new LocalDate(periodEnd * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("income", "jasper"), getReport("income", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "startDate", startDate.toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "endDate", endDate.toString(ChannelService.FORMATTER));

                List<RoomUse> incomeRoomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                    @Override
                    public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        RoomUse.Status[] statuses = {BaseGroupRoomUse.Status.BOOKING_FREE, BaseGroupRoomUse.Status.BOOKING_WARRANTY};
                        Predicate status = root.<RoomUse.Status>get("status").in(statuses);
                        Predicate sDate = cb.lessThanOrEqualTo(root.<LocalDate>get("startDate"), endDate);
                        Predicate eDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), startDate);

                        return cb.and(sDate, eDate, status);
                    }
                });

                List<RoomUse> livingRoomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                    @Override
                    public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Predicate status = cb.equal(root.<RoomUse.Status>get("status"), BaseGroupRoomUse.Status.LIVING);
                        Predicate sDate = cb.lessThanOrEqualTo(root.<LocalDate>get("startDate"), endDate);
                        Predicate eDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), startDate);

                        return cb.and(sDate, eDate, status);
                    }
                });

                ConcurrentHashMap<String, RoomUseMapItem> incomeMap = new ConcurrentHashMap<>();
                for (RoomUse item : incomeRoomUses) {
                    incomeMap.putIfAbsent(item.getBaseRoom().getName(), new RoomUseMapItem(0, 0));
                    RoomUseMapItem mapItem = incomeMap.get(item.getBaseRoom().getName());
                    mapItem.rooms += 1;
                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(item);
                    mapItem.customers += groupMembers == null ? 1 : groupMembers.size();
                }

                ConcurrentHashMap<String, RoomUseMapItem> livingMap = new ConcurrentHashMap<>();
                for (RoomUse item : livingRoomUses) {
                    livingMap.putIfAbsent(item.getBaseRoom().getName(), new RoomUseMapItem(0, 0));
                    RoomUseMapItem mapItem = livingMap.get(item.getBaseRoom().getName());
                    mapItem.rooms += 1;
                    List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(item);
                    mapItem.customers += groupMembers == null ? 1 : groupMembers.size();
                }

                Collection<RoomUseItem> incomeCollection = new ArrayList<>();
                for (Map.Entry<String, RoomUseMapItem> entry : incomeMap.entrySet()) {
                    RoomUseMapItem item = entry.getValue();
                    incomeCollection.add(new RoomUseItem(entry.getKey(), item.getRooms(), item.getCustomers()));
                }

                Collection<RoomUseItem> livingCollection = new ArrayList<>();
                for (Map.Entry<String, RoomUseMapItem> entry : livingMap.entrySet()) {
                    RoomUseMapItem item = entry.getValue();
                    livingCollection.add(new RoomUseItem(entry.getKey(), item.getRooms(), item.getCustomers()));
                }

                parameters.put("incomeCollection", incomeCollection);
                parameters.put("livingCollection", livingCollection);
            }
        }.createReport();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DateStatItem {
        private String date;
        private String roomsToSell;
        private String booked;
        private String living;
        private String livingPersons;
        private String profit;
        private String sot;
        private String load;
    }

    @RequestMapping("dateStat")
    public void dateStat(@RequestParam(value = "periodStart", required = false) final Long periodStart, @RequestParam(value = "periodEnd", required = false) final Long periodEnd, HttpServletResponse response) throws JRException, IOException {
        final LocalDate startDate = periodStart == null ? settingsService.getHotelDate() : new LocalDate(periodStart * 1000, DateTimeZone.UTC);
        final LocalDate endDate = periodEnd == null ? startDate.plusDays(1) : new LocalDate(periodEnd * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("dateStat", "jasper"), getReport("dateStat", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                putOrEmpty(parameters, "startDate", startDate.toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "endDate", endDate.toString(ChannelService.FORMATTER));

                List<RoomTypeValue> rooms = getRooomValues(startDate, endDate);

                List<Room> allRooms = roomRepository.findAll(new Specification<Room>() {
                    @Override
                    public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Predicate approved = cb.equal(root.<Boolean>get("approved"), true);
                        return cb.and(approved);
                    }
                });
                Integer totalRoomQty = allRooms.size();

                ConcurrentHashMap<LocalDate, Integer> map = new ConcurrentHashMap<>();

                for (RoomTypeValue room : rooms) {
                    map.putIfAbsent(room.getDate(), 0);
                    Integer freeRooms = map.get(room.getDate());
                    freeRooms += room.getRoomsAvailable();
                }
                List<LocalDate> sortedMapKeys = new ArrayList(map.keySet());
                Collections.sort(sortedMapKeys, new Comparator<LocalDate>() {
                    public int compare(LocalDate o1, LocalDate o2) {
                        return o1.compareTo(o2);
                    }
                });

                Integer totalRoomsToSell = 0;
                Integer totalBooked = 0;
                Integer totalLiving = 0;
                Integer totalLivingPersons = 0;
                Long totalProfit = 0L;
                List<DateStatItem> dataCollection = new ArrayList<>();

                for (final LocalDate date : sortedMapKeys) {
                    Integer freeRooms = map.get(date);

                    List<RoomUse> dateRoomUses = roomUseRepository.findAll(new Specification<RoomUse>() {
                        @Override
                        public Predicate toPredicate(Root<RoomUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            RoomUse.Status[] statuses = {BaseGroupRoomUse.Status.BOOKING_FREE, BaseGroupRoomUse.Status.BOOKING_WARRANTY, BaseGroupRoomUse.Status.LIVING, BaseGroupRoomUse.Status.OUTGO};
                            Predicate status = root.<RoomUse.Status>get("status").in(statuses);
                            Predicate sDate = cb.lessThanOrEqualTo(root.<LocalDate>get("startDate"), date);
                            Predicate eDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("endDate"), date);
                            return cb.and(sDate, eDate, status);
                        }
                    });

                    Integer qty = 0;
                    Long profit = 0L;
                    Integer booked = 0;
                    Integer living = 0;
                    Integer livingPersons = 0;
                    for (RoomUse item : dateRoomUses) {
                        RoomUse.Status status = item.getStatus();
                        List<GroupMemberToRoomUse> groupMembers = groupMemberToRoomUseRepository.findByRoomUse(item);

                        if (status == BaseGroupRoomUse.Status.LIVING) {
                            living += 1;
                            livingPersons += groupMembers == null ? 1 : groupMembers.size();
                        } else if (status == BaseGroupRoomUse.Status.BOOKING_FREE || status == BaseGroupRoomUse.Status.BOOKING_WARRANTY) {
                            booked += 1;
                        }
                        profit += item.getTotal();
                    }

                    totalRoomsToSell += freeRooms;
                    totalBooked += booked;
                    totalLiving += living;
                    totalLivingPersons += livingPersons;
                    totalProfit += profit;

                    dataCollection.add(new DateStatItem(
                            date.toString(ChannelService.FORMATTER),
                            String.valueOf(freeRooms),
                            String.valueOf(booked),
                            String.valueOf(living),
                            String.valueOf(livingPersons),
                            String.format("%.02f", (float) profit / 100),
                            livingPersons > 0 ? String.format("%.02f", ((float) profit / 100) / livingPersons) : "0",
                            String.valueOf((booked + living) / totalRoomQty * 100)
                    ));
                }
                parameters.put("dataCollection", dataCollection);

                putOrEmpty(parameters, "totalRoomsToSell", totalRoomsToSell);
                putOrEmpty(parameters, "totalBooked", totalBooked);
                putOrEmpty(parameters, "totalLiving", totalLiving);
                putOrEmpty(parameters, "totalLivingPersons", totalLivingPersons);
                putOrEmpty(parameters, "totalProfit", String.format("%.02f", (float) totalProfit / 100));
            }
        }.createReport();
    }

    public List<RoomTypeValue> getRooomValues(final LocalDate start, final LocalDate end) {
        List<RoomTypeValue> rtValues = new ArrayList<>();

        List<RoomType> roomTypes = getRoomTypes();
        for (final RoomType roomType : roomTypes) {
            LocalDate current = start;
            List<RoomTypeValue> rtvs = roomTypeValueRepository.findAll(new Specification<RoomTypeValue>() {
                @Override
                public Predicate toPredicate(Root<RoomTypeValue> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    Predicate sDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("date"), start);
                    Predicate eDate = cb.lessThanOrEqualTo(root.<LocalDate>get("date"), end);
                    Predicate rt = cb.equal(root.<RoomType>get("roomType"), roomType);

                    return cb.and(sDate, eDate, rt);
                }
            });
            Collections.sort(rtvs, RoomTypeValue.DATE_COMPARATOR);
            for (RoomTypeValue rtv : rtvs) {
                while (current.isBefore(rtv.getDate())) {
                    rtValues.add(createRoomValue(current, roomType));
                    current = current.plusDays(1);
                }
                current = current.plusDays(1);
            }
            while (!current.isAfter(end)) {
                rtValues.add(createRoomValue(current, roomType));
                current = current.plusDays(1);
            }
            rtValues.addAll(rtvs);
        }
        return rtValues;
    }

    private RoomTypeValue createRoomValue(LocalDate date, RoomType roomType) {
        RoomTypeValue newValue = new RoomTypeValue();
        newValue.setRoomType(roomType);
        newValue.setDate(date);
        newValue.setRoomsAvailable(roomType.getOtaRooms());
        return newValue;
    }

    private List<RoomType> getRoomTypes() {
        List<RoomType> roomTypes = roomTypeRepository.findAll(new Specification<RoomType>() {
            @Override
            public Predicate toPredicate(Root<RoomType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate isApproved = cb.equal(root.<RoomType>get("approved"), true);
                return cb.and(isApproved);
            }
        });
        return roomTypes;
    }

    public List<RoomTypeValue> roomsToDates(List<RoomTypeValue> rooms, final LocalDate start, final LocalDate end) {
        List<RoomTypeValue> rtd = new ArrayList<>();

        return rtd;
    }
}