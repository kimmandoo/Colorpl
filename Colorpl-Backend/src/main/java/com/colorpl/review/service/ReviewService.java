package com.colorpl.review.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.global.common.exception.MemberMismatchException;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Empathy;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.DetailReviewDTO;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.repository.EmpathyRepository;
import com.colorpl.review.repository.ReviewRepository;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ReviewService {

//    @Autowired
//    private ReviewRepository reviewRepository;

//    @Autowired
//    private MemberRepository memberRepository;

//    @Autowired
//    private CommentRepository commentRepository;
//
//    @Autowired
//    private TicketRepository ticketRepository;


    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;
    private final EmpathyRepository empathyRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,CommentRepository commentRepository, TicketRepository ticketRepository, MemberRepository memberRepository, EmpathyRepository empathyRepository) {
        this.reviewRepository = reviewRepository;
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.empathyRepository = empathyRepository;
    }

    // 리뷰 무한 스크롤
    public List<ReviewDTO> getReviews(Integer memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Review> reviews = reviewRepository.findAll(pageable).getContent();
        return reviews.stream()
                .map(review -> toReviewDTO(memberId, review)) // Pass size to toReviewDTO
                .collect(Collectors.toList());
    }

    // 특정 멤버 리뷰 리스트
    public List<ReviewDTO> findReviewsOfMember(Integer memberId, int page, int size) {
        // id로 멤버 찾기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        // 관련 티켓 추출
        List<Ticket> tickets = member.getTickets();

        // null인 리뷰 외 모두 추출
        List<Review> reviews = tickets.stream()
                .map(Ticket::getReview) // 각 티켓의 리뷰 추출
                .filter(Objects::nonNull) // null 확인
                .distinct() // 중복 리뷰 방지
                .collect(Collectors.toList());

        // 페이지화
        int start = page * size;
        int end = Math.min(start + size, reviews.size());

        if (start >= reviews.size()) {
            return Collections.emptyList(); // Return empty list if page is out of bounds
        }

        List<Review> paginatedReviews = reviews.subList(start, end);

        // DTO로 반환
        return paginatedReviews.stream()
                .map(review -> toReviewDTO(memberId, review))
                .collect(Collectors.toList());
    }




    @Transactional(readOnly = true)
    public ReviewDTO findById(Long reviewId, Integer memberId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(MemberNotFoundException::new);
        return toReviewDTO(memberId, review);
    }


    private List<Comment> convertToComments(List<CommentDTO> commentDTOs, Review review, Member member) {
        return commentDTOs.stream()
                .map(dto -> dto.toComment (member, review))
                .collect(Collectors.toList());
    }

    @Transactional
    public DetailReviewDTO createReview(Integer memberId, Long ticketId, DetailReviewDTO detailReviewDTO) {
        // 멤버 및 티켓 가져오기
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        // Build review entity from DTO
        Review review = Review.builder()
                .ticket(ticket)
                .content(detailReviewDTO.getContent())
                .spoiler(detailReviewDTO.getSpoiler())
                .emotion(detailReviewDTO.getEmotion())
                .emphathy(detailReviewDTO.getEmpathy())
                .build();

        Review savedReview = reviewRepository.save(review);

        // DTO의 댓글들 엔티티화
        List<Comment> comments = detailReviewDTO.getComments().stream()
                .map(dto -> dto.toComment(member, savedReview))
                .toList();


        // DTO 반환
        return DetailReviewDTO.toDetailReviewDTO(memberId, savedReview);
    }


    @Transactional
    public DetailReviewDTO updateReview(Integer memberId, Long reviewId, DetailReviewDTO detailReviewDTO) {
        // 리뷰 가져오기
        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        ;
        // 리뷰 업데이트
        review.updateReview(detailReviewDTO.getContent(), detailReviewDTO.getSpoiler(), detailReviewDTO.getEmotion());
        Review updatedReview = reviewRepository.save(review);
        return detailReviewDTO.toDetailReviewDTO(memberId, updatedReview);
    }


    @Transactional
    public void deleteById(Long id) {
        // 리뷰 찾기, 못찾으면 NoSuchElementException
        if (!reviewRepository.existsById(id)) {
            throw new ReviewNotFoundException();
        }
        // 삭제
        reviewRepository.deleteById(id);
    }


    private DetailReviewDTO convertToDTO(Review review) {
        List<CommentDTO> commentDTOs = Optional.ofNullable(review.getComments())
                .orElse(Collections.emptyList())
                .stream()
                .map(CommentDTO::toCommentDTO) // Convert each Comment to CommentDTO
                .collect(Collectors.toList());

        return DetailReviewDTO.builder()
                .id(review.getId())
                .content(review.getContent())
                .spoiler(review.getSpoiler())
                .emotion(review.getEmotion())
                .empathy(review.getEmphathy())
                .comments(commentDTOs)
                .build();
    }

    public Optional<Empathy> findByReviewAndMember(Review review, Member member) {
//        System.out.println("Review ID: " + review.getId());
//        System.out.println("Member ID: " + member.getId());
        return empathyRepository.findByReviewAndMember(review, member);
    }

    public ReviewDTO toReviewDTO(Integer memberId, Review review) {
        List<CommentDTO> commentDTOs = review.getComments().stream()
                .map(CommentDTO::toCommentDTO)
                .collect(Collectors.toList());
        int totalComments = commentDTOs.size();

        boolean myreviewcheck = review.getTicket() != null &&
                review.getTicket().getMember() != null &&
                review.getTicket().getMember().getId() != null &&
                review.getTicket().getMember().getId().equals(memberId);

        Integer size = 10;
        int pages = (int) Math.ceil((double) commentDTOs.size() / size);

        String formattedDate = review.getCreateDate() != null ? review.getCreateDate().format(formatter) : null;

        Member member = memberRepository.findById(memberId).orElseThrow(MemberMismatchException::new);

        boolean myempathy = empathyRepository.findByReviewAndMember(review, member).isPresent();
//        System.out.println(myempathy);
        return ReviewDTO.builder()
                .id(review.getId())
                .ticketId(review.getTicket() != null ? review.getTicket().getId() : null)
                .writer(review.getTicket() != null && review.getTicket().getMember() != null ? review.getTicket().getMember().getNickname() : null)
                .title(review.getTicket() != null ? review.getTicket().getName() : null)
                .category(review.getTicket() != null && review.getTicket().getCategory() != null ? review.getTicket().getCategory().name() : null)
                .content(review.getContent())
                .spoiler(review.getSpoiler())
                .emotion(review.getEmotion())
                .createdate(formattedDate)
                .empathy(review.getEmphathy())
                .myempathy(myempathy)
                .commentpagesize(pages)
                .commentscount(totalComments)
                .myreview(myreviewcheck)
                .build();
    }

}