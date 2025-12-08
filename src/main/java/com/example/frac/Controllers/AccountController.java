package com.example.frac.Controllers;

import com.example.frac.Entity.Account;
import com.example.frac.Servicece.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController
{
    private final AccountService accountService;

    @PostMapping("account/signup")
    public String SignUp(
            @RequestParam String username,
            @RequestParam String password
    )
    {
        return "";
    }
    @GetMapping("/account/signup")
    public String SignUp()
    {
        return "signup";
    }



    @GetMapping("/account/signin")
    public String SignIn()
    {
        return "signup";
    }

    public AccountController(AccountService _accountService)
    {
        accountService = _accountService;
    }
}
