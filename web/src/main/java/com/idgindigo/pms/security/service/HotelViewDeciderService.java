package com.idgindigo.pms.security.service;

import com.idgindigo.pms.logins.domain.Admin;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.logins.repository.HotelRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.security.permission.Decider;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 4/30/14 2:25 PM
 */
@Service
public class HotelViewDeciderService extends ConcreteDeciderService {

    @Inject
    private HotelRepository repository;

    @PostConstruct
    public void postConstruct() {
        put(HotelUser.USER, Decider.FALSE);
        put(Admin.ADMIN, Decider.TRUE);
        put(ManagerSupervisor.MANAGER_SUPERVISOR, new Decider() {
            @Override
            public boolean decide(Object target) {
                Hotel hotel = repository.findOne((Long) target);
                ManagerSupervisor supervisor = hotel.getSupervisor();
                return supervisor != null && supervisor.equals(SecurityUtils.getUserDetails().getAuthentication());
            }
        });
        put(Manager.MANAGER, new Decider() {
            @Override
            public boolean decide(Object target) {
                Hotel hotel = repository.findOne((Long) target);
                Manager manager = hotel.getManager();
                return manager != null && manager.equals(SecurityUtils.getUserDetails().getAuthentication());
            }
        });
    }

}
