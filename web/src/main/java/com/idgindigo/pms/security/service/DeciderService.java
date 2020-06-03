package com.idgindigo.pms.security.service;

import com.idgindigo.pms.domain.Permission;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.security.permission.Decider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author valentyn_vakatsiienko
 * @since 4/8/14 1:45 PM
 */
@Service
public class DeciderService implements InitializingBean {
    private static final Decider defaultDecider = new Decider() {
        @Override
        public boolean decide(Object target) {
            return true;
        }
    };

    private Map<Permission, Decider> permToDecider = new HashMap<>();

    @Inject
    private AuthenticationCreateDeciderService authenticationCreateDeciderService;
    @Inject
    private AuthenticationViewDeciderService authenticationViewDeciderService;
    @Inject
    private AuthenticationChangeHotelDeciderService authenticationChangeHotelDeciderService;
    @Inject
    private HotelViewDeciderService hotelViewDeciderService;

    @Inject
    private AuthenticationFilterEnablerService authenticationFilterEnablerService;
    @Inject
    private HotelFilterEnablerService hotelFilterEnablerService;

    public Decider getDecider(Permission permission) {
        return permToDecider.containsKey(permission) ? permToDecider.get(permission) : defaultDecider;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //Authentication
        permToDecider.put(Permission.AUTHENTICATION_CHANGE_PASSWORD, new Decider() {
            @Override
            public boolean decide(Object target) {
                return SecurityUtils.getUserDetails().getAuthentication().getId().equals(target);
            }
        });
        permToDecider.put(Permission.AUTHENTICATION_CHANGE_HOTEL, new Decider() {
            @Override
            public boolean decide(Object target) {
                return authenticationChangeHotelDeciderService.getDecider().decide(target);
            }
        });
        permToDecider.put(Permission.AUTHENTICATION_CREATE, new Decider() {
            @Override
            public boolean decide(Object target) {
                return authenticationCreateDeciderService.getDecider().decide(target);
            }
        });
        permToDecider.put(Permission.AUTHENTICATION_VIEW, new Decider() {
            @Override
            public boolean decide(Object target) {
                return authenticationViewDeciderService.getDecider().decide(target);
            }
        });
        permToDecider.put(Permission.AUTHENTICATION_LIST, getAdminFilteringDecider(authenticationFilterEnablerService));

        //Hotel
        permToDecider.put(Permission.HOTEL_CREATE, new Decider() {
            @Override
            public boolean decide(Object target) {
                return !SecurityUtils.isTenantUser();
            }
        });
        permToDecider.put(Permission.HOTEL_LIST, getAdminFilteringDecider(hotelFilterEnablerService));
        permToDecider.put(Permission.HOTEL_VIEW, new Decider() {
            @Override
            public boolean decide(Object target) {
                return hotelViewDeciderService.getDecider().decide(target);
            }
        });
    }

    private Decider getAdminFilteringDecider(final GenericFilterEnablerService filterEnablerService) {
        return new Decider() {
            @Override
            public boolean decide(Object target) {
                if (SecurityUtils.isTenantUser()) {
                    return false;
                }
                filterEnablerService.enableFilter(SecurityUtils.getUserDetails().getAuthentication().getUserType());
                return true;
            }
        };
    }
}