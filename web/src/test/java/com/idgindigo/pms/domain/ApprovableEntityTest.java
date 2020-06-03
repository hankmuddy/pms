package com.idgindigo.pms.domain;

import com.idgindigo.pms.repository.ApprovableRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import junit.framework.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 11/27/13 10:08 AM
 */
public abstract class ApprovableEntityTest<T extends ApprovableEntity> extends BasePersistenceTest<T> {

    @Test
    public void testApproved() {
        ApprovableRepository<T> repo = gettApprovableRepository();

        T entity = getProvider().getPersistentEntity(new Visitor<T>() {
            @Override
            public void visit(T entity) {
                entity.setApproved(false);
            }
        });
        Long id = entity.getId();

        assertFalse(repo.isApproved(id));

        repo.approve(id);
        assertTrue(repo.isApproved(id));
    }

    @Test
    public void testIsApproved() {
        ApprovableRepository<T> repo = gettApprovableRepository();
        T entity = getProvider().getPersistentEntity(new Visitor<T>() {
            @Override
            public void visit(T entity) {
                entity.setApproved(false);
            }
        });
        Assert.assertFalse(repo.isApproved(entity.getId()));
        repo.approve(entity.getId());
        Assert.assertTrue(repo.isApproved(entity.getId()));

        Long nonExisting;
        do {
            nonExisting = EntityProvider.randomPositiveLong();
        } while (repo.exists(nonExisting));
        Assert.assertFalse(repo.isApproved(nonExisting));
    }

    private ApprovableRepository<T> gettApprovableRepository() {
        assertTrue(getProvider().getRepository() instanceof ApprovableRepository);
        return (ApprovableRepository<T>) getProvider().getRepository();
    }
}
