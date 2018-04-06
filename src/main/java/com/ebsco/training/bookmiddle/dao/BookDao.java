package com.ebsco.training.bookmiddle.dao;

import java.util.List;
import java.util.Optional;

import com.ebsco.training.bookmiddle.dto.BookDto;

public interface BookDao {

    List<BookDto> getBooks();

    Optional<BookDto> getBookById(String id);

    Optional<BookDto> deleteBook(String id);

    BookDto createBook(String title, String author, String genre);

    Optional<BookDto> updateBook(String id, String title, String author, String genre);

}
