package com.idgindigo.pms.security.permission;

import com.idgindigo.pms.domain.Permission;
import com.idgindigo.pms.security.SecurityUtils;
import com.idgindigo.pms.security.service.DeciderService;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import javax.inject.Inject;
import java.io.Serializable;

/**
 * @author valentyn_vakatsiienko
 * @since 4/8/14 1:01 PM
 */
public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Inject
    private DeciderService deciderService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        Permission perm = Permission.valueOf(permission.toString().toUpperCase());
        if (!perm.isAdministrative()) {
            return evaluateNonAdmin(perm, targetDomainObject);
        } else {
            return evaluateAdmin(perm, targetDomainObject);
        }
    }

    private boolean evaluateAdmin(Permission perm, Object target) {
        if (SecurityUtils.isTenantUser()) {
            return false;
        } else if (SecurityUtils.isRecognizedUser()) {
            return deciderService.getDecider(perm).decide(target);
        }
        return false;
    }

    public boolean evaluateNonAdmin(Permission perm, Object target) {
        if (SecurityUtils.isTenantUser()) {
            return SecurityUtils.getAuthentication().getAuthorities().contains(perm) && deciderService.getDecider(perm).decide(target);
        } else if (SecurityUtils.isRecognizedUser()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new NotImplementedException("Permission evaluation with target id is not yet implemented");
    }
}