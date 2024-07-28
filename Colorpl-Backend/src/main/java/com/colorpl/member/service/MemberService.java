package com.colorpl.member.service;

import com.colorpl.member.Member;
import com.colorpl.member.dto.MemberDTO;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.review.dto.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<MemberDTO> findAll() {
        return memberRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public MemberDTO findById(Integer id) {
        return memberRepository.findById(id).map(this::toDTO).orElse(null);
    }

    public MemberDTO save(MemberDTO memberDTO) {
        Member member = toEntity(memberDTO);
        Member savedMember = memberRepository.save(member);
        return toDTO(savedMember);
    }

    public void deleteById(Integer id) {
        if (!memberRepository.existsById(id)) {
            throw new NoSuchElementException("Member with ID " + id + " does not exist.");
        }
        memberRepository.deleteById(id);
    }

    public MemberDTO toDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword()) // Avoid exposing sensitive information if not needed
                .build();
    }

    private Member toEntity(MemberDTO memberDTO) {
        return Member.builder()
                .id(memberDTO.getId())
                .email(memberDTO.getEmail())
                .nickname(memberDTO.getNickname())
                .password(memberDTO.getPassword()) // Handle password securely
                .build();
    }
}
