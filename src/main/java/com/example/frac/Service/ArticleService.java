package com.example.frac.Servicece;

import com.example.frac.Entity.Account;
import com.example.frac.Entity.Article;
import com.example.frac.Repository.IArticleLikeRepository;
import com.example.frac.Repository.IArticleRepository;
import com.example.frac.Repository.ICommentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {
    private final IArticleRepository repo;
    private final IArticleLikeRepository likeRepo;
    private final ICommentRepository commentRepo;

    public ArticleService(IArticleRepository _repo,
                          IArticleLikeRepository likeRepo,
                          ICommentRepository commentRepo)
    {
        this.repo = _repo;
        this.likeRepo = likeRepo;
        this.commentRepo = commentRepo;
    }

    public List<Article> GetAllArticle()
    {
        List<Article> article_list = repo.findAll();
        return article_list;
    }

    public Article CreateArticle(String title, String content, Account author)
    {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(author);
        article.setViews(0);
        return repo.save(article);
    }

    public Article GetArticleById(Integer id)
    {
        return repo.findById(id).orElse(null);
    }

    public void IncreaseViews(Article article)
    {
        article.setViews(article.getViews() + 1);
        repo.save(article);
    }

    @Transactional
    public void DeleteArticle(Integer id)
    {
        Article article = repo.findById(id).orElse(null);
        if (article != null)
        {
            // 연관된 좋아요, 댓글 먼저 삭제
            likeRepo.deleteByArticle(article);
            commentRepo.deleteByArticle(article);
            repo.deleteById(id);
        }
    }
}
