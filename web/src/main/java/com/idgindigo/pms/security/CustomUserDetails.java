package com.idgindigo.pms.security;

import com.idgindigo.pms.domain.User;
import com.idgindigo.pms.logins.domain.Authentication;
import com.idgindigo.pms.utils.SmartToStringBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author vomel
 * @since 29.10.13 13:22
 */
@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private Authentication authentication;
    private User user;
    private Collection<GrantedAuthority> authorities = new ArrayList<>();

    public CustomUserDetails(Authentication authentication) {
        Assert.notNull(authentication);
        this.authentication = authentication;
        if (SecurityUtils.isTenantUser()) {
            this.authorities.addAll(SecurityUtils.getCurrentUser().getRole().getPermissions());
        } else {
            this.authorities.addAll(getAdminUserAuthorities(authentication));
        }
    }

    private Collection<GrantedAuthority> getAdminUserAuthorities(Authentication authentication) {
        Collection<GrantedAuthority> res = new ArrayList<>();
        res.addAll(SecurityUtils.NON_ADMIN_AUTHORITIES);
        res.add(new SimpleGrantedAuthority(authentication.getUserType()));
        return res;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return authentication.getPassword();
    }

    @Override
    public String getUsername() {
        return authentication.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return new SmartToStringBuilder(this)
                .append("authentication", authentication)
                .append("user", user)
                .toString();
    }
}
