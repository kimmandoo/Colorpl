package com.colorpl.comment.repository;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentDTO> findByReviewId(Long reviewId);

}