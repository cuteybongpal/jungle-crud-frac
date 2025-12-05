package com.example.frac.Servicece;

import com.example.frac.Entity.Account;
import com.example.frac.Repository.IAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService
{
    private IAccountRepository repo = null;
    private PasswordEncoder encoder = null;

    public boolean Signup(String username, String password)
    {
        Account newAccount = new Account();
        newAccount.Username = username;
        newAccount.Password = encoder.encode(password);
        try
        {
            repo.save(newAccount);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public AccountService(IAccountRepository _repo, PasswordEncoder _encoder)
    {
        repo = _repo;
        encoder = _encoder;
    }


}
