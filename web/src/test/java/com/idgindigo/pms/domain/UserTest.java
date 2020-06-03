package com.idgindigo.pms.domain;

import com.idgindigo.pms.repository.UserRepository;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.UserProvider;
import com.idgindigo.pms.utils.Visitor;
import org.springframework.dao.DataIntegrityViolationException;
import org.testng.annotations.Test;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

/**
 * @author vomel
 * @since 29.10.13 14:56
 */
public class UserTest extends BasePersistenceTest<User> {

    @Inject
    private UserProvider provider;

    @Inject
    private UserRepository repository;

    @Override
    protected EntityProvider<User> getProvider() {
        return provider;
    }

    @Test(expectedExceptions = DataIntegrityViolationException.class)
    public void testUniqueEmail() {
        final User user1 = provider.getPersistentEntity();
        repository.saveAndFlush(provider.getTransientEntity(new Visitor<User>() {
            @Override
            public void visit(User entity) {
                entity.setUsername(user1.getUsername());
            }
        }));
    }

    @Test(expectedExceptions = ConstraintViolationException.class)
    public void testEmptyLogin() {
        repository.saveAndFlush(provider.getTransientEntity(new Visitor<User>() {
            @Override
            public void visit(User entity) {
                entity.setUsername(null);
            }
        }));
    }
}