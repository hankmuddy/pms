package com.idgindigo.pms.security;

import com.idgindigo.pms.channel.wubook.WubookAccount;
import com.idgindigo.pms.domain.Permission;
import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Hotel;
import com.idgindigo.pms.logins.domain.HotelUser;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeZone;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author vomel
 * @since 29.10.13 13:22
 */
public final class SecurityUtils {
    public static final Collection<Permission> NON_ADMIN_AUTHORITIES = new ArrayList<Permission>() {{
        for (Permission permission : Permission.values()) {
            if (!permission.isAdministrative()) {
                add(permission);
            }
        }
    }};

    private SecurityUtils() {
    }

    public static CustomUserDetails getUserDetails() {
        return isRecognizedUser() ? (CustomUserDetails) getAuthentication().getPrincipal() : null;
    }

    public static User getCurrentUser() {
        return getUserDetails() != null ? getUserDetails().getUser() : null;
    }

    public static Hotel getHotel() {
        return getUserDetails() != null ? getUserDetails().getAuthentication().getHotel() : null;
    }

    public static DateTimeZone getTimeZone() {
        return getHotel() != null ? DateTimeZone.forID(getHotel().getInfo().getTimeZone()) : DateTimeZone.forID("UTC");
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static boolean isAuthenticationPresent() {
        return getAuthentication() != null;
    }

    public static boolean isAnonymous() {
        return getAuthentication() instanceof AnonymousAuthenticationToken;
    }

    public static boolean isRecognizedUser() {
        return isAuthenticationPresent() && !isAnonymous() && getAuthentication().getPrincipal() instanceof CustomUserDetails;
    }

    public static boolean isTenantUser() {
        return isRecognizedUser() && getUserDetails().getAuthentication().getUserType().equals(HotelUser.USER);
    }

    public static void authenticateUser(com.idgindigo.pms.logins.domain.Authentication userLogin, User user) {
        CustomUserDetails principal = new CustomUserDetails(userLogin);
        principal.setUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, userLogin.getPassword(), user.getRole().getPermissions());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void authenticateNonTenantUser(com.idgindigo.pms.logins.domain.Authentication userLogin) {
        CustomUserDetails principal = new CustomUserDetails(userLogin);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, userLogin.getPassword(), Collections.<SimpleGrantedAuthority>singletonList(new SimpleGrantedAuthority(userLogin.getUserType())));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static boolean isInRole(String role) {
        return isTenantUser() && getUserDetails().getUser().getRole().getName().equals(role);
    }

    public static String getCurrentTenantId() {
        return getHotel() != null ? getHotel().getTenantId() : null;
    }

    public static WubookAccount getWubookAccount() {
        return getWubookAccount(getHotel());
    }

    public static WubookAccount getWubookAccount(Hotel hotel) {
        return isWubookConfigured(hotel) ? new WubookAccount(hotel.getWuName(), hotel.getWuPass(), hotel.getLcode()) : null;
    }

    public static boolean isWubookConfigured() {
        return isWubookConfigured(getHotel());
    }

    public static boolean isWubookConfigured(Hotel hotel) {
        return hotel != null && isWubookCredentialsPresent(hotel);
    }

    public static boolean isWubookCredentialsPresent(Hotel hotel) {
        return StringUtils.isNotBlank(hotel.getLcode()) && StringUtils.isNotBlank(hotel.getWuName()) && StringUtils.isNotBlank(hotel.getWuPass());
    }

    public static boolean isWubookCredentialsPresent() {
        return getHotel() != null && isWubookCredentialsPresent(getHotel());
    }

    public static String getUsername() {
        return isRecognizedUser() ? getUserDetails().getUsername() : null;
    }

    public static boolean isExtranet() {
        return getHotel() != null && getHotel().isExtranet();
    }
}