package com.colorpl.review.dto;

import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.global.common.exception.MemberMismatchException;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.domain.Review;
import com.colorpl.review.repository.EmpathyRepository;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
public class ReviewDTO {
    private Long id;
    private Long ticketId;
    private String writer;
    private boolean myreview;
    private String title;
    private String createdate;
    private String category;
    private String imgurl;
    private String content;
    private Boolean spoiler;
    private Byte emotion;
    private Integer empathy;
    private Boolean myempathy;
    private Integer commentpagesize;
    private Integer commentscount;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

    // Constructor
    public ReviewDTO(Long id, Long ticketId, String writer, boolean myreview, String title, String createdate, String category, String imgurl, String content, Boolean spoiler, Byte emotion, Integer empathy, Boolean myempathy, Integer commentpagesize, Integer commentscount) {
        this.id = id;
        this.ticketId = ticketId;
        this.writer = writer;
        this.myreview = myreview;
        this.title = title;
        this.createdate = createdate;
        this.category = category;
        this.imgurl = imgurl;
        this.content = content;
        this.spoiler = spoiler;
        this.emotion = emotion;
        this.empathy = empathy;
        this.myempathy = myempathy;
        this.commentpagesize = commentpagesize;
        this.commentscount = commentscount;
    }
}
