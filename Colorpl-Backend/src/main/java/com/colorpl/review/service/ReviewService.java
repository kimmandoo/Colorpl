package com.colorpl.review.service;

import com.colorpl.review.domain.Review;
import com.colorpl.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review findById(Integer id) {

        return reviewRepository.findById(id).orElse(null);
    }

    public Review save(Review review) {

        return reviewRepository.save(review);
    }

    public void deleteById(Integer id) {
        if (!reviewRepository.existsById(id)) {
            throw new NoSuchElementException("Review with ID " + id + " does not exist.");
        }
        reviewRepository.deleteById(id);
    }
}