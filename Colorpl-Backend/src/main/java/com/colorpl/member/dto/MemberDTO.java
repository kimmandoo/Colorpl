package com.colorpl.member.dto;

import com.colorpl.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String email;
    private String nickname;
    private String password;

    public static MemberDTO toMemberDTO(Member member) {
        if (member == null) {
            return null;
        }

        return MemberDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .build();
    }
}