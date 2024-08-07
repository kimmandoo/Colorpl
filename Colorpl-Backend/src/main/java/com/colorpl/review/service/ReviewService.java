package com.colorpl.review.service;

import com.colorpl.comment.domain.Comment;
import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.comment.repository.CommentRepository;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.global.common.storage.StorageService;
import com.colorpl.global.common.storage.UploadFile;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Empathy;
import com.colorpl.review.domain.EmpathyId;
import com.colorpl.review.domain.Review;
import com.colorpl.review.dto.ReadReviewResponse;
import com.colorpl.review.dto.RequestDTO;
import com.colorpl.review.dto.ReviewDTO;
import com.colorpl.review.dto.ReviewDTO.ReviewDTOBuilder;
import com.colorpl.review.repository.EmpathyRepository;
import com.colorpl.review.repository.ReviewRepository;
import com.colorpl.schedule.command.domain.CustomSchedule;
import com.colorpl.schedule.command.domain.ReservationSchedule;
import com.colorpl.schedule.command.domain.Schedule;
import com.colorpl.schedule.command.domain.ScheduleRepository;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//@RequiredArgsConstructor
@Service
@Transactional
public class ReviewService {

    private final StorageService storageService;
    private final ReviewRepository reviewRepository;
    //    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final EmpathyRepository empathyRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
        "yyyy년 MM월 dd일 HH:mm");

    @Autowired
    public ReviewService(StorageService storageService, ReviewRepository reviewRepository,
        CommentRepository commentRepository, ScheduleRepository scheduleRepository,
        MemberRepository memberRepository, EmpathyRepository empathyRepository) {
        this.storageService = storageService;
        this.reviewRepository = reviewRepository;
        this.scheduleRepository = scheduleRepository;
//        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.empathyRepository = empathyRepository;
    }

    // 전체 리뷰 무한 스크롤
    public ReadReviewResponse getReviews(Integer memberId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviews = reviewRepository.findAll(pageable);
        List<ReviewDTO> reviewDTOS = reviews.stream()
            .map(review -> toReviewDTO(memberId, review))
            .collect(Collectors.toList());
        int totalPages = (int) Math.ceil((double) reviewRepository.count() / size);
        ReadReviewResponse response = ReadReviewResponse.builder().items(reviewDTOS)
            .totalPage(totalPages).build();
        return response;
    }

    // 특정 멤버의 리뷰들만 조회
    public ReadReviewResponse findReviewsOfMember(Integer memberId, int page, int size) {
        // id로 멤버 찾기
        Member member = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        // 관련 일정 추출
        List<Schedule> schedules = member.getSchedules();

        // null 인 일정 외 모두 추출
        List<Review> reviews = schedules.stream()
            .map(Schedule::getReview)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .distinct()
            .toList();

        // 페이지화
        int start = page * size;
        int end = Math.min(start + size, reviews.size());

        if (start >= reviews.size()) {
            return ReadReviewResponse.builder()
                .items(Collections.emptyList())
                .totalPage(0)
                .build(); // Return empty response if page is out of bounds
        }

        List<Review> paginatedReviews = reviews.subList(start, end);

        // DTO로 반환
        List<ReviewDTO> reviewDTOS = paginatedReviews.stream()
            .map(review -> toReviewDTO(memberId, review))
            .collect(Collectors.toList());

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) reviews.size() / size);

        // Create and return response
        ReadReviewResponse response = ReadReviewResponse.builder()
            .items(reviewDTOS)
            .totalPage(totalPages)
            .build();

        return response;
    }

    // 특정 리뷰 찾기
    @Transactional(readOnly = true)
    public ReviewDTO findById(Long reviewId, Integer memberId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(ReviewNotFoundException::new);
        return toReviewDTO(memberId, review);
    }

    private List<Comment> convertToComments(List<CommentDTO> commentDTOs, Review review,
        Member member) {
        return commentDTOs.stream()
            .map(dto -> dto.toComment(member, review))
            .collect(Collectors.toList());
    }

    @Transactional
    public Long createReview(RequestDTO requestDTO, MultipartFile file) {
        // 멤버 및 티켓 가져오기
        Member member = memberRepository.findById(requestDTO.getMemberId())
            .orElseThrow(MemberNotFoundException::new);

        Schedule schedule = scheduleRepository.findById(requestDTO.getTicketId())
            .orElseThrow(() -> new RuntimeException("Schedule not found"));

        String filename;
        UploadFile uploadFile = null;

        if (file != null && !file.isEmpty()) {
            // Store the provided file
            uploadFile = storageService.storeFile(file);
            filename = uploadFile.getStoreFilename();
        } else {
            // No file provided, use a placeholder filename or null
            filename = "noimg"; // Placeholder filename
        }

        System.out.println("uploaded image name" + uploadFile);
        // Build review entity from DTO
        Review review = Review.builder()
            .filename(filename)
            .schedule(schedule)
            .content(requestDTO.getContent())
            .spoiler(requestDTO.getSpoiler())
            .emotion(requestDTO.getEmotion())
            .emphathy(0)
            .build();

        // 저장 및 id 반환
        return reviewRepository.save(review).getId();
    }

    @Transactional
    public ReviewDTO updateReview(Integer memberId, Long reviewId, RequestDTO detailReviewDTO) {
        // 리뷰 가져오기
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(ReviewNotFoundException::new);

        // 리뷰 업데이트
        review.updateReview(detailReviewDTO.getContent(), detailReviewDTO.getSpoiler(),
            detailReviewDTO.getEmotion());
        Review updatedReview = reviewRepository.save(review);
        return toReviewDTO(memberId, updatedReview);
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


    public Optional<Empathy> findByReviewAndMember(Long reviewId, Integer memberId) {
        EmpathyId empathyId = new EmpathyId(reviewId, memberId);
        return empathyRepository.findById(empathyId);
    }

    public ReviewDTO toReviewDTO(Integer memberId, Review review) {
        List<CommentDTO> commentDTOs = review.getComments().stream()
            .map(CommentDTO::toCommentDTO)
            .toList();
        int totalComments = commentDTOs.size();

        boolean myReviewCheck = review.getSchedule() != null &&
            review.getSchedule().getMember() != null &&
            review.getSchedule().getMember().getId() != null &&
            review.getSchedule().getMember().getId().equals(memberId);

        int size = 10; // Assuming fixed size for comment pages
        int pages = (int) Math.ceil((double) totalComments / size);

        String formattedDate =
            review.getCreateDate() != null ? review.getCreateDate().format(formatter) : null;

        boolean myEmpathy = empathyRepository.findById(new EmpathyId(review.getId(), memberId))
            .isPresent();

        String basePath = "images/";
        String filename = review.getFilename();
        String filepath;

        if ("noimg".equals(filename)) {
            // Special case for "noimg"
            filepath = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(basePath)
                .path("noimg.png") // or any specific path for placeholder image
                .build()
                .toUriString();
        } else {
            // Standard case
            filepath = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(basePath)
                .path(filename)
                .build()
                .toUriString();
        }

        ReviewDTOBuilder builder = ReviewDTO.builder()
            .id(review.getId())
            .ticketId(review.getSchedule() != null ? review.getSchedule().getId() : null)
            .writer(review.getSchedule() != null && review.getSchedule().getMember() != null
                ? review.getSchedule().getMember().getNickname() : null)
            .imgurl(filepath)
            .content(review.getContent())
            .spoiler(review.getSpoiler())
            .emotion(review.getEmotion())
            .createdate(formattedDate)
            .empathy(review.getEmphathy())
            .myempathy(myEmpathy)
            .commentpagesize(pages)
            .commentscount(totalComments)
            .myreview(myReviewCheck);

        if (review.getSchedule() != null
            && review.getSchedule() instanceof CustomSchedule customSchedule) {
            return builder
                .title(customSchedule.getName() != null ? customSchedule.getName() : null)
                .category(
                    customSchedule.getCategory() != null ? customSchedule.getCategory().toString()
                        : null)
                .build();
        } else if (review.getSchedule() != null
            && review.getSchedule() instanceof ReservationSchedule reservationSchedule) {
            return builder
                .title(reservationSchedule.getReservationDetail().getShowSchedule().getShowDetail()
                    .getName())
                .category(
                    reservationSchedule.getReservationDetail().getShowSchedule().getShowDetail()
                        .getCategory().toString())
                .build();
        }
        return null;
    }
}
