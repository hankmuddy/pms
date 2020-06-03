package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.extranet.CustomerGroup;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.CustomerGroupRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 5:33 PM
 */
@Service
public class CustomerGroupApproveService extends GenericApproveService<CustomerGroup> {
    @Inject
    private CustomerGroupRepository repository;

    @Override
    public ApprovableRepository<CustomerGroup> getApproveRepository() {
        return repository;
    }
}
