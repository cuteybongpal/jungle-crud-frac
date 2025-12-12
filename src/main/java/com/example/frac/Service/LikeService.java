package com.example.frac.Servicece;

import com.example.frac.Entity.Account;
import com.example.frac.Entity.Article;
import com.example.frac.Entity.ArticleLike;
import com.example.frac.Repository.IArticleLikeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {
    private final IArticleLikeRepository likeRepository;

    public LikeService(IArticleLikeRepository likeRepository)
    {
        this.likeRepository = likeRepository;
    }

    public int GetLikeCount(Article article)
    {
        return likeRepository.countByArticle(article);
    }

    public boolean HasUserLiked(Article article, Account account)
    {
        return likeRepository.existsByArticleAndAccount(article, account);
    }

    public boolean ToggleLike(Article article, Account account)
    {
        Optional<ArticleLike> existingLike = likeRepository.findByArticleAndAccount(article, account);

        if (existingLike.isPresent())
        {
            likeRepository.delete(existingLike.get());
            return false;
        }
        else
        {
            ArticleLike like = new ArticleLike();
            like.setArticle(article);
            like.setAccount(account);
            likeRepository.save(like);
            return true;
        }
    }
}
