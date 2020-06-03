package com.idgindigo.pms.service.validation;

import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import org.springframework.stereotype.Service;

/**
 * @author valentyn_vakatsiienko
 * @since 6/12/14 11:20 AM
 */
@Service
public class ApprovedValidationService {
    public <T extends ApprovableEntity> void validateApproved(T dependency, ApprovableRepository<T> repository, ExceptionGenerator generator) {
        if (dependency == null || dependency.getId() == null || !repository.isApproved(dependency.getId())) {
            throw generator.generate();
        }
    }

    public interface ExceptionGenerator<T extends RestFriendlyException> {
        T generate();
    }
}
