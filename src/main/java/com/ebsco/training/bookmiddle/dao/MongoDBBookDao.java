package com.ebsco.training.bookmiddle.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ebsco.training.bookmiddle.dto.BookDto;
import com.ebsco.training.bookmiddle.repository.BookRepository;

@Repository("MongoDBBookDao")
public class MongoDBBookDao implements BookDao {
    
    @Autowired
    private BookRepository repository;

    /* (non-Javadoc)
     * @see com.ebsco.training.bookmiddle.dao.BookDao#getBooks()
     */
    @Override
    public List<BookDto> getBooks() {
        return repository.find();
    }

    /* (non-Javadoc)
     * @see com.ebsco.training.bookmiddle.dao.BookDao#getBookById(java.lang.String)
     */
    @Override
    public Optional<BookDto> getBookById(String id) {
        return Optional.of(repository.findById(id));
    }

    /* (non-Javadoc)
     * @see com.ebsco.training.bookmiddle.dao.BookDao#deleteBook(java.lang.String)
     */
    @Override
    public Optional<BookDto> deleteBook(String id) {
        BookDto result = repository.findById(id);
        if (result != null) {
            repository.delete(result);
        }
        return Optional.of(result);
    }

    /* (non-Javadoc)
     * @see com.ebsco.training.bookmiddle.dao.BookDao#createBook(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public BookDto createBook(String title, String author, String genre) {
        BookDto result = new BookDto(null, title, author, genre);
        repository.insert(result);
        
        return result;
    }

    /* (non-Javadoc)
     * @see com.ebsco.training.bookmiddle.dao.BookDao#updateBook(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Optional<BookDto> updateBook(String id, String title, String author, String genre) {
        BookDto result = repository.findById(id);
        if (result != null) {
            result.setTitle(title);
            result.setAuthor(author);
            result.setGenre(genre);
            
            repository.update(result);
        }
        return Optional.of(result);
    }
}