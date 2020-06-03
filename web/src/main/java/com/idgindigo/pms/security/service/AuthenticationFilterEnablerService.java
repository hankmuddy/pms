package com.idgindigo.pms.security.service;

import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author valentyn_vakatsiienko
 * @since 4/22/14 2:45 PM
 */
@Service
public class AuthenticationFilterEnablerService extends GenericFilterEnablerService {

    @PostConstruct
    public void postConstruct() {
        put(ManagerSupervisor.MANAGER_SUPERVISOR, Authentication.FILTER_BY_SUPERVISOR);
        put(Manager.MANAGER, Authentication.FILTER_BY_MANAGER);
    }
}