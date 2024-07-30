package com.colorpl.member.controller;

import com.colorpl.member.Member;
import com.colorpl.member.dto.MemberDTO;
import com.colorpl.member.dto.SignInRequest;
import com.colorpl.member.dto.SignInResponse;
import com.colorpl.member.service.BlackListService;
import com.colorpl.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final BlackListService blackListService;

    @Autowired
    public MemberController(MemberService memberService, BlackListService blackListService) {
        this.memberService = memberService;
        this.blackListService = blackListService;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberDTO> registerMember(@RequestBody MemberDTO memberDTO) {
        Member member = memberService.registerMember(memberDTO);
        return ResponseEntity.ok(MemberDTO.toMemberDTO(member));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
        SignInResponse memberDTO = memberService.signIn(signInRequest.getEmail(), signInRequest.getPassword());
        return ResponseEntity.ok(memberDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable Integer id, @RequestBody MemberDTO memberDTO) {
        Member updatedMember = memberService.updateMemberInfo(id, memberDTO);
        return ResponseEntity.ok(MemberDTO.toMemberDTO(updatedMember));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<MemberDTO>> retrieveAllMembers() {
        List<Member> members = memberService.getAllUsers();
        List<MemberDTO> memberDTOs = members.stream()
                .map(MemberDTO::toMemberDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(memberDTOs);
    }
//    @PostMapping("/sign-out")
//    public ResponseEntity<String> signOut(@RequestHeader("Authorization") String accessToken,
//        @RequestBody String refreshToken) {
//        try {
//            // Bearer 토큰 앞부분 제거
//            String token = accessToken.replace("Bearer ", "");
//
//            // 로그아웃 처리
//            blackListService.signOut(refreshToken, token);
//
//            return ResponseEntity.ok("로그아웃 되었습니다.");
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.badRequest().body("Invalid token");
//        }
//    }
    @PostMapping("/sign-out")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> signOut(@RequestHeader("Refresh") String refreshToken) {
        try {
            blackListService.signOut(refreshToken);
            return ResponseEntity.noContent().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
