package com.sqe.assignment.service;

import com.sqe.assignment.model.Book;

import java.util.List;

public interface BookService {


	Book updateBook(String title, Book book);

	Book createBook(Book book);

	boolean deleteBook(String title);

	Book getBookByTitle(String title);

	List<Book> getAllBooks();
}
