package com.example.frac.Servicece;

import com.example.frac.Entity.Account;
import com.example.frac.Entity.Article;
import com.example.frac.Entity.Comment;
import com.example.frac.Repository.ICommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final ICommentRepository commentRepository;

    public CommentService(ICommentRepository commentRepository)
    {
        this.commentRepository = commentRepository;
    }

    public List<Comment> GetCommentsByArticle(Article article)
    {
        return commentRepository.findByArticleOrderByCreatedAtDesc(article);
    }

    public Comment CreateComment(Article article, Account author, String content)
    {
        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setAuthor(author);
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public void DeleteComment(Integer commentId)
    {
        commentRepository.deleteById(commentId);
    }
}
