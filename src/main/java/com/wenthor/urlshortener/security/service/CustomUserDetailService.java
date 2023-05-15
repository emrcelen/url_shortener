package com.wenthor.urlshortener.security.service;



import com.wenthor.urlshortener.model.Account;
import com.wenthor.urlshortener.repository.AccountRepository;
import com.wenthor.urlshortener.security.model.CustomUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository repository;

    public CustomUserDetailService(AccountRepository repository) {
        this.repository = repository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = repository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException(String.format("There is no user register in the username (%d) you requested.",username)));
        return new CustomUserDetail(account);
    }
}
