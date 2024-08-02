package com.colorpl.member.controller;

import com.colorpl.global.common.exception.BusinessException;
import com.colorpl.global.common.exception.EmailNotFoundException;
import com.colorpl.global.common.exception.MemberNotFoundException;
import com.colorpl.member.Member;
import com.colorpl.member.dto.FollowCountDTO;
import com.colorpl.member.dto.MemberDTO;
import com.colorpl.member.dto.SignInRequest;
import com.colorpl.member.dto.SignInResponse;
import com.colorpl.member.service.BlackListService;
import com.colorpl.member.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @Operation(summary = "회원가입", description = "회원 가입을 진행하는 API")
    public ResponseEntity<MemberDTO> registerMember(@RequestBody MemberDTO memberDTO) {
        Member member = memberService.registerMember(memberDTO);
        return ResponseEntity.ok(MemberDTO.toMemberDTO(member));
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인을 진행하는 API")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
        SignInResponse memberDTO = memberService.signIn(signInRequest.getEmail(), signInRequest.getPassword());
        return ResponseEntity.ok(memberDTO);
    }

    @PutMapping("/{memberId}")
    @Operation(summary = "멤버 수정", description = "멤버 정보를 수정하는 API")
    public ResponseEntity<MemberDTO> updateMember(@PathVariable Integer memberId, @RequestBody MemberDTO memberDTO) {
        Member updatedMember = memberService.updateMemberInfo(memberId, memberDTO);
        return ResponseEntity.ok(MemberDTO.toMemberDTO(updatedMember));
    }
    // ID로 멤버 조회
    @GetMapping("/{memberId}")
    @Operation(summary = "memberId로 멤버 조회", description = "memberId로 멤버를 조회하는 API")
    public ResponseEntity<MemberDTO> findMemberById(@PathVariable Integer memberId) {
        try {
            MemberDTO memberDTO = memberService.findMemberById(memberId);
            return ResponseEntity.ok(memberDTO);
        } catch (MemberNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 이메일로 멤버 조회
    @GetMapping("/email/{email}")
    @Operation(summary = "email로 멤버 조회", description = "email로 멤버를 조회하는 API")
    public ResponseEntity<MemberDTO> findMemberByEmail(@PathVariable String email) {
        try {
            MemberDTO memberDTO = memberService.findMemberByEmail(email);
            return ResponseEntity.ok(memberDTO);
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


//    // 나의 리뷰 조회
//    @GetMapping("/{memberId}/reviews")
//    public ResponseEntity<List<Review>> getMyReviews(@PathVariable Integer memberId) {
//        List<Review> reviews = memberService.getMyReviews(memberId);
//        return new ResponseEntity<>(reviews, HttpStatus.OK);
//    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "모든 멤버 조회", description = "모든 멤버를 조회하는 API")
    public ResponseEntity<List<MemberDTO>> retrieveAllMembers() {
        List<Member> members = memberService.getAllUsers();
        List<MemberDTO> memberDTOs = members.stream()
                .map(MemberDTO::toMemberDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(memberDTOs);
    }
    @PostMapping("/sign-out")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "로그아웃", description = "로그 아웃을 진행하는 API, Refresh Token을 BlackList 처리")
    public ResponseEntity<Void> signOut(@RequestHeader("Refresh") String refreshToken) {
        try {
            blackListService.signOut(refreshToken);
            return ResponseEntity.noContent().build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/{memberId}/follow/{followId}")
    @Operation(summary = "팔로우", description = "특정 멤버가 다른 멤버를 팔로우하는 API")
    public ResponseEntity<String> followMember(@PathVariable Integer memberId, @PathVariable Integer followId) {
        String followMember = memberService.followMember(memberId, followId);
        return ResponseEntity.ok(followMember);
    }

    @DeleteMapping("/{memberId}/unfollow/{followId}")
    @Operation(summary = "언팔로우", description = "특정 멤버가 다른 멤버의 팔로우를 취소하는 API")
    public ResponseEntity<String> unfollowMember(@PathVariable Integer memberId, @PathVariable Integer followId) {
        String unfollowMember = memberService.unfollowMember(memberId, followId);
        return ResponseEntity.status(HttpStatus.OK).body(unfollowMember);
    }

    @GetMapping("/{memberId}/followers")
    @Operation(summary = "팔로워 조회", description = "특정 멤버의 팔로워를 조회하는 API")
    public ResponseEntity<List<MemberDTO>> getFollowers(@PathVariable Integer memberId) {
        List<MemberDTO> followers = memberService.getFollowers(memberId)
                .stream()
                .map(MemberDTO::toMemberDTO)
                .toList();
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{memberId}/following")
    @Operation(summary = "팔로우 조회", description = "특정 멤버의 팔로우를 조회하는 API")
    public ResponseEntity<List<MemberDTO>> getFollow(@PathVariable Integer memberId) {
        List<MemberDTO> following = memberService.getFollowing(memberId)
                .stream()
                .map(MemberDTO::toMemberDTO)
                .toList();
        return ResponseEntity.ok(following);
    }

    @GetMapping("/{memberId}/followers/count")
    @Operation(summary = "팔로워 수 조회", description = "특정 멤버의 팔로워 수를 조회하는 API")
    public ResponseEntity<FollowCountDTO> getFollowersCount(@PathVariable Integer memberId) {
        FollowCountDTO followersCount = memberService.getFollowersCount(memberId);
        return ResponseEntity.ok(followersCount);
    }

    @GetMapping("/{memberId}/following/count")
    @Operation(summary = "팔로우 수 조회", description = "특정 멤버의 팔로우 수를 조회하는 API")
    public ResponseEntity<FollowCountDTO> getFollowCount(@PathVariable Integer memberId) {
        FollowCountDTO followingCount = memberService.getFollowingCount(memberId);
        return ResponseEntity.ok(followingCount);
    }

}
