package com.colorpl.member.service;

import com.colorpl.global.config.TokenProvider;
import com.colorpl.member.Member;
import com.colorpl.member.dto.MemberDTO;
import com.colorpl.member.dto.SignInResponse;
import com.colorpl.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    //등록시 이메일 중복 여부 체크하는 로직 추후에 작성
    @Transactional
    public Member registerMember(MemberDTO memberDTO) {
        Member member = Member.toMember(memberDTO, passwordEncoder);
        return memberRepository.save(member);
    }

    @Transactional
    public SignInResponse signIn(String email, String password) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("이메일이나 패스워드가 일치하지 않습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new IllegalArgumentException("이메일이나 패스워드가 일치하지 않습니다.");
        }

        String token = tokenProvider.createToken(email);

        // SignInResponse 생성 및 반환
        return new SignInResponse(member.getNickname(), member.getType(), token);
    }
    @Transactional
    public Member updateMemberInfo(Integer memberId, MemberDTO memberDTO) {
        Member existingMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        existingMember.updateMember(Member.toMember(memberDTO, passwordEncoder), passwordEncoder);
        return memberRepository.save(existingMember);
    }
    @Transactional
    public List<Member> getAllUsers() {
        return memberRepository.findAll();
    }
}
