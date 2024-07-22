package com.colorpl.member.repository;

import com.colorpl.member.Member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {



}
