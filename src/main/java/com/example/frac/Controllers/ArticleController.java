package com.example.frac.Controllers;

import com.example.frac.Entity.Account;
import com.example.frac.Repository.IAccountRepository;
import com.example.frac.Servicece.ArticleService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArticleController
{
    private final ArticleService articleService;
    private final IAccountRepository accountRepository;

    public ArticleController(ArticleService articleService, IAccountRepository accountRepository)
    {
        this.articleService = articleService;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/articles/new")
    public String newPostPage()
    {
        return "newpost";
    }

    @PostMapping("/articles/new")
    public String createPost(@RequestParam String title,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails)
    {
        Account author = accountRepository.findByUsername(userDetails.getUsername());
        articleService.CreateArticle(title, content, author);
        return "redirect:/";
    }
}
