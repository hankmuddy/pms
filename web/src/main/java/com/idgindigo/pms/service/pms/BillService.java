package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.restutils.exception.RefundException;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 11/29/13 10:00 AM
 */
@Service
public class BillService {
    @Inject
    private BillRepository billRepository;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private RoomUseService roomUseService;
    @Inject
    private CustomerGroupService customerGroupService;
    @Inject
    private RefundService refundService;
    @Inject
    private LivingUseService livingUseService;

    public void updateTotal(RoomUse roomUse, BankDetails details) {
        updateTotal(roomUse, false, details);
    }

    public void updateTotal(RoomUse roomUse, boolean deleteEmpty, BankDetails details) {
        List<Bill> bills = baseServiceUseRepository.findBillsByRoomUse(roomUse);

        Refund forCustomer = new Refund(roomUse, true, details);
        Refund forRoomUse = new Refund(roomUse, false, details);

        for (Bill bill : bills) {
            updateTotal(bill, bill.isForCustomer() ? forCustomer : forRoomUse, details);
        }
        if (deleteEmpty) {
            billRepository.deleteEmptyByRoomUse(roomUse);
        }
        handleRefund(forCustomer);
        handleRefund(forRoomUse);
    }

    private void handleRefund(Refund refund) {
        if (refund.getTotal() > 0) {
            refundService.saveRefund(refund);
        }
    }

    public void updateTotal(Bill bill, BankDetails details) {
        updateTotal(bill, null, details);
    }

    public void updateTotal(Bill bill, Refund refund, BankDetails details) {
        long oldTotalPaid = bill.getTotalPaid();

        Long serviceUsesTotal = baseServiceUseRepository.getTotalByBill(bill);
        long newRawTotal = serviceUsesTotal != null ? serviceUsesTotal : 0L;
        if (newRawTotal < bill.getDiscount()) {
            bill.setDiscount(newRawTotal);
            billRepository.setDiscount(bill, bill.getDiscount());
        }
        long newTotal = newRawTotal - bill.getDiscount();

        //Refund
        if (oldTotalPaid > newTotal) {
            if (details == null) {
                throw new RefundException(RefundException.BANK_DETAILS_REQUIRED);
            }
            performRefund(bill, refund, oldTotalPaid, newTotal, details);
        }
        billRepository.updateRawTotal(bill, newRawTotal);
        billRepository.updateTotal(bill, newTotal);
        if (bill.isForCustomer()) {
            customerGroupService.updateTotal(bill.getGroup());
        } else {
            roomUseService.updateTotal(bill.getRoomUse());
        }
    }

    private void performRefund(Bill bill, Refund refund, long oldTotalPaid, long newTotal, BankDetails details) {
        boolean manageRefund = refund == null;
        if (refund == null) {
            if (bill.isForCustomer()) {
                refund = new Refund(bill.getGroup(), details);
            } else {
                refund = new Refund(bill.getRoomUse(), false, details);
            }
        }
        refund.setTotal(refund.getTotal() + oldTotalPaid - newTotal);

        bill.setTotalPaid(newTotal);
        billRepository.updateTotalPaid(bill, bill.getTotalPaid());

        if (bill.isForCustomer()) {
            customerGroupService.updateTotalPaid(bill.getGroup());
        } else {
            roomUseService.updateTotalPaid(bill.getRoomUse());
        }

        if (manageRefund) {
            handleRefund(refund);
        }
    }

    public void handleTotalsChange(Iterable<Bill> modifiedBills, BankDetails details) {
        RefundToSourceMapper mapped = new RefundToSourceMapper(modifiedBills).map();
        Map<CustomerGroup, Refund> refundsByGroup = mapped.getRefundsByGroup();
        Map<RoomUse, Refund> refundsByRoomUse = mapped.getRefundsByRoomUse();

        for (Bill bill : modifiedBills) {
            updateTotal(bill, bill.isForCustomer() ? refundsByGroup.get(bill.getGroup()) : refundsByRoomUse.get(bill.getRoomUse()), details);
        }

        Set<Refund> refunds = new HashSet<>();
        refunds.addAll(refundsByGroup.values());
        refunds.addAll(refundsByRoomUse.values());
        for (Refund refund : refunds) {
            refund.setBankDetails(details);
            handleRefund(refund);
        }
    }

    public Bill getBill(CustomerGroup group) {
        Bill bill = new Bill();
        bill.setTotal(0L);
        bill.setGroup(group);
        return billRepository.save(bill);
    }

    public Bill getBill(RoomUse roomUse) {
        return getBill(roomUse, false);
    }

    public Bill getBill(RoomUse roomUse, boolean forCustomer) {
        Bill bill = new Bill();
        bill.setTotal(0L);
        if (forCustomer) {
            bill.setGroup(roomUse.getCustomerGroup());
        } else {
            bill.setRoomUse(roomUse);
        }
        /*roomUse.setBill(bill);
        } else if (billRepository.isApproved(bill.getId())) {
            throw new RoomUseException(RoomUseException.BILL_IS_ALREADY_APPROVED, "bill");
        } else {
            bill = billRepository.findOne(bill.getId());
            if (!bill.getRoomUse().equals(roomUse)) {
                throw new RoomUseException(RoomUseException.BILL_UPDATE_INVALID_GROUP, "bill.roomUse");
            }
        }*/
        return billRepository.save(bill);
    }

    public void setForCustomer(Bill bill, RoomUse roomUse, boolean forCustomer, BankDetails details) {
        int newDiscount;
        if (forCustomer) {
            billRepository.setForCustomer(bill);
            newDiscount = bill.getGroup().getDiscount();
        } else {
            billRepository.setForRoomUse(roomUse, bill);
            newDiscount = 0;
        }
        updateDiscount(bill, newDiscount, details);
    }

    private void updateDiscount(Bill bill, int discount, BankDetails details) {
        livingUseService.setDiscount(bill, discount);
        handleTotalsChange(Collections.singletonList(bill), details);
    }

    @Getter
    private static class RefundToSourceMapper {
        private Iterable<Bill> modifiedBills;
        private Map<RoomUse, Refund> refundsByRoomUse;
        private Map<CustomerGroup, Refund> refundsByGroup;

        public RefundToSourceMapper(Iterable<Bill> modifiedBills) {
            this.modifiedBills = modifiedBills;
        }

        public RefundToSourceMapper map() {
            Set<RoomUse> affectedRoomUses = new HashSet<>();
            Set<CustomerGroup> affectedGroups = new HashSet<>();
            for (Bill bill : modifiedBills) {
                if (!bill.isForCustomer()) {
                    affectedRoomUses.add(bill.getRoomUse());
                } else {
                    affectedGroups.add(bill.getGroup());
                }
            }
            refundsByRoomUse = new HashMap<>();
            refundsByGroup = new HashMap<>();
            for (RoomUse roomUse : affectedRoomUses) {
                refundsByRoomUse.put(roomUse, new Refund(roomUse, true));
            }
            for (CustomerGroup group : affectedGroups) {
                refundsByGroup.put(group, new Refund(group));
            }
            return this;
        }
    }

    public void setDiscount(Bill bill, long discount, BankDetails details) {
        bill.setDiscount(discount);
        billRepository.setDiscount(bill, discount);
        updateTotal(bill, details);
    }
}
