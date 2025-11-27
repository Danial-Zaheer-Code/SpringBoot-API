package com.sqe.assignment.controller;

import com.sqe.assignment.model.Member;
import com.sqe.assignment.service.MemberService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@PostMapping
	public ResponseEntity<Member> createMember(@RequestBody Member member) {
		Member created = memberService.createMember(member);
		return ResponseEntity.ok(created);
	}

	@PutMapping("/{email}")
	public ResponseEntity<Member> updateMember(@PathVariable String email, @RequestBody Member member) {
		Member updated = memberService.updateMember(email, member);

		if (updated == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{email}")
	public ResponseEntity<Void> deleteMember(@PathVariable String email) {
		boolean deleted = memberService.deleteMember(email);

		if (!deleted)
			return ResponseEntity.notFound().build();

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{email}")
	public ResponseEntity<Member> getMember(@PathVariable String email) {
		Member member = memberService.getMemberByEmail(email);

		if (member == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(member);
	}

	@GetMapping
	public ResponseEntity<List<Member>> getAllMembers() {
		return ResponseEntity.ok(memberService.getAllMembers());
	}
}
