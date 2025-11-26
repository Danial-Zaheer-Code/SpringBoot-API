package com.sqe.assignment.service;

import com.sqe.assignment.model.Member;

import java.util.List;

public interface MemberService {

	Member createMember(Member member);

	Member updateMember(String email, Member member);

	void deleteMember(String email);

	Member getMemberByEmail(String email);

	List<Member> getAllMembers();
}
