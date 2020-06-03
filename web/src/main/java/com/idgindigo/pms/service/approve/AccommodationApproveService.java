package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.pms.Accommodation;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.pms.AccommodationRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 6:12 PM
 */
@Service
public class AccommodationApproveService extends GenericApproveService<Accommodation> {
    @Inject
    private AccommodationRepository repository;

    @Override
    public ApprovableRepository<Accommodation> getApproveRepository() {
        return repository;
    }
}
