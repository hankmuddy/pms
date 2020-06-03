package com.idgindigo.pms.service.pms;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.BankDetails;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.domain.pms.Refund;
import com.idgindigo.pms.domain.pms.RoomUse;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.RefundRepository;
import com.idgindigo.pms.restutils.exception.RefundException;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author valentyn_vakatsiienko
 * @since 1/10/14 12:05 PM
 */
@Service
public class RefundService {
    @Inject
    private RefundRepository repository;
    @Inject
    private BaseServiceUseRepository baseServiceUseRepository;
    @Inject
    private BillService billService;

    private final RefundProvider<RoomUse> roomUseRefundProvider = new RefundProvider<RoomUse>() {
        public Refund getRefund(RoomUse roomUse, BankDetails details) {
            return new Refund(roomUse, false, details);
        }
    };
    private final RefundProvider<CustomerGroup> groupRefundProvider = new RefundProvider<CustomerGroup>() {
        @Override
        public Refund getRefund(CustomerGroup group, BankDetails details) {
            return new Refund(group, details);
        }
    };

    public List<Refund> refund(Refund refund) {
        List<BaseServiceUse> serviceUses = refund.getServiceUses();
        if (serviceUses.isEmpty()) {
            throw new RefundException(RefundException.SERVICE_USES_EMPTY, "serviceUses");
        }
        return refund(serviceUses, refund.getBankDetails());
    }

    public List<Refund> refund(List<? extends BaseServiceUse> serviceUses, BankDetails details) {
        if (serviceUses.isEmpty()) {
            return Collections.emptyList();
        }
        List<Refund> res = new ArrayList<>();

        ServiceUseToSourceMapper serviceUseToSourceMapper = new ServiceUseToSourceMapper(serviceUses).map();

        res.addAll(manageBySource(serviceUseToSourceMapper.getByGroup(), groupRefundProvider, details));
        res.addAll(manageBySource(serviceUseToSourceMapper.getByRoomUse(), roomUseRefundProvider, details));

        return res;
    }

    private <T> Collection<Refund> manageBySource(Map<T, List<BaseServiceUse>> byKey, RefundProvider<T> provider, BankDetails details) {
        Collection<Refund> res = new ArrayList<Refund>() {
            @Override
            public boolean add(Refund o) {
                return o != null && super.add(o);
            }
        };
        for (Map.Entry<T, List<BaseServiceUse>> entry : byKey.entrySet()) {
            Collection<BaseServiceUse> serviceUses = entry.getValue();
            if (!serviceUses.isEmpty()) {
                res.add(refund(serviceUses, provider.getRefund(entry.getKey(), details)));
            }
        }
        return res;
    }

    private Refund refund(Iterable<? extends BaseServiceUse> serviceUses, Refund refund) {
        Set<Bill> modifiedBills = new HashSet<>();
        List<BaseServiceUse> serviceUsesToSave = new ArrayList<>();
        for (BaseServiceUse serviceUse : serviceUses) {
            if (refund(serviceUse, refund)) {
                serviceUsesToSave.add(serviceUse);
                modifiedBills.add(serviceUse.getBill());
            }
        }
        if (refund.getId() == null) {
            saveRefund(refund);
        }
        baseServiceUseRepository.save(serviceUsesToSave);
        for (Bill bill : modifiedBills) {
            billService.updateTotal(bill, refund, refund.getBankDetails());
        }
        repository.updateTotal(refund, refund.getTotal());
        return refund;
    }

    private boolean refund(BaseServiceUse serviceUse, Refund refund) {
        serviceUse = baseServiceUseRepository.findOne(serviceUse.getId());
        if (serviceUse.isRefunded()) {
            return false;
        }
        serviceUse.setRefund(refund);
        return true;
    }

    public void saveRefund(Refund refund) {
        if (refund.getTotal() > 0 && refund.getBankDetails() == null) {
            throw new RefundException(RefundException.BANK_DETAILS_REQUIRED, "bankDetails");
        }
        repository.save(refund);
    }

    private interface RefundProvider<T> {
        Refund getRefund(T source, BankDetails details);
    }

    @Getter
    private static class ServiceUseToSourceMapper {
        private List<? extends BaseServiceUse> serviceUses;
        private Map<CustomerGroup, List<BaseServiceUse>> byGroup;
        private Map<RoomUse, List<BaseServiceUse>> byRoomUse;

        public ServiceUseToSourceMapper(List<? extends BaseServiceUse> serviceUses) {
            this.serviceUses = serviceUses;
        }

        public ServiceUseToSourceMapper map() {
            byGroup = new HashMap<>();
            byRoomUse = new HashMap<>();
            for (BaseServiceUse serviceUse : serviceUses) {
                Bill bill = serviceUse.getBill();
                if (bill.isForCustomer()) {
                    put(byGroup, serviceUse, bill.getGroup());
                } else {
                    put(byRoomUse, serviceUse, serviceUse.getRoomUse());
                }
            }
            return this;
        }

        private <T> void put(Map<T, List<BaseServiceUse>> byKey, BaseServiceUse serviceUse, T key) {
            if (byKey.get(key) == null) {
                byKey.put(key, new ArrayList<BaseServiceUse>());
            }
            byKey.get(key).add(serviceUse);
        }

    }
}
