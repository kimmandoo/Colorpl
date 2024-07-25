package com.colorpl.comment.controller;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.comment.service.CommentService;
import com.colorpl.review.domain.Review;
import com.colorpl.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews/{reviewId}/comments")
public class CommentController {

//    private final CommentService commentService;
//    private final CommentRepository commentRepository;
//
//
//
//    @Autowired
//    public CommentController(CommentService commentService, CommentRepository commentRepository) {
//        this.commentService = commentService;
//        this.commentRepository = commentRepository;
//    }
//
//    @Autowired
//    private ReviewService reviewService;
//
//    public List<Comment> getCommentsByReviewId(Integer reviewId) {
//        return commentRepository.findByReviewId(reviewId);
//    }
//
////    Review review = Review.builder().build();
////        model.addAttribute("review", review);
////        return "review_form";
//
//
//
//    @GetMapping("/create")
//    public String showCreateCommentForm(@PathVariable Long reviewId, Model model) {
//        Comment comment = Comment.builder().build();
//        model.addAttribute("comment", comment);
//        model.addAttribute("reviewId", reviewId);
//        return "comment_form";
//    }
//
//
//
//    @PostMapping
//    public String createComment(@PathVariable Integer reviewId, @ModelAttribute Comment comment) {
//        Review review = reviewService.findById(reviewId); // Fetch the review
//
//        Comment newComment = Comment.builder()
//                .review(review)
//                .content(comment.getContent())
//                .build();
//
//        commentService.save(reviewId, newComment);
//        return "redirect:/reviews/" + reviewId + "/comments";
//    }
//
//    @GetMapping("/{commentId}/update")
//    public String showUpdateCommentForm(@PathVariable Long reviewId, @PathVariable Integer commentId, Model model) {
//        Comment comment = commentService.findByCommentId(commentId);
//        if (comment == null) {
//            return "404";  // Or handle not found case appropriately
//        }
//        model.addAttribute("comment", comment);
//        model.addAttribute("reviewId", reviewId);
//        return "comment_update";
//    }
//
//    @PostMapping("/{commentId}")
//    public String updateComment(@PathVariable Long reviewId, @PathVariable Long commentId, @ModelAttribute Comment comment) {
//        comment.setId(commentId); // Ensure ID is set
//        comment.setReview(new Review(reviewId)); // Assuming setter for review in Comment
//        commentService.saveComment(comment);
//        return "redirect:/reviews/" + reviewId + "/comments";
//    }
//
//    @GetMapping("/{commentId}/delete")
//    public String deleteComment(@PathVariable Long reviewId, @PathVariable Long commentId) {
//        commentService.deleteById(reviewId, commentId);
//        return "redirect:/reviews/" + reviewId + "/comments";
//    }
}