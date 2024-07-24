package com.colorpl.comment.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.member.Member;
import com.colorpl.member.MemberRepository;
import com.colorpl.review.domain.Review;
import com.colorpl.review.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
//@SpringJUnitConfig // Add your Spring configuration class here if needed
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    @DisplayName("정상 댓글 저장 및 조회 테스트")
    @Transactional
    public void saveAndFindComment() {

        // given
        Member member = memberRepository.save(Member.builder()
                .email("member1@example.com")
                .nickname("John Doe")
                .password("password")
                .build());

        Review review = reviewRepository.save(Review.builder()
                .id(1)
                .content("Review Content")
                .build());

        Comment comment = Comment.builder()
                .review(review)
                .member(member)
                .content("This is a comment.")
                .build();
        Comment savedComment = commentRepository.save(comment);

        // when
        Comment retrievedComment = commentRepository.findById(savedComment.getId()).orElse(null);

        // then
        assertThat(retrievedComment).isNotNull();
        assertThat(retrievedComment.getId()).isEqualTo(savedComment.getId());
        assertThat(retrievedComment.getReview()).isEqualTo(savedComment.getReview());
        assertThat(retrievedComment.getMember()).isEqualTo(savedComment.getMember());
        assertThat(retrievedComment.getContent()).isEqualTo(savedComment.getContent());
    }
}
