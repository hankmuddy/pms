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
 * @since 5/19/14 2:57 PM
 */
@Service
public class AuthenticationChangeHotelDeciderService extends ConcreteDeciderService {
    @Inject
    private HotelRepository hotelRepository;

    @PostConstruct
    public void postConstruct() throws Exception {
        put(HotelUser.USER, Decider.FALSE);
        put(Admin.ADMIN, Decider.TRUE);
        put(ManagerSupervisor.MANAGER_SUPERVISOR, new Decider() {
            @Override
            public boolean decide(Object target) {
                Hotel hotel = hotelRepository.findByTenantId((String) target);
                return hotel.getSupervisor().equals(SecurityUtils.getUserDetails().getAuthentication());
            }
        });
        put(Manager.MANAGER, new Decider() {
            @Override
            public boolean decide(Object target) {
                Hotel hotel = hotelRepository.findByTenantId((String) target);
                return hotel.getManager().equals(SecurityUtils.getUserDetails().getAuthentication());
            }
        });
    }
}