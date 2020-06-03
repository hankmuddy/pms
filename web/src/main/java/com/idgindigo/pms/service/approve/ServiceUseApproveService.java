package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.pms.BaseServiceUse;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.extranet.service.ServiceRepository;
import com.idgindigo.pms.repository.pms.BaseServiceUseRepository;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 5:37 PM
 */
@org.springframework.stereotype.Service
public class ServiceUseApproveService extends GenericApproveService<BaseServiceUse> {
    @Inject
    private BaseServiceUseRepository repository;
    @Inject
    private ServiceRepository serviceRepository;

    @Override
    public ApprovableRepository<BaseServiceUse> getApproveRepository() {
        return repository;
    }
}
