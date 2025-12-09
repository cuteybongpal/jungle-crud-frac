package com.example.frac.Repository;

import com.example.frac.Entity.Article;
import com.example.frac.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, Integer>
{
    List<Comment> findByArticleOrderByCreatedAtDesc(Article article);
    void deleteByArticle(Article article);
}
