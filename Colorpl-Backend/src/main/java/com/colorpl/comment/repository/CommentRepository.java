package com.colorpl.comment.repository;

import com.colorpl.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    static List<Comment> findByReviewId(Integer reviewId);
}