package com.colorpl.comment.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.review.domain.Review;
import com.colorpl.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReviewService reviewService;

    public Comment findByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public List<Comment> findByReviewId(Integer reviewId) {
        return CommentRepository.findByReviewId(reviewId);
    }

    public Comment save(Integer reviewId, Comment comment) {
        Review review = reviewService.findById(reviewId);
        if (review == null) {
            throw new NoSuchElementException("Review with ID " + reviewId + " does not exist.");
        }
        // save comment's reviewid with given review
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new NoSuchElementException("Comment with ID " + commentId + " does not exist.");
        }
        commentRepository.deleteById(commentId);
    }

    public Comment updateComment(Long commentId, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() ->
                new NoSuchElementException("Comment with ID " + commentId + " does not exist."));

        existingComment.setContent(updatedComment.getContent());
        existingComment.setMember(updatedComment.getMember());

        // Updating the review if necessary
        if (updatedComment.getReview() != null) {
            Review review = reviewService.findById(updatedComment.getReview().getId());
            if (review != null) {
                existingComment.setReview(review);
            } else {
                throw new NoSuchElementException("Review with ID " + updatedComment.getReview().getId() + " does not exist.");
            }
        }

        return commentRepository.save(existingComment);
    }
}
