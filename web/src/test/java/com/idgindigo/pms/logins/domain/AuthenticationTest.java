package com.idgindigo.pms.logins.domain;

import com.idgindigo.pms.domain.JpaTests;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.utils.AuthenticationProvider;
import com.idgindigo.pms.utils.EntityProvider;
import com.idgindigo.pms.utils.Visitor;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author valentyn_vakatsiienko
 * @since 5/7/14 5:46 PM
 */
public class AuthenticationTest extends JpaTests {
    @Inject
    private AuthenticationRepository repository;
    @Inject
    private AuthenticationProvider provider;

    @Test(dataProvider = "testFindAdministrative")
    public void testFindAdministrative(EntityProvider<? extends Authentication> adminUserProvider) {
        String username;
        do {
            username = EntityProvider.randomAlphabeticString();
        } while (!repository.findByUsername(username).isEmpty());

        createWithUsername(username, provider.hotelUser);
        createWithUsername(username, adminUserProvider);

        Collection<String> adminUserTypes = Arrays.asList(Admin.ADMIN, Manager.MANAGER, ManagerSupervisor.MANAGER_SUPERVISOR);

        Authentication authentication = repository.findAdministrativeByUsername(username);
        assertTrue(adminUserTypes.contains(authentication.getUserType()));
        assertFalse(authentication.getUserType().equals(HotelUser.USER));
    }

    @DataProvider(name = "testFindAdministrative")
    public Object[][] getData() {
        List<Object[]> result = new ArrayList<>(3);
        result.add(new Object[]{provider.manager});
        result.add(new Object[]{provider.managerSupervisor});
        result.add(new Object[]{provider});
        return result.toArray(new Object[result.size()][]);
    }

    private <T extends Authentication> T createWithUsername(final String username, EntityProvider<T> provider) {
        return provider.getPersistentEntity(new Visitor<T>() {
            @Override
            public void visit(T entity) {
                entity.setUsername(username);
            }
        });
    }
}
