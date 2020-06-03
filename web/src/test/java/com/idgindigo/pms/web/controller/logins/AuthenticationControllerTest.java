package com.idgindigo.pms.web.controller.logins;

import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.restutils.exception.RestFriendlyException;
import com.idgindigo.pms.utils.AuthenticationProvider;
import com.idgindigo.pms.utils.HotelProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import com.idgindigo.pms.web.controller.admin.AuthenticationController;
import junit.framework.Assert;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.assertEquals;

/**
 * @author valentyn_vakatsiienko
 * @since 4/22/14 3:20 PM
 */
public class AuthenticationControllerTest extends InMemoryDbWebTest<Authentication> {
    public static final int TOTAL_AUTHENTICATIONS = 5;
    @Inject
    private AuthenticationProvider provider;
    @Inject
    private HotelProvider hotelProvider;

    @Test(dataProvider = "testSecuredCreate")
    public void testSecuredCreate(Authentication user, Authentication toCreate, ResultMatcher res) throws Exception {
        loginAs(user);
        securedMvc.perform(preparePost(toCreate)).andExpect(res);
    }

    @DataProvider(name = "testSecuredCreate")
    public Object[][] getData_testSecuredCreate() {
        List<Object[]> result = new ArrayList<>(8);
        Authentication user;

        //Admin
        user = provider.getPersistentEntity();
        result.add(new Object[]{user, provider.getTransientEntity(), CREATED});
        result.add(new Object[]{user, provider.managerSupervisor.getTransientEntity(), CREATED});
        result.add(new Object[]{user, provider.manager.getTransientEntity(), CREATED});

        //Manager Supervisor
        user = provider.managerSupervisor.getPersistentEntity();
        result.add(new Object[]{user, provider.getTransientEntity(), FORBIDDEN});
        result.add(new Object[]{user, provider.managerSupervisor.getTransientEntity(), FORBIDDEN});
        result.add(new Object[]{user, provider.manager.getTransientEntity(), CREATED});

        //Manager
        user = provider.manager.getPersistentEntity();
        result.add(new Object[]{user, provider.getTransientEntity(), FORBIDDEN});
        result.add(new Object[]{user, provider.managerSupervisor.getTransientEntity(), FORBIDDEN});
        result.add(new Object[]{user, provider.manager.getTransientEntity(), FORBIDDEN});

        //User
        user = provider.hotelUser.getPersistentEntity();
        result.add(new Object[]{user, provider.getTransientEntity(), FORBIDDEN});
        result.add(new Object[]{user, provider.managerSupervisor.getTransientEntity(), FORBIDDEN});
        result.add(new Object[]{user, provider.manager.getTransientEntity(), FORBIDDEN});
        return result.toArray(new Object[result.size()][]);
    }

    @Test(dataProvider = "testViewSecurity")
    public void testViewSecurity(Authentication user, List<Authentication> visible, List<Authentication> invisible) throws Exception {
        loginAs(user);
        for (Authentication authentication : visible) {
            testAuthorized(prepareGet(authentication.getId()));
        }
        for (Authentication authentication : invisible) {
            testForbidden(prepareGet(authentication.getId()));
        }
    }

    @DataProvider(name = "testViewSecurity")
    public Object[][] getData_testViewSecurity() {
        List<Object[]> result = new ArrayList<>(8);
        List<Authentication> nonBound = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            nonBound.add(provider.getPersistentEntity());
            nonBound.add(provider.hotelUser.getPersistentEntity());
            nonBound.add(provider.manager.getPersistentEntity());
            nonBound.add(provider.managerSupervisor.getPersistentEntity());
        }

        final Manager manager = provider.manager.getPersistentEntity();
        List<Authentication> forManager = new ArrayList<>();
        for (int i = 0; i < TOTAL_AUTHENTICATIONS; i++) {
            forManager.add(provider.hotelUser.getPersistentEntity(new Visitor<HotelUser>() {
                @Override
                public void visit(HotelUser entity) {
                    entity.setHotel(hotelProvider.getPersistentEntity(new Visitor<Hotel>() {
                        @Override
                        public void visit(Hotel entity) {
                            entity.setManager(manager);
                        }
                    }));
                }
            }));
        }
        result.add(new Object[]{manager, forManager, nonBound});

        final ManagerSupervisor supervisor = provider.managerSupervisor.getPersistentEntity();
        List<Authentication> forSupervisor = new ArrayList<>();
        for (int i = 0; i < TOTAL_AUTHENTICATIONS; i++) {
            forSupervisor.add(provider.hotelUser.getPersistentEntity(new Visitor<HotelUser>() {
                @Override
                public void visit(HotelUser entity) {
                    entity.setHotel(hotelProvider.getPersistentEntity(new Visitor<Hotel>() {
                        @Override
                        public void visit(Hotel entity) {
                            entity.setSupervisor(supervisor);
                        }
                    }));
                }
            }));
        }
        result.add(new Object[]{supervisor, forSupervisor, nonBound});

        HotelUser user = provider.hotelUser.getPersistentEntity();
        result.add(new Object[]{user, Collections.emptyList(), provider.getRepository().findAll()});

        Authentication admin = provider.getPersistentEntity();
        result.add(new Object[]{admin, provider.getRepository().findAll(), Collections.emptyList()});
        return result.toArray(new Object[result.size()][]);
    }

    @Inject
    private AuthenticationController controller;

    @Test
    public void testDuplicateAdmin() {
        loginAs(provider.getPersistentEntity());

        final Authentication authentication = provider.manager.getPersistentEntity();
        Authentication toCreate = provider.managerSupervisor.getTransientEntity(new Visitor<ManagerSupervisor>() {
            @Override
            public void visit(ManagerSupervisor entity) {
                entity.setUsername(authentication.getUsername());
            }
        });
        try {
            controller.create(toCreate);
            Assert.fail();
        } catch (RestFriendlyException e) {
            assertEquals(e.getMessage(), RestFriendlyException.DUPLICATE_ENTRY);
            assertEquals(e.getSource(), "username");
        }
    }

    @Override
    protected String getUrl() {
        return AuthenticationController.URL + "/";
    }
}
