package com.idgindigo.pms.web.controller.logins;

import com.idgindigo.pms.logins.domain.Admin;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.service.admin.HotelService;
import com.idgindigo.pms.service.multitenancy.TenantService;
import com.idgindigo.pms.utils.AuthenticationProvider;
import com.idgindigo.pms.utils.HotelProvider;
import com.idgindigo.pms.utils.Visitor;
import com.idgindigo.pms.web.controller.InMemoryDbWebTest;
import com.idgindigo.pms.web.controller.admin.HotelController;
import com.idgindigo.pms.web.controller.admin.HotelUserDto;
import junit.framework.Assert;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.ResultMatcher;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.idgindigo.pms.utils.EntityProvider.randomAlphabeticString;
import static com.idgindigo.pms.web.controller.admin.HotelController.UserCreateDto;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

/**
 * @author valentyn_vakatsiienko
 * @since 4/30/14 1:57 PM
 */
public class HotelControllerTest extends InMemoryDbWebTest<Hotel> {
    private static final int TOTAL_HOTELS = 5;
    @Inject
    private AuthenticationProvider authenticationProvider;
    @Inject
    private HotelProvider provider;
    @Inject
    private TenantService tenantService;
    @Inject
    private HotelService hotelService;
    @Inject
    private HotelController controller;

    @Test(dataProvider = "testSecuredCreate")
    public void testSecuredCreate(Authentication user, ResultMatcher res) throws Exception {
        loginAs(user);
        new NonStrictExpectations(hotelService) {{
            hotelService.initHotel(withInstanceOf(HotelUserDto.class), null);
        }};
        new NonStrictExpectations(tenantService) {{
            tenantService.initTenant(withInstanceOf(String.class));
        }};

        securedMvc.perform(preparePost(getDto())).andExpect(res);
    }

    @DataProvider(name = "testSecuredCreate")
    public Object[][] getData_testSecuredCreate() {
        List<Object[]> result = new ArrayList<>(8);

        //Admin
        result.add(new Object[]{authenticationProvider.getPersistentEntity(), OK});

        //Manager Supervisor
        result.add(new Object[]{authenticationProvider.managerSupervisor.getPersistentEntity(), OK});

        //Manager
        result.add(new Object[]{authenticationProvider.manager.getPersistentEntity(), OK});

        //User
        result.add(new Object[]{authenticationProvider.hotelUser.getPersistentEntity(), FORBIDDEN});
        return result.toArray(new Object[result.size()][]);
    }

    private UserCreateDto getDto() {
        UserCreateDto dto = new UserCreateDto();
        dto.setHotel(provider.getTransientEntity());
        HotelUserDto userDto = new HotelUserDto();
        userDto.setAuthentication(authenticationProvider.hotelUser.getTransientEntity());
        userDto.setEmail(randomAlphabeticString());
        userDto.setFirstName(randomAlphabeticString());
        userDto.setLastName(randomAlphabeticString());
        userDto.setPhone(randomAlphabeticString());
        userDto.setPosition(randomAlphabeticString());
        dto.setUser(userDto);
        return dto;
    }

    @Test(dataProvider = "testViewSecurity")
    public void testViewSecurity(Authentication user, List<Hotel> visible, List<Hotel> invisible) throws Exception {
        loginAs(user);
        for (Hotel hotel : visible) {
            testAuthorized(prepareGet(hotel.getId()));
        }
        for (Hotel hotel : invisible) {
            testForbidden(prepareGet(hotel.getId()));
        }
    }

    @DataProvider(name = "testViewSecurity")
    public Object[][] getData_testViewSecurity() {
        List<Object[]> result = new ArrayList<>(8);
        List<Hotel> nonBound = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            nonBound.add(provider.getPersistentEntity());
        }

        final Manager manager = authenticationProvider.manager.getPersistentEntity();
        List<Hotel> forManager = new ArrayList<>();
        for (int i = 0; i < TOTAL_HOTELS; i++) {
            forManager.add(provider.getPersistentEntity(new Visitor<Hotel>() {
                @Override
                public void visit(Hotel entity) {
                    entity.setManager(manager);
                }
            }));
        }
        result.add(new Object[]{manager, forManager, nonBound});

        final ManagerSupervisor supervisor = authenticationProvider.managerSupervisor.getPersistentEntity();
        List<Hotel> forSupervisor = new ArrayList<>();
        for (int i = 0; i < TOTAL_HOTELS; i++) {
            forSupervisor.add(provider.getPersistentEntity(new Visitor<Hotel>() {
                @Override
                public void visit(Hotel entity) {
                    entity.setSupervisor(supervisor);
                }
            }));
        }
        result.add(new Object[]{supervisor, forSupervisor, nonBound});

        HotelUser user = authenticationProvider.hotelUser.getPersistentEntity();
        result.add(new Object[]{user, Collections.emptyList(), provider.getRepository().findAll()});

        Authentication admin = authenticationProvider.getPersistentEntity();
        result.add(new Object[]{admin, provider.getRepository().findAll(), Collections.emptyList()});
        return result.toArray(new Object[result.size()][]);
    }

    @Test(dataProvider = "testSetSupervisorSecurity")
    public void testSetSupervisorSecurity(Authentication authentication, Hotel hotel, ManagerSupervisor supervisor, ResultMatcher matcher) throws Exception {
        loginAs(authentication);
        securedMvc.perform(preparePut(hotel.getId() + "/supervisor/" + supervisor.getId())).andExpect(matcher);
    }

    @DataProvider(name = "testSetSupervisorSecurity")
    public Object[][] getData_testSetSupervisorSecurity() {
        List<Object[]> result = new ArrayList<Object[]>(8);
        Hotel hotel = provider.getPersistentEntity();

        Admin admin = (Admin) authenticationProvider.getPersistentEntity();
        ManagerSupervisor supervisor = authenticationProvider.managerSupervisor.getPersistentEntity();
        Manager manager = authenticationProvider.manager.getPersistentEntity();
        HotelUser user = authenticationProvider.hotelUser.getPersistentEntity();

        result.add(new Object[]{admin, hotel, supervisor, OK});
        result.add(new Object[]{supervisor, hotel, supervisor, FORBIDDEN});
        result.add(new Object[]{manager, hotel, supervisor, FORBIDDEN});
        result.add(new Object[]{user, hotel, supervisor, FORBIDDEN});
        return result.toArray(new Object[result.size()][]);
    }


    @Test(dataProvider = "testSetManagerSecurity")
    public void testSetManagerSecurity(Authentication authentication, Hotel hotel, Manager manager, ResultMatcher matcher) throws Exception {
        loginAs(authentication);
        securedMvc.perform(preparePut(hotel.getId() + "/manager/" + manager.getId())).andExpect(matcher);
    }

    @DataProvider(name = "testSetManagerSecurity")
    public Object[][] getData_testSetManagerSecurity() {
        List<Object[]> result = new ArrayList<Object[]>(8);

        Admin admin = (Admin) authenticationProvider.getPersistentEntity();
        final ManagerSupervisor supervisor = authenticationProvider.managerSupervisor.getPersistentEntity();
        Manager manager = authenticationProvider.manager.getPersistentEntity(new Visitor<Manager>() {
            @Override
            public void visit(Manager entity) {
                entity.setSupervisor(supervisor);
            }
        });
        HotelUser user = authenticationProvider.hotelUser.getPersistentEntity();

        Hotel hotel = provider.getPersistentEntity(new Visitor<Hotel>() {
            @Override
            public void visit(Hotel entity) {
                entity.setSupervisor(supervisor);
            }
        });

        result.add(new Object[]{admin, hotel, manager, OK});
        result.add(new Object[]{supervisor, hotel, manager, OK});
        result.add(new Object[]{manager, hotel, manager, FORBIDDEN});
        result.add(new Object[]{user, hotel, manager, FORBIDDEN});
        return result.toArray(new Object[result.size()][]);
    }

    @Test
    public void testSetSupervisor() {
        Hotel hotel = provider.getPersistentEntity(new Visitor<Hotel>() {
            @Override
            public void visit(Hotel entity) {
                entity.setSupervisor(authenticationProvider.managerSupervisor.getPersistentEntity());
                entity.setManager(authenticationProvider.manager.getPersistentEntity());
            }
        });
        ManagerSupervisor supervisor = authenticationProvider.managerSupervisor.getPersistentEntity();

        controller.setSupervisor(hotel.getId(), supervisor.getId());

        hotel = provider.getRepository().findOne(hotel.getId());

        assertEquals(hotel.getSupervisor(), supervisor);
        assertNull(hotel.getManager());
    }

    @Test
    public void testSetManager() {
        final Hotel hotel = provider.getPersistentEntity(new Visitor<Hotel>() {
            @Override
            public void visit(Hotel entity) {
                entity.setSupervisor(authenticationProvider.managerSupervisor.getPersistentEntity());
                entity.setManager(authenticationProvider.manager.getPersistentEntity());
            }
        });
        Manager manager = authenticationProvider.manager.getPersistentEntity(new Visitor<Manager>() {
            @Override
            public void visit(Manager entity) {
                entity.setSupervisor(hotel.getSupervisor());
            }
        });
        Manager invalidManager = authenticationProvider.manager.getPersistentEntity();

        loginAs(hotel.getSupervisor());

        controller.setManager(hotel.getId(), manager.getId());
        Hotel saved = provider.getRepository().findOne(hotel.getId());
        assertEquals(saved.getManager(), manager);

        try {
            controller.setManager(hotel.getId(), invalidManager.getId());
            Assert.fail();
        } catch (AccessDeniedException e) {
            assertEquals(saved.getManager(), manager);
        }
    }

    @Tested
    private HotelController mockController;
    @Injectable
    private HotelRepository repository;

    @Test
    public void testBlock() {
        final Hotel hotel = provider.getPersistentEntity();
        assertFalse(hotel.isBlocked());

        mockController.block(hotel.getId(), true);
        new Verifications() {{
            repository.setBlocked(hotel.getId(), true);
        }};

        mockController.block(hotel.getId(), false);
        new Verifications() {{
            repository.setBlocked(hotel.getId(), false);
        }};
    }

    @Override
    protected String getUrl() {
        return HotelController.URL + "/";
    }
}
