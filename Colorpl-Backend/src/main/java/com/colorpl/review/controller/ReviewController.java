package com.colorpl.review.controller;

import com.colorpl.review.domain.Review;
import com.colorpl.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

//    @GetMapping
//    public ResponseEntity<List<Review>> getAllReviews() {
//        List<Review> reviews = reviewService.findAll();
//        return new ResponseEntity<>(reviews, HttpStatus.OK);
//    }

    @GetMapping
    public String list(Model model) {
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "review_list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        Review review = Review.builder().build();
        model.addAttribute("review", review);
        return "review_form";
    }

    @PostMapping
    public String createReview(Review review) {
        reviewService.save(review);
        return "redirect:/reviews";
    }

    @GetMapping("/{id}/details")
    public String showReviewDetails(@PathVariable Integer id, Model model) {
        Review review = reviewService.findById(id);
        if (review == null) {
            return "404";  // Or handle not found case appropriately
        }
        model.addAttribute("review", review);
        return "review_detail";
    }

    @GetMapping("/{id}/update")
    public String showReviewupdate(@PathVariable Integer id, Model model) {
        Review review = reviewService.findById(id);
        if (review == null) {
            return "404";  // Or handle not found case appropriately
        }
        model.addAttribute("review", review);
        return "review_update";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Integer id) {
        Review review = reviewService.findById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<Review> createReview(@RequestBody Review review) {
//        Review createdReview = reviewService.save(review);
//        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
//    }

    @PostMapping("/{id}")
    public String updateReview(@PathVariable Integer id, @ModelAttribute Review review) {
        if (reviewService.findById(id) == null) {
            return "404";  // Or handle not found case appropriately
        }
        review.setId(id);  // Ensure the ID is set for the update
        reviewService.save(review);
        return "redirect:/reviews";
    }



    @PostMapping("/{id}/delete")
    public String deleteReview(@PathVariable Integer id) {
        if (reviewService.findById(id) == null) {
            return "404";  // Or handle not found case appropriately
        }
        reviewService.deleteById(id);
        return "redirect:/reviews";
    }

}