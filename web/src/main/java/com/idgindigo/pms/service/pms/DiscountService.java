package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.extranet.person.Adult;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.extranet.person.AdultRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import com.idgindigo.pms.repository.pms.CompanyRepository;
import com.idgindigo.pms.repository.pms.PaymentRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/20/13 11:31 AM
 */
@Service
public class DiscountService {
    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private AdultRepository adultRepository;
    @Inject
    private CustomerGroupRepository repository;
    @Inject
    private BaseServiceUseRepository serviceUseRepository;
    @Inject
    private BillRepository billRepository;
    @Inject
    private BillService billService;
    @Inject
    private PaymentRepository paymentRepository;

    public int getDiscountForGroup(CustomerGroup group) {
        if (group.getId() != null) {
            return repository.findOne(group.getId()).getDiscount();
        }
        if (group.getCompany() != null) {
            return companyRepository.findOne(group.getCompany().getId()).getDiscount();
        } else {
            Adult customer = group.getCustomer();
            if (customer.getId() != null) {
                customer = adultRepository.findOne(customer.getId());
            }
            return customer.getDiscount();
        }
    }

//    /**
//     * @param group
//     * @param discount
//     * @return true if refund was created, false otherwise
//     */
//    public boolean setGroupDiscount(CustomerGroup group, int discount) {
//        group = repository.findOne(group.getId());
//        repository.setDiscount(group.getId(), discount);
//
//        int old = group.getDiscount();
//        List<SimpleServiceUse> livingUses = serviceUseRepository.findByRoomUseCustomerGroupId(group.getId());
//        for (SimpleServiceUse LIVING_USE : livingUses) {
//            long total = LIVING_USE.getTotal();
//            total = (100 - discount) * total / (100 - old);
//            LIVING_USE.setTotal(total);
//        }
//        serviceUseRepository.save(livingUses);
//
//        List<Bill> bills = billRepository.findByCustomerGroup(group);
//        Map<Bill, Long> totalByBill = new HashMap();
//        for (Bill bill : bills) {
//            totalByBill.put(bill, bill.getTotal());
//        }
//
//        billService.updateTotal(group);
//
//        bills = billRepository.findByCustomerGroup(group);
//        Refund refund = null;
//        for (Bill bill : bills) {
//            if (!totalByBill.containsKey(bill)) {
//                break;
//            }
//            long oldTotal = totalByBill.get(bill);
//            if (oldTotal > bill.getTotal()) {
//                long paid = paymentRepository.getCurrentSum(bill);
//                if (paid > bill.getTotal()) {
//                    if (refund == null) {
//                        refund = new Refund();//TODO Set group
//                    }
//                    refund.setTotal(refund.getTotal() + (paid - bill.getTotal()));
//                }
//                if (paid >= bill.getTotal()) {
//                    bill.setFullyPaid(true);
//                }
//            } else if (oldTotal < bill.getTotal()) {
//                bill.setFullyPaid(false);
//            }
//        }
//        return false;
//    }
//
//    /**
//     * @param bill
//     * @param discount
//     * @return true if refund was created, false otherwise
//     */
//    public boolean setBillDiscount(Bill bill, long discount) {
//        return false;
//    }
}
