package com.soulcode.Servicos.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class UserSecurityDetail implements UserDetails {
    private String login;
    private String password;

    private boolean isEnabled;

    public UserSecurityDetail(String login, String password, boolean isEnabled) {
        this.login = login;
        this.password = password;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() { // a conta não expirou
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // a conta não bloqueou
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // as credenciais não expiraram
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
