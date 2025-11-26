package com.sqe.assignment.controller;

import com.sqe.assignment.model.Member;
import com.sqe.assignment.service.MemberService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@PostMapping
	public Member createMember(@RequestBody Member member) {
		return memberService.createMember(member);
	}

	// Update member by email
	public Member updateMember(@PathVariable String email, @RequestBody Member member) {
		return memberService.updateMember(email, member);
	}

	@DeleteMapping("/{email}")
	public void deleteMember(@PathVariable String email) {
		memberService.deleteMember(email);
	}

	@GetMapping("/{email}")
	public Member getMember(@PathVariable String email) {
		return memberService.getMemberByEmail(email);
	}

	@GetMapping
	public List<Member> getAllMembers() {
		return memberService.getAllMembers();
	}
}
