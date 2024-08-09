package com.colorpl.comment.dto;

import lombok.*;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDTO {
    private String commentContent;
}
