package com.idgindigo.pms.utils;

import com.idgindigo.pms.logins.domain.Admin;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.logins.repository.HotelUserRepository;
import com.idgindigo.pms.logins.repository.ManagerRepository;
import com.idgindigo.pms.logins.repository.ManagerSupervisorRepository;
import com.idgindigo.pms.repository.BaseRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author valentyn_vakatsiienko
 * @since 11/7/13 11:47 AM
 */
@Component
public class AuthenticationProvider extends EntityProvider<Authentication> {
    @Inject
    @Named("authenticationRepository")
    private AuthenticationRepository repository;
    @Inject
    private HotelProvider hotelProvider;
    @Inject
    private HotelUserRepository hotelUserRepository;
    @Inject
    private ManagerRepository managerRepository;
    @Inject
    private ManagerSupervisorRepository managerSupervisorRepository;

    public HotelUserProvider hotelUser = new HotelUserProvider();
    public ManagerProvider manager = new ManagerProvider();
    public ManagerSupervisorProvider managerSupervisor = new ManagerSupervisorProvider();

    public class HotelUserProvider extends EntityProvider<HotelUser> {
        @Override
        public HotelUser createAndFill() {
            HotelUser authentication = new HotelUser();
            fillAuthentication(authentication);
            return authentication;
        }

        @Override
        public BaseRepository<HotelUser> getRepository() {
            return hotelUserRepository;
        }
    }

    public class ManagerProvider extends EntityProvider<Manager> {
        @Override
        public Manager createAndFill() {
            Manager authentication = new Manager();
            fillAuthentication(authentication);
            authentication.setSupervisor(managerSupervisor.getPersistentEntity());
            return authentication;
        }

        @Override
        public BaseRepository<Manager> getRepository() {
            return managerRepository;
        }
    }

    public class ManagerSupervisorProvider extends EntityProvider<ManagerSupervisor> {
        @Override
        public ManagerSupervisor createAndFill() {
            ManagerSupervisor authentication = new ManagerSupervisor();
            fillAuthentication(authentication);
            return authentication;
        }

        @Override
        public BaseRepository<ManagerSupervisor> getRepository() {
            return managerSupervisorRepository;
        }
    }

    @Override
    public Authentication createAndFill() {
        Authentication authentication = new Admin();
        authentication.setHotel(hotelProvider.getPersistentEntity());
        fillAuthentication(authentication);
        return authentication;
    }

    private void fillAuthentication(Authentication authentication) {
        authentication.setHotel(hotelProvider.getPersistentEntity());
        authentication.setUsername(randomString());
        authentication.setPassword(randomString());
    }

    @Override
    public BaseRepository<Authentication> getRepository() {
        return repository;
    }
}
