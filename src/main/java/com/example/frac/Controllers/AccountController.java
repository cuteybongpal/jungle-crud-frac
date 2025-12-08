package com.example.frac.Controllers;

import com.example.frac.Form.SignUpForm;
import com.example.frac.Servicece.AccountService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController
{
    private final AccountService accountService;

    @PostMapping("account/signup")
    public String SignUp(@Valid @ModelAttribute("signupForm") SignUpForm form, BindingResult bindingResult)
    {
        if (bindingResult.hasErrors())
        {
            return "signup";
        }
        if (!accountService.Signup(form.username, form.password))
        {
            bindingResult.rejectValue("username", "이미 존재하는 아이디입니다.");
            return "signup";
        }
        return "redirect:/account/signin";
    }
    @GetMapping("/account/signup")
    public String SignUp(Model model)
    {
        model.addAttribute("signupForm", new SignUpForm());
        return "signup";
    }



    @GetMapping("/account/signin")
    public String SignIn()
    {
        return "signin";
    }

    public AccountController(AccountService _accountService)
    {
        accountService = _accountService;
    }
}
