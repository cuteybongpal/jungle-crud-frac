package com.example.frac.Servicece;

import com.example.frac.Entity.Article;
import com.example.frac.Repository.IArticleRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    private IArticleRepository repo;

    public List<Article> getArticle(Example<Article> example)
    {
        List<Article> article_list = repo.findAll(example);
        return article_list;
    }
    public ArticleService(IArticleRepository _repo)
    {
        repo = _repo;
    }
}
