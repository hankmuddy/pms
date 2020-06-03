package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.repository.BaseRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.service.approve.ApproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author valentyn_vakatsiienko
 * @since 11/26/13 6:18 PM
 */
public abstract class ApprovableController<T extends ApprovableEntity> extends BaseCrudController<T> {
    protected static final Logger logger = LoggerFactory.getLogger(ApprovableController.class);
    public static final String APPROVE_URI = "{id}/approved";

    @Override
    @Transactional
    public ResponseEntity<T> update(@RequestBody T entity, @PathVariable("id") Long id) {
        if (getApproveService().getApproveRepository().isApproved(id)) {
            return updateApproved(getRepository().findOne(id), entity);
        } else {
            return super.update(entity, id);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        checkModificationAllowed(id);
        super.delete(id);
        return new ResponseEntity<>();
    }

    @RequestMapping(value = APPROVE_URI, method = RequestMethod.PUT)
    @Transactional
    @ResponseBody
    public void approve(@PathVariable("id") Long... ids) {
        for (Long id : ids) {
            if (getApproveService().getApproveRepository().isApproved(id)) {
                logger.warn("Could not approve already approved entity with id: {}", id);
                continue;
            }
            getApproveService().approve(id);
        }
    }

    public abstract <X extends T> ApproveService<X> getApproveService();

    @Override
    public BaseRepository<T> getRepository() {
        return getApproveService().getApproveRepository();
    }

    private void checkModificationAllowed(Long id) {
        if (getApproveService().getApproveRepository().isApproved(id)) {
            denyModification();
        }
    }

    private void denyModification() {
        throw new RestFriendlyException(RestFriendlyException.APPROVED_ENTITY_MODIFICATION);
    }

    private ResponseEntity<T> updateApproved(T toUpdate, T updated) {
        applyModifications(toUpdate, updated);
        ResponseEntity<T> res = super.update(toUpdate, toUpdate.getId());
        onModificationSuccess(res.getContent());
        return res;
    }

    protected void applyModifications(T toUpdate, T updated) {
        denyModification();
    }

    protected void onModificationSuccess(T saved) {
    }

    protected <X extends ApprovableEntity> boolean isApproved(X entity, ApprovableRepository<X> repository) {
        return entity != null && entity.getId() != null && repository.isApproved(entity.getId());
    }
}
