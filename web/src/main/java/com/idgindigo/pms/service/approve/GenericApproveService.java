package com.idgindigo.pms.service.approve;

import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.restutils.exception.ApproveException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author valentyn_vakatsiienko
 * @since 12/26/13 5:21 PM
 */
public abstract class GenericApproveService<T extends ApprovableEntity> implements ApproveService<T> {
    @Override
    public T approve(Long id) {
        T entity = getApproveRepository().findOne(id);
        if (entity == null) {
            throw new EntityNotFoundException();
        }
        if (entity.getApproved()) {
            return entity;
        }
        validateDependenciesApproved(entity);
        getApproveRepository().approve(id);
        entity.setApproved(true);
        return entity;
    }

    protected void validateDependenciesApproved(T entity) {
        List<String> sources = checkDependenciesApproved(entity);
        if (!sources.isEmpty()) {
            throw new ApproveException(sources);
        }
    }

    protected List<String> checkDependenciesApproved(T entity) {
        return new ArrayList<>();
    }

}
