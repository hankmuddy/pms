package com.idgindigo.pms.security.service;

import com.idgindigo.pms.logins.domain.Admin;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.HotelUser;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.security.permission.Decider;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author valentyn_vakatsiienko
 * @since 4/22/14 1:24 PM
 */
@Service
public class AuthenticationCreateDeciderService extends ConcreteDeciderService {

    @PostConstruct
    public void postConstruct() throws Exception {
        put(HotelUser.USER, Decider.FALSE);
        put(Admin.ADMIN, Decider.TRUE);
        put(ManagerSupervisor.MANAGER_SUPERVISOR, new Decider() {
            @Override
            public boolean decide(Object target) {
                Authentication authentication = (Authentication) target;
                return authentication.getUserType().equals(Manager.MANAGER);
            }
        });
        put(Manager.MANAGER, Decider.FALSE);
    }
}
