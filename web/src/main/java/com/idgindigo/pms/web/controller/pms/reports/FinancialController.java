package com.idgindigo.pms.web.controller.pms.reports;

import com.idgindigo.pms.domain.extranet.roomtype.RoomType;
import com.idgindigo.pms.domain.pms.GroupMemberToRoomUse;
import com.idgindigo.pms.domain.pms.LivingUse;
import com.idgindigo.pms.domain.pms.Room;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.service.channels.ChannelService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by misha on 25.04.14.
 */
@Controller
public class FinancialController extends ReportController {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoomTypeSalesItem {
        private String roomType;
        private String qty;
        private String fullLoad;
        private String roomNightLoad;
        private String guestBedLoad;
        private String profit;
        private String load;
        private String roomProfit;
        private String saleRate;
    }

    @RequestMapping("roomTypeSales")
    public void createGuestForm(@RequestParam(value = "periodStart", required = false) final Long periodStart, @RequestParam(value = "periodEnd", required = false) final Long periodEnd, HttpServletResponse response) throws JRException, IOException {
        final LocalDate startDate = periodStart == null ? settingsService.getHotelDate() : new LocalDate(periodStart * 1000, DateTimeZone.UTC);
        final LocalDate endDate = periodEnd == null ? startDate.plusDays(1) : new LocalDate(periodEnd * 1000, DateTimeZone.UTC);
        new ReportCreator(response, getReport("roomTypeSales", "jasper"), getReport("roomTypeSales", "pdf")) {
            @Override
            void collectParameters(Map<String, Object> parameters) {
                Integer days = Days.daysBetween(startDate, endDate).getDays();
                Integer nights = days <= 1 ? 1 : days - 1;
                putOrEmpty(parameters, "startDate", startDate.toString(ChannelService.FORMATTER));
                putOrEmpty(parameters, "endDate", endDate.toString(ChannelService.FORMATTER));

                List<Room> allRooms = roomRepository.findAll(new Specification<Room>() {
                    @Override
                    public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                        Predicate approved = cb.equal(root.<Boolean>get("approved"), true);
                        return cb.and(approved);
                    }
                });

                Integer totalQty = allRooms.size();
                Integer totalFullLoad = 0;
                Float totalRoomNightLoad = 0F;
                Float totalGuestBedLoad = 0F;
                Float totalProfit = 0F;
                Float totalLoad = 0F;
                Float totalRoomProfit = 0F;
                Float totalSaleRate = 0F;

                List<RoomTypeSalesItem> dataCollection = new ArrayList<>();

                List<RoomType> roomTypes = roomTypeRepository.findAll();

                for (final RoomType roomType : roomTypes) {
                    List<Room> roomsOfType = roomRepository.findAll(new Specification<Room>() {
                        @Override
                        public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            Predicate approved = cb.equal(root.<Boolean>get("approved"), true);
                            Predicate rt = cb.equal(root.<RoomType>get("roomType"), roomType);
                            return cb.and(approved, rt);
                        }
                    });
                    Integer roomsOfTypeQty = roomsOfType.size();
                    totalQty += roomsOfTypeQty;

                    List<LivingUse> livingUses = livingUseRepository.findAll(new Specification<LivingUse>() {
                        @Override
                        public Predicate toPredicate(Root<LivingUse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                            Predicate rType = cb.equal(root.join("roomUse").join("room").join("roomType").<Long>get("id"), roomType.getId());
                            Predicate sDate = cb.greaterThanOrEqualTo(root.<LocalDate>get("date"), startDate);
                            Predicate eDate = cb.lessThanOrEqualTo(root.<LocalDate>get("date"), endDate);
                            return cb.and(rType, sDate, eDate);
                        }
                    });

                    Long profitOfType = 0L;
                    Integer roomsTotal = 0;
                    Integer guestsTotal = 0;
                    for (LivingUse livingUse : livingUses) {
                        roomsTotal += 1;
                        profitOfType += livingUse.getTotal();

                        RoomUse livingRoomUse = livingUse.getRoomUse();
                        List<GroupMemberToRoomUse> groupMembers = livingRoomUse == null ? null : groupMemberToRoomUseRepository.findByRoomUse(livingRoomUse);
                        guestsTotal += groupMembers == null ? 0 : groupMembers.size();
                    }

                    Integer fullLoad = roomType.getAdults() + roomType.getChildren() + roomType.getAdditional();
                    Float roomNightLoad = (float) roomsTotal / (float) nights;
                    Float guestBedLoad = (float) guestsTotal / (float) nights;
                    Float load = roomNightLoad / (float) fullLoad * 100;
                    Float roomProfit = roomsTotal == 0 ? 0F : (float) profitOfType / (float) roomsTotal;
                    Float saleRate = roomNightLoad == 0 ? 0F : (float) profitOfType / (float) roomNightLoad;

                    totalFullLoad += fullLoad;
                    totalRoomNightLoad += roomNightLoad;
                    totalGuestBedLoad += guestBedLoad;
                    totalProfit += profitOfType;
                    totalRoomProfit += roomProfit;
                    totalSaleRate += saleRate;

                    dataCollection.add(new RoomTypeSalesItem(
                            roomType.getName(),
                            String.valueOf(roomsOfTypeQty),
                            String.valueOf(fullLoad),
                            toDouble((float) roomNightLoad),
                            toDouble((float) guestBedLoad),
                            toMoney((float) profitOfType),
                            toDouble((float) load),
                            toMoney((float) roomProfit),
                            toMoney((float) saleRate)
                    ));

                    parameters.put("dataCollection", dataCollection);

                    totalLoad = (float) totalRoomNightLoad / (float) totalFullLoad * 100;

                    putOrEmpty(parameters, "totalQty", String.valueOf(totalQty));
                    putOrEmpty(parameters, "totalFullLoad", String.valueOf(totalFullLoad));
                    putOrEmpty(parameters, "totalRoomNightLoad", toDouble((float) totalRoomNightLoad));
                    putOrEmpty(parameters, "totalGuestBedLoad", toDouble((float) totalGuestBedLoad));
                    putOrEmpty(parameters, "totalProfit", toMoney((float) totalProfit));
                    putOrEmpty(parameters, "totalLoad", toDouble((float) totalLoad));
                    putOrEmpty(parameters, "totalRoomProfit", toMoney((float) totalRoomProfit));
                    putOrEmpty(parameters, "totalSaleRate", toMoney((float) totalSaleRate));
                }
            }
        }.createReport();
    }
}
