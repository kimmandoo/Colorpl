package com.colorpl.review.dto;

import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.review.domain.Review;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class DetailReviewDTO {
    private Long id;
    private Long ticketId;
    private String writer;
    private String title;
    private String category;
    private String content;
    private Boolean spoiler;
    private Byte emotion;
    private LocalDateTime createdate;
    private Integer empathy;
    private Integer commentpagesize;
    private Integer commentscount;
    private boolean myreview;
    @Builder.Default
    private List<CommentDTO> comments = new ArrayList<>(); // Initialize to empty list


    public static DetailReviewDTO toDetailReviewDTO(Integer memberId, Review review) {
        List<CommentDTO> commentDTOs = review.getComments().stream()
                .map(CommentDTO::toCommentDTO)
                .collect(Collectors.toList());
        boolean myreviewcheck = false;

        if (review.getTicket() != null && review.getTicket().getMember() != null && review.getTicket().getMember().getId() != null && review.getTicket().getMember().getId() == memberId) {
            myreviewcheck = true;
        } else {
            myreviewcheck = false;
        }

        int totalComments = commentDTOs.size();

        // size is written here
        Integer size = 10;

        int pages = (int) Math.ceil((double) commentDTOs.size() / size); // size

        return DetailReviewDTO.builder()
                .id(review.getId())
                .ticketId(review.getTicket() != null ? review.getTicket().getId() : null)
                .writer(review.getTicket() != null && review.getTicket().getMember() != null ? review.getTicket().getMember().getNickname() : null)
                .title(review.getTicket() != null ? review.getTicket().getName() : null)
                .category(review.getTicket() != null && review.getTicket().getCategory() != null ? review.getTicket().getCategory().name() : null)
                .content(review.getContent())
                .spoiler(review.getSpoiler())
                .emotion(review.getEmotion())
                .createdate(review.getCreateDate())
                .empathy(review.getEmphathy())
                .commentpagesize(pages)
                .commentscount(totalComments)
                .myreview(myreviewcheck)
                .comments(commentDTOs)
                .build();
    }

}