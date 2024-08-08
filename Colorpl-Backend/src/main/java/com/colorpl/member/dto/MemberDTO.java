package com.colorpl.member.dto;

import com.colorpl.member.Member;
import com.colorpl.show.domain.detail.Category;
import java.util.Set;
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
    private String profile;
    private Set<Category> categories;

    public static MemberDTO toMemberDTO(Member member) {
        if (member == null) {
            return null;
        }

        return MemberDTO.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .categories(member.getCategories())
                .profile(member.getProfile())
                .build();
    }
}