package com.idgindigo.pms.web.controller;

import com.idgindigo.pms.domain.ApprovableEntity;
import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.utils.Visitor;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 10:23 AM
 */
public abstract class ApprovableControllerTest<T extends ApprovableEntity> extends BaseWebCrudTest<T> {

    @Test
    public void testApproved() throws Exception {
        T entity = getProvider().getPersistentEntity(new Visitor<T>() {
            @Override
            public void visit(T entity) {
                entity.setApproved(false);
            }
        });
        Long id = entity.getId();

        assertTrue(getProvider().getRepository() instanceof ApprovableRepository, "a repository of an approvable entity must be of type ApprovableRepository");
        ApprovableRepository<T> repository = (ApprovableRepository<T>) getProvider().getRepository();

        assertFalse(repository.isApproved(id));

        mvc.perform(preparePut(id + "/approved")).andExpect(status().isOk());
        assertTrue(repository.isApproved(id));

        if (doRunUpdateStep()) {
            if (isModificationAllowed()) {
                testOk(preparePut(entity));
            } else {
                testBadRequest(preparePut(entity), RestFriendlyException.APPROVED_ENTITY_MODIFICATION);
            }
        }
        if (doRunDeleteStep()) {
            testBadRequest(prepareDelete(id), RestFriendlyException.APPROVED_ENTITY_MODIFICATION);
        }
    }

    @Override
    protected T createEntity(Visitor<T>... visitor) {
        Visitor<T>[] visitors = new Visitor[visitor.length + 1];
        System.arraycopy(visitor, 0, visitors, 0, visitor.length);
        Visitor<T> visitor1 = new Visitor<T>() {
            @Override
            public void visit(T entity) {
                entity.setApproved(false);
            }
        };
        visitors[visitor.length] = visitor1;
        return super.createEntity(visitors);
    }

    protected boolean isModificationAllowed() {
        return false;
    }
}
