package com.yenyasoft.email_plus_otp.services;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Author: Subash
 * Created: 8/25/2026
 * File: CustomUserDetails.java
 */
public class CustomUserDetails implements UserDetails {

    private final com.yenyasoft.email_plus_otp.models.User user;

    public CustomUserDetails(com.yenyasoft.email_plus_otp.models.User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

    public com.yenyasoft.email_plus_otp.models.User getUser() {
        return user;
    }
}
