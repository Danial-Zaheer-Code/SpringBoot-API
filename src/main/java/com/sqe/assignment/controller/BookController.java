package com.sqe.assignment.controller;

import com.sqe.assignment.model.Book;
import com.sqe.assignment.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@PostMapping
	public ResponseEntity<Book> createBook(@RequestBody Book book) {
		bookService.createBook(book);
		return ResponseEntity.ok(book);  // FIX: return the original object
	}

	@PutMapping("/{title}")
	public ResponseEntity<Book> updateBook(@PathVariable String title, @RequestBody Book book) {
		Book updated = bookService.updateBook(title, book);

		if (updated == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{title}")
	public ResponseEntity<Void> deleteBook(@PathVariable String title) {
		boolean deleted = bookService.deleteBook(title);

		if (!deleted)
			return ResponseEntity.notFound().build();

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{title}")
	public ResponseEntity<Book> getBook(@PathVariable String title) {
		Book book = bookService.getBookByTitle(title);

		if (book == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(book);
	}

	@GetMapping
	public ResponseEntity<List<Book>> getAllBooks() {
		return ResponseEntity.ok(bookService.getAllBooks());
	}
}
