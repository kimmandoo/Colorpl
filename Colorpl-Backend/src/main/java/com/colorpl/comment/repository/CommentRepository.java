package com.colorpl.comment.repository;

import com.colorpl.comment.domain.Comment;
import com.colorpl.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByReviewId(Long reviewId, Pageable pageable);
}