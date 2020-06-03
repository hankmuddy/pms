package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.service.Service;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import org.joda.time.LocalDate;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 3/4/14 11:09 AM
 */
@org.springframework.stereotype.Service
public class BaseServiceUseService {
    @Inject
    private BaseServiceUseRepository repository;
    @Inject
    private SystemServiceService systemServiceService;
    @Inject
    private BillService billService;

    public void move(RoomUse from, RoomUse to, LocalDate sinceDate, boolean moveAdditionalBeds, BankDetails details) {
        List<BaseServiceUse> serviceUses = repository.findByRoomUseAndAfterDateIncluding(from, sinceDate);
        List<BaseServiceUse> forCustomer = new ArrayList<>();
        List<BaseServiceUse> forRoomUse = new ArrayList<>();
        for (BaseServiceUse serviceUse : serviceUses) {
            if (serviceUse.getBill().isForCustomer()) {
                forCustomer.add(serviceUse);
            } else {
                forRoomUse.add(serviceUse);
            }
        }

        if (!forRoomUse.isEmpty()) {
            doMove(moveAdditionalBeds, forRoomUse, billService.getBill(to, false), to);
        }
        if (!forCustomer.isEmpty()) {
            doMove(moveAdditionalBeds, forCustomer, billService.getBill(to, true), to);
        }
        billService.updateTotal(from, details);
    }

    private void doMove(boolean moveAdditionalBeds, List<BaseServiceUse> serviceUses, Bill bill, RoomUse roomUse) {
        Service adultBed = systemServiceService.getAdultBedService();
        Service childBed = systemServiceService.getChildBedService();

        List<BaseServiceUse> filtered = new ArrayList<>();
        for (BaseServiceUse serviceUse : serviceUses) {
            if (moveAdditionalBeds || !isAdditionalBed(serviceUse, childBed, adultBed)) {
                filtered.add(serviceUse);
            }
        }
        if (!filtered.isEmpty()) {
            repository.setBill(filtered, bill);
            repository.setRoomUse(filtered, roomUse);
        }
//        billService.updateTotal(bill);
    }

    private boolean isAdditionalBed(BaseServiceUse serviceUse, Service childBed, Service adultBed) {
        return serviceUse.getService().equals(adultBed) || serviceUse.getService().equals(childBed);
    }
}