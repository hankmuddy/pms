package com.idgindigo.pms.configuration.security;

import com.idgindigo.pms.domain.Permission;
import com.idgindigo.pms.logins.domain.Admin;
import com.idgindigo.pms.logins.domain.Manager;
import com.idgindigo.pms.logins.domain.ManagerSupervisor;
import com.idgindigo.pms.web.controller.ApprovableController;
import com.idgindigo.pms.web.controller.BaseCrudController;
import com.idgindigo.pms.web.controller.pms.UserController;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.Arrays;

/**
 * @author valentyn_vakatsiienko
 * @since 4/9/14 6:34 PM
 */
public class CrudSecurityManager {

    public static final String ID_SUFFIX = "/" + BaseCrudController.ID;
    public static final String APPROVE_SUFFIX = "/" + ApprovableController.APPROVE_URI;

    private static final Iterable<SecuredEntity> SECURED_ENTITIES = Arrays.asList(
//            new SecuredEntity("accommodation", AccommodationController.URL, true),
//            new SecuredEntity("adult", AdultController.URL)
            new SecuredEntity("user", UserController.URL)
    );

    public static ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry secureCruds(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
        for (SecuredEntity entity : SECURED_ENTITIES) {
            String controllerUrl = entity.getControllerUrl();
            String name = entity.getName();

            registry = registry.antMatchers(HttpMethod.POST, controllerUrl).hasAnyAuthority(getAuthorities(name, "create"))
                    .antMatchers(HttpMethod.GET, controllerUrl + ID_SUFFIX).hasAnyAuthority(getAuthorities(name, "view"))
                    .antMatchers(HttpMethod.GET, controllerUrl).hasAnyAuthority(getAuthorities(name, "list"))
                    .antMatchers(HttpMethod.PUT, controllerUrl + ID_SUFFIX).hasAnyAuthority(getAuthorities(name, "update"))
                    .antMatchers(HttpMethod.DELETE, controllerUrl + ID_SUFFIX).hasAnyAuthority(getAuthorities(name, "delete"));

            if (entity.isApprovable()) {
                registry = registry.antMatchers(HttpMethod.PUT, controllerUrl + APPROVE_SUFFIX).hasAnyAuthority(getAuthorities(name, "approve"));
            }
        }
        return registry;
    }

    private static String[] getAuthorities(String name, String action) {
        return new String[]{
                Admin.ADMIN, ManagerSupervisor.MANAGER_SUPERVISOR, Manager.MANAGER, Permission.valueOf(name.toUpperCase() + "_" + action.toUpperCase()).getAuthority()};
    }

    @Getter
    @AllArgsConstructor
    private static class SecuredEntity {
        String name;
        String controllerUrl;
        boolean approvable;

        public SecuredEntity(String name, String controllerUrl) {
            this.name = name;
            this.controllerUrl = controllerUrl;
        }
    }
}
