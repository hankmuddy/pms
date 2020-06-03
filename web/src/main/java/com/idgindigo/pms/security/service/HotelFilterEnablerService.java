package com.idgindigo.pms.security.service;

import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author valentyn_vakatsiienko
 * @since 4/22/14 6:03 PM
 */
@Service
public class HotelFilterEnablerService extends GenericFilterEnablerService {

    @PostConstruct
    public void postConstruct() {
        put(ManagerSupervisor.MANAGER_SUPERVISOR, Hotel.FILTER_BY_SUPERVISOR);
        put(Manager.MANAGER, Hotel.FILTER_BY_MANAGER);
    }
}
