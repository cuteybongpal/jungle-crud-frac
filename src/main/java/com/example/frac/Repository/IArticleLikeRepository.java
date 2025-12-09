package com.example.frac.Repository;

import com.example.frac.Entity.Account;
import com.example.frac.Entity.Article;
import com.example.frac.Entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IArticleLikeRepository extends JpaRepository<ArticleLike, Integer>
{
    int countByArticle(Article article);
    Optional<ArticleLike> findByArticleAndAccount(Article article, Account account);
    boolean existsByArticleAndAccount(Article article, Account account);
    void deleteByArticle(Article article);
}
