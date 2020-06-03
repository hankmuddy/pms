package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.domain.pms.Bill;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;
import com.idgindigo.pms.repository.pms.BillRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 5:25 PM
 */
@Service
public class BillApproveService extends GenericApproveService<Bill> {
    @Inject
    private BillRepository repository;
    @Inject
    private CustomerGroupRepository customerGroupRepository;
    @Inject
    private CustomerGroupApproveService customerGroupApproveService;
    @Inject
    private ServiceUseApproveService serviceUseApproveService;
    @Inject
    private BaseServiceUseRepository serviceUseApproveRepository;

    @Override
    public Bill approve(Long id) {
        Bill bill = repository.findOne(id);
        customerGroupApproveService.approve(getGroup(bill).getId());
        for (BaseServiceUse serviceUse : bill.getServiceUses()) {
            serviceUseApproveService.approve(serviceUse.getId());
        }
        return super.approve(id);
    }

    @Override
    public ApprovableRepository<Bill> getApproveRepository() {
        return repository;
    }

    @Override
    protected List<String> checkDependenciesApproved(Bill entity) {
        List<String> sources = new ArrayList<>();
        if (!customerGroupRepository.isApproved(getGroup(entity).getId())) {
            sources.add(CustomerGroup.GROUP);
        }
        for (BaseServiceUse serviceUse : entity.getServiceUses()) {
            if (!serviceUseApproveRepository.isApproved(serviceUse.getId())) {
                sources.add("serviceUse");
                break;
            }
        }
        return sources;
    }

    private CustomerGroup getGroup(Bill bill) {
        return bill.isForCustomer() ? bill.getGroup() : bill.getRoomUse().getCustomerGroup();
    }
}
