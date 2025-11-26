package com.sqe.assignment.service;

import com.sqe.assignment.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	private DataSource dataSource;

	@Override
	public Member createMember(Member member) {
		String sql = "INSERT INTO members (name, email) VALUES (?, ?)";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, member.getName());
			stmt.setString(2, member.getEmail());
			stmt.executeUpdate();

			return member;

		} catch (SQLException e) {
			throw new RuntimeException("Error inserting member", e);
		}
	}

	@Override
	public Member updateMember(String email, Member member) {
		String sql = "UPDATE members SET name = ?, email = ? WHERE email = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, member.getName());
			stmt.setString(2, member.getEmail());
			stmt.setString(3, email);

			stmt.executeUpdate();
			return member;

		} catch (SQLException e) {
			throw new RuntimeException("Error updating member", e);
		}
	}

	@Override
	public void deleteMember(String email) {
		String sql = "DELETE FROM members WHERE email = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email);
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Error deleting member", e);
		}
	}

	@Override
	public Member getMemberByEmail(String email) {
		String sql = "SELECT name, email FROM members WHERE email = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Member member = new Member();
				member.setName(rs.getString("name"));
				member.setEmail(rs.getString("email"));
				return member;
			}

			return null;

		} catch (SQLException e) {
			throw new RuntimeException("Error fetching member by email", e);
		}
	}

	@Override
	public List<Member> getAllMembers() {
		String sql = "SELECT name, email FROM members";

		List<Member> members = new ArrayList<>();

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Member member = new Member();
				member.setName(rs.getString("name"));
				member.setEmail(rs.getString("email"));
				members.add(member);
			}

			return members;

		} catch (SQLException e) {
			throw new RuntimeException("Error fetching all members", e);
		}
	}
}
