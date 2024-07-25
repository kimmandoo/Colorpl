package com.colorpl.comment.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.member.service.MemberService;
import com.colorpl.review.domain.Review;
import com.colorpl.review.repository.ReviewRepository;
import com.colorpl.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public CommentService(ReviewRepository reviewRepository, MemberRepository memberRepository) {
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
    }

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MemberService memberService;

    public List<CommentDTO> findAll() {
        return commentRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public CommentDTO findById(Long id) {
        return commentRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public CommentDTO save(CommentDTO commentDTO) {
        Comment comment = toEntity(commentDTO);
        Comment savedComment = commentRepository.save(comment);
        return toDTO(savedComment);
    }

    public void deleteById(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new NoSuchElementException("Comment with ID " + id + " does not exist.");
        }
        commentRepository.deleteById(id);
    }

    private CommentDTO toDTO(Comment comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }

    private Comment toEntity(CommentDTO commentDTO) {
        // Extract IDs from DTO if Review and Member are embedded objects
        Integer reviewId = commentDTO.getReview() != null ? commentDTO.getReview().getId() : null;
        Integer memberId = commentDTO.getMember() != null ? commentDTO.getMember().getId() : null;

        // Assuming you have some service or repository to fetch the entities
        Review review = reviewRepository.findById(reviewId).orElse(null);
        Member member = memberRepository.findById(memberId).orElse(null);

        return Comment.builder()
                .id(commentDTO.getId())
                .content(commentDTO.getContent())
                .review(review)  // Set the Review entity
                .member(member)  // Set the Member entity
                .build();
    }
}