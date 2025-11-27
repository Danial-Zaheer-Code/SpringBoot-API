package com.sqe.assignment.service;

import com.sqe.assignment.model.Book;
import com.sqe.assignment.service.util.SQLExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private DataSource dataSource;


	@Override
	public Book createBook(Book book) {
		String sql = "INSERT INTO books (title, author, member_id) " +
				"VALUES (?, ?, (SELECT id FROM members WHERE name = ?))";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getMemberName());

			stmt.executeUpdate();
		} catch (SQLException e) {
			SQLExceptionTranslator.translateAndThrow(e);
		}

		return book;
	}

	@Override
	public Book updateBook(String title, Book book) {
		String sql = "UPDATE books SET title = ?, author = ?, member_id = " +
				"(SELECT id FROM members WHERE name = ?) WHERE title = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getMemberName());
			stmt.setString(4, title);

			int rows = stmt.executeUpdate();

			if (rows == 0) return null;

			return getBookByTitle(book.getTitle());

		} catch (SQLException e) {
			SQLExceptionTranslator.translateAndThrow(e);
			return null;
		}
	}

	@Override
	public boolean deleteBook(String title) {
		String sql = "DELETE FROM books WHERE title = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, title);
			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			SQLExceptionTranslator.translateAndThrow(e);
			return false;
		}
	}

	@Override
	public Book getBookByTitle(String title) {
		String sql = "SELECT b.title, b.author, m.name AS member_name " +
				"FROM books b LEFT JOIN members m ON b.member_id = m.id " +
				"WHERE b.title = ?";

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, title);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) return null;

			Book b = new Book();
			b.setTitle(rs.getString("title"));
			b.setAuthor(rs.getString("author"));
			b.setMemberName(rs.getString("member_name"));
			return b;

		} catch (SQLException e) {
			SQLExceptionTranslator.translateAndThrow(e);
			return null;
		}
	}

	@Override
	public List<Book> getAllBooks() {
		String sql = "SELECT b.title, b.author, m.name AS member_name " +
				"FROM books b LEFT JOIN members m ON b.member_id = m.id";

		List<Book> books = new ArrayList<>();

		try (Connection conn = dataSource.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Book b = new Book();
				b.setTitle(rs.getString("title"));
				b.setAuthor(rs.getString("author"));
				b.setMemberName(rs.getString("member_name"));
				books.add(b);
			}

			return books;

		} catch (SQLException e) {
			e.printStackTrace();
			SQLExceptionTranslator.translateAndThrow(e);
			return books;
		}
	}
}
