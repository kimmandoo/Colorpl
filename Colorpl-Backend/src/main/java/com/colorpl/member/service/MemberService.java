package com.colorpl.member.service;

import static com.colorpl.member.dto.MemberDTO.toMemberDTO;

import com.colorpl.global.common.exception.*;
import com.colorpl.global.common.storage.StorageService;
import com.colorpl.global.common.storage.UploadFile;
import com.colorpl.global.config.TokenProvider;
import com.colorpl.member.Member;
import com.colorpl.member.MemberRefreshToken;
import com.colorpl.member.dto.FollowCountDTO;
import com.colorpl.member.dto.MemberDTO;
import com.colorpl.member.dto.SignInResponse;
import com.colorpl.member.repository.MemberRefreshTokenRepository;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.show.domain.detail.Category;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberRefreshTokenRepository memberRefreshTokenRepository;
    private final StorageService storageService;



    private static final int MAX_CATEGORIES = 2;
    @Transactional
    public Member registerOauthMember(MemberDTO memberDTO) {

        if (memberRepository.findByEmail(memberDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }


        Member member = Member.toMember(memberDTO, passwordEncoder);
        return memberRepository.save(member);
    }

    @Transactional
    public Member registerMember(MemberDTO memberDTO, MultipartFile profileImage) {

        if (memberRepository.findByEmail(memberDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }

        // 프로필 이미지를 업로드하고 파일 이름을 얻습니다.
        String profileFilename = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            UploadFile uploadFile = storageService.storeFile(profileImage);
            profileFilename = uploadFile.getStoreFilename();
        }

        // Member 객체를 생성하고 프로필 파일 이름을 설정합니다.
        memberDTO.setProfile(profileFilename);
        Member member = Member.toMember(memberDTO, passwordEncoder);

        return memberRepository.save(member);
    }

    @Transactional
    public SignInResponse signIn(String email, String password) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(MemberRequestNotMatchException::new);

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new MemberRequestNotMatchException();
        }

        String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", member.getId(), member.getType()));	// token -> accessToken
        String refreshToken = tokenProvider.createRefreshToken();	// 리프레시 토큰 생성
        // 리프레시 토큰이 이미 있으면 토큰을 갱신하고 없으면 토큰을 추가
        memberRefreshTokenRepository.findById(member.getId())
            .ifPresentOrElse(
                it -> it.updateRefreshToken(refreshToken),
                () -> memberRefreshTokenRepository.save(new MemberRefreshToken(member, refreshToken))
            );
        return new SignInResponse(member.getEmail(), member.getType(), accessToken, refreshToken);
    }
    @Transactional
    public SignInResponse oauthSignIn(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isPresent()) {
            // 기존 회원이 있으면 로그인 처리
            Member member = optionalMember.get();
            String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", member.getId(), member.getType()));
            String refreshToken = tokenProvider.createRefreshToken();
            memberRefreshTokenRepository.findById(member.getId())
                .ifPresentOrElse(
                    it -> it.updateRefreshToken(refreshToken),
                    () -> memberRefreshTokenRepository.save(new MemberRefreshToken(member, refreshToken))
                );
            return new SignInResponse(member.getEmail(), member.getType(), accessToken, refreshToken);
        } else {
            // 기존 회원이 없으면 회원 가입 후 로그인 처리
            String randomPassword = UUID.randomUUID().toString(); // 랜덤 비밀번호 생성
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setEmail(email);
            memberDTO.setPassword(randomPassword); // 비밀번호 설정

            // 필요한 경우 추가 정보 설정
            Member newMember = registerOauthMember(memberDTO);
            String accessToken = tokenProvider.createAccessToken(String.format("%s:%s", newMember.getId(), newMember.getType()));
            String refreshToken = tokenProvider.createRefreshToken();
            memberRefreshTokenRepository.save(new MemberRefreshToken(newMember, refreshToken));
            return new SignInResponse(newMember.getEmail(), newMember.getType(), accessToken, refreshToken);
        }
    }

//    멤버 정보 업데이트
//    @Transactional
//    public Member updateMemberInfo(Integer memberId, MemberDTO memberDTO) {
//        Member existingMember = memberRepository.findById(memberId)
//            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));
//
//        existingMember.updateMember(Member.toMember(memberDTO, passwordEncoder), passwordEncoder);
//        return memberRepository.save(existingMember);
//    }
    @Transactional
    public Member updateMemberInfo(Integer memberId, MemberDTO memberDTO, MultipartFile profileImage) {
        // 1. 멤버 존재 확인
        Member existingMember = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버입니다."));

        String email = existingMember.getEmail();

        // 2. 이메일 변경 불가 검증
        if (memberDTO.getEmail() != null && !memberDTO.getEmail().equals(email)) {
            throw new EmailNotEditException();
        }

        // 3. 닉네임 업데이트
        if (memberDTO.getNickname() != null) {
            existingMember.updateNickname(memberDTO.getNickname());
        }

        // 4. 비밀번호 업데이트
        if (memberDTO.getPassword() != null) {
            existingMember.updatePassword(passwordEncoder.encode(memberDTO.getPassword()));
        }

        // 5. 프로필 이미지 처리
        if (profileImage != null && !profileImage.isEmpty()) {
            UploadFile uploadFile = storageService.storeFile(profileImage);
            String profileFilename = uploadFile.getStoreFilename();
            existingMember.updateProfile(profileFilename);
        } else if (memberDTO.getProfile() != null) {
            // 프로필 이미지 파일명이 주어진 경우 데이터베이스 업데이트
            existingMember.updateProfile(memberDTO.getProfile());
        }

        // 6. 카테고리 업데이트
        if (memberDTO.getCategories() != null) {
            existingMember.updateCategories(memberDTO.getCategories());
        }
        // 7. 멤버 정보 저장
        return memberRepository.save(existingMember);
    }



    //모든 멤버 조회
    @Transactional
    public List<Member> getAllUsers() {
        return memberRepository.findAll();
    }


    @Transactional
    public MemberDTO findMemberById(Integer memberId) {
        Member existingMember = memberRepository.findById(memberId)
            .orElseThrow(MemberNotFoundException::new);

        return toMemberDTO(existingMember);
    }

    @Transactional
    public MemberDTO findMemberByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new EmailNotFoundException();
        }

        Member existingMember = memberRepository.findByEmail(email)
            .orElseThrow(() -> new BusinessException(Messages.MEMBER_NOT_FOUND));

        return toMemberDTO(existingMember);
    }

    @Transactional
    public String followMember(Integer memberId, Integer followId) {
        if (memberId.equals(followId)) {
            throw new MemberSelfFollowException();
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        Member followMember = memberRepository.findById(followId)
                .orElseThrow(MemberNotFoundException::new);

        if (member.getFollowingList().contains(followMember)) {
            throw new MemberAlreadyFollowException();
        }

        member.addFollowing(followMember);
        memberRepository.save(member);
        return followMember.getNickname()+ "님을 팔로우 합니다";
    }

    @Transactional
    public String unfollowMember(Integer memberId, Integer followId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        Member followMember = memberRepository.findById(followId)
                .orElseThrow(MemberNotFoundException::new);

        member.removeFollowing(followMember);
        memberRepository.save(member);
        return followMember.getNickname()+ "님을 언팔로우 합니다";
    }

    @Transactional
    public List<Member> getFollowers(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return List.copyOf(member.getFollowerList());
    }

    @Transactional
    public List<Member> getFollowing(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return List.copyOf(member.getFollowingList());
    }
    @Transactional
    public FollowCountDTO getFollowersCount(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        int followersCount = member.getFollowerList().size();
        return new FollowCountDTO(followersCount); // members 필드는 사용하지 않음
    }

    @Transactional
    public FollowCountDTO getFollowingCount(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        int followingCount = member.getFollowingList().size();
        return new FollowCountDTO(followingCount); // members 필드는 사용하지 않음
    }

    //기본적으로 Authorization 헤더에 값이 있어야 정상 사용 가능
    public Integer getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return Integer.parseInt(userDetails.getUsername());
    }
    // nickname으로 멤버를 조회하는 메서드
    @Transactional
    public List<MemberDTO> findMembersByNickname(String nickname) {
        List<Member> members = memberRepository.findByNickname(nickname);
        if (members.isEmpty()) {
            throw new MemberNotFoundException();
        }
        return members.stream()
            .map(MemberDTO::toMemberDTO)
            .collect(Collectors.toList());
    }


}
