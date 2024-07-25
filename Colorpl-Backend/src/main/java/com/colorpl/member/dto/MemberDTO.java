package com.colorpl.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDTO {
    private Integer id;
    private String email;
    private String nickname;
    private String password;
}