package com.idgindigo.pms.domain;

import com.idgindigo.pms.utils.EntityProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * @author vomel
 * @since 29.10.13 14:57
 */
public abstract class BasePersistenceTest<T extends BaseIdentifiable> extends JpaTests {
    public static final int ENTITY_LIST_SIZE = 10;

    @Test
    public void testCrud() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(null);
        //C
        T entity = getProvider().createAndFill();
        visitEntity(entity);
        JpaRepository<T, Long> repository = getProvider().getRepository();
        T savedEntity = repository.saveAndFlush(entity);
        long id = savedEntity.getId();
        //R
        T foundEntity = repository.findOne(id);
        Assert.assertEquals(String.valueOf(foundEntity), String.valueOf(entity));
        //U
        getProvider().updateEntity(foundEntity);
        T updatedEntity = repository.saveAndFlush(foundEntity);

        foundEntity = repository.findOne(id);
        Assert.assertEquals(String.valueOf(foundEntity), String.valueOf(updatedEntity));
        //D
        repository.delete(id);
        Assert.assertNull(repository.findOne(id));

        //List
        int startSize = getProvider().getRepository().findAll().size();
        for (int i = 0; i < ENTITY_LIST_SIZE; i++) {
            T listItem = getProvider().createAndFill();
            getProvider().getRepository().save(listItem);
        }
        assertEquals(getProvider().getRepository().findAll().size(), startSize + ENTITY_LIST_SIZE);
    }

    protected void visitEntity(T entity) {
    }

    protected abstract EntityProvider<T> getProvider();

}