package com.example.frac.Servicece;

import com.example.frac.Entity.Account;
import com.example.frac.Repository.IAccountRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {
    private IAccountRepository repo = null;
    private PasswordEncoder encoder = null;

    public boolean Signup(String username, String password)
    {
        Account newAccount = new Account();
        newAccount.setUsername(username);
        newAccount.setPassword(encoder.encode(password));
        if (repo.existsByUsername(username))
            return false;
        repo.save(newAccount);
        return true;
    }

    public AccountService(IAccountRepository _repo, PasswordEncoder _encoder)
    {
        repo = _repo;
        encoder = _encoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = repo.findByUsername(username);
        UserDetails detail = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of();
            }

            @Override
            public @Nullable String getPassword() {
                return account.getPassword();
            }

            @Override
            public String getUsername() {
                return account.getUsername();
            }
        };
        return detail;
    }
}
