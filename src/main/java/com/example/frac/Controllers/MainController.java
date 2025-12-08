package com.example.frac.Controllers;

import com.example.frac.Entity.Article;
import com.example.frac.Servicece.ArticleService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class MainController
{
    private final ArticleService articleService;
    @GetMapping("/")
    public String Index(Model model)
    {
        List<Article> articles = articleService.GetAllArticle();
        model.addAttribute("articles", articles);
        return "index.html";
    }

    public MainController(ArticleService _articleService)
    {
        articleService = _articleService;
    }
}
