package com.colorpl.comment.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment save(Comment comment) {
        System.out.println(comment);
        return commentRepository.save(comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
