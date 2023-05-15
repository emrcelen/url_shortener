package com.wenthor.urlshortener.security.model;


import com.wenthor.urlshortener.model.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CustomUserDetail implements UserDetails {

    private String username;
    private String password;
    private List<GrantedAuthority> roles;

    public CustomUserDetail(Account account){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(account.getRole().getName());
        this.username = account.getEmail();
        this.password = account.getPassword();
        this.roles = Arrays.asList(grantedAuthority);
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
}
