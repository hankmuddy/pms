package com.idgindigo.pms.security.service;

import com.idgindigo.pms.logins.domain.Admin;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.logins.repository.AuthenticationRepository;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.security.permission.Decider;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author valentyn_vakatsiienko
 * @since 4/22/14 2:20 PM
 */
@Service
public class AuthenticationViewDeciderService extends ConcreteDeciderService {

    @Inject
    private AuthenticationRepository repository;

    @PostConstruct
    public void postConstruct() throws Exception {
        put(HotelUser.USER, Decider.FALSE);
        put(Admin.ADMIN, Decider.TRUE);
        put(ManagerSupervisor.MANAGER_SUPERVISOR, new Decider() {
            @Override
            public boolean decide(Object target) {
                Authentication authentication = repository.findOne((Long) target);
                if (authentication.getUserType().equals(Manager.MANAGER)) {
                    return ((Manager) authentication).getSupervisor().equals(SecurityUtils.getUserDetails().getAuthentication());
                } else if (authentication.getUserType().equals(HotelUser.USER)) {
                    ManagerSupervisor supervisor = authentication.getHotel().getSupervisor();
                    return supervisor != null && supervisor.equals(SecurityUtils.getUserDetails().getAuthentication());
                } else {
                    return false;
                }
            }
        });
        put(Manager.MANAGER, new Decider() {
            @Override
            public boolean decide(Object target) {
                Authentication authentication = repository.findOne((Long) target);
                return authentication.getUserType().equals(HotelUser.USER)
                        && authentication.getHotel().getManager() != null
                        && authentication.getHotel().getManager().equals(SecurityUtils.getUserDetails().getAuthentication());
            }
        });
    }
}