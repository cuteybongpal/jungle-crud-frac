package com.example.frac.Controllers;

import com.example.frac.Entity.Account;
import com.example.frac.Entity.Article;
import com.example.frac.Entity.Comment;
import com.example.frac.Repository.IAccountRepository;
import com.example.frac.Servicece.ArticleService;
import com.example.frac.Servicece.CommentService;
import com.example.frac.Servicece.LikeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ArticleController
{
    private final ArticleService articleService;
    private final IAccountRepository accountRepository;
    private final LikeService likeService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService,
                             IAccountRepository accountRepository,
                             LikeService likeService,
                             CommentService commentService)
    {
        this.articleService = articleService;
        this.accountRepository = accountRepository;
        this.likeService = likeService;
        this.commentService = commentService;
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

    @GetMapping("/articles/{id}")
    @SuppressWarnings("unchecked")
    public String viewArticle(@PathVariable Integer id,
                              Model model,
                              @AuthenticationPrincipal UserDetails userDetails,
                              HttpSession session)
    {
        Article article = articleService.GetArticleById(id);
        if (article == null)
        {
            return "redirect:/";
        }

        // 세션 기반 조회수 중복 방지
        Set<Integer> viewedArticles = (Set<Integer>) session.getAttribute("viewedArticles");
        if (viewedArticles == null)
        {
            viewedArticles = new HashSet<>();
        }
        if (!viewedArticles.contains(id))
        {
            articleService.IncreaseViews(article);
            viewedArticles.add(id);
            session.setAttribute("viewedArticles", viewedArticles);
        }

        // 좋아요 정보
        int likeCount = likeService.GetLikeCount(article);
        boolean hasLiked = false;
        boolean isAuthor = false;
        if (userDetails != null)
        {
            Account account = accountRepository.findByUsername(userDetails.getUsername());
            hasLiked = likeService.HasUserLiked(article, account);
            isAuthor = article.getAuthor().getUserId().equals(account.getUserId());
        }

        // 댓글 목록
        List<Comment> comments = commentService.GetCommentsByArticle(article);

        model.addAttribute("article", article);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("hasLiked", hasLiked);
        model.addAttribute("isAuthor", isAuthor);
        model.addAttribute("comments", comments);

        return "post-info";
    }

    @PostMapping("/articles/{id}/like")
    public String toggleLike(@PathVariable Integer id,
                             @AuthenticationPrincipal UserDetails userDetails)
    {
        Article article = articleService.GetArticleById(id);
        if (article == null || userDetails == null)
        {
            return "redirect:/articles/" + id;
        }

        Account account = accountRepository.findByUsername(userDetails.getUsername());
        likeService.ToggleLike(article, account);

        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles/{id}/comment")
    public String addComment(@PathVariable Integer id,
                             @RequestParam String content,
                             @AuthenticationPrincipal UserDetails userDetails)
    {
        Article article = articleService.GetArticleById(id);
        if (article == null || userDetails == null)
        {
            return "redirect:/articles/" + id;
        }

        Account account = accountRepository.findByUsername(userDetails.getUsername());
        commentService.CreateComment(article, account, content);

        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles/{id}/delete")
    public String deleteArticle(@PathVariable Integer id,
                                @AuthenticationPrincipal UserDetails userDetails)
    {
        Article article = articleService.GetArticleById(id);
        if (article == null || userDetails == null)
        {
            return "redirect:/";
        }

        Account account = accountRepository.findByUsername(userDetails.getUsername());

        if (!article.getAuthor().getUserId().equals(account.getUserId()))
        {
            return "redirect:/articles/" + id;
        }

        articleService.DeleteArticle(id);
        return "redirect:/";
    }
}
