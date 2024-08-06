package com.colorpl.review.dto;

import com.colorpl.comment.dto.CommentDTO;
import com.colorpl.review.domain.Review;
import lombok.Builder;
import lombok.Data;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class RequestDTO_cpy {
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




    // 공감확인, 이미지파일, 리뷰 페이지 묶음 json 형식으로.
    // yyyy년 MM월 dd일 hh:mm
    // 1. 더미데이터 20개정도
    // 2. DTO 먼저
    // 3. JSON파싱
//    {
//        "items" : List<DTO>,
//        "totalPage" : 40
//    }

    @Builder.Default
    private List<CommentDTO> comments = new ArrayList<>(); // Initialize to empty list

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

    public static RequestDTO_cpy toDetailReviewDTO(Integer memberId, Review review) {
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
        String formattedDate = review.getCreateDate() != null ? review.getCreateDate().format(formatter) : null;


        return RequestDTO_cpy.builder()
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
                .commentpagesize(pages)
                .commentscount(totalComments)
                .myreview(myreviewcheck)
                .comments(commentDTOs)
                .build();
    }

}



