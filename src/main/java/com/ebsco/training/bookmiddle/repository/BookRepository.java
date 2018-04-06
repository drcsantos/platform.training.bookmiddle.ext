package com.ebsco.training.bookmiddle.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

import com.ebsco.training.bookmiddle.dto.BookDto;

@Repository
public class BookRepository extends TrackableRepository<BookDto> {

    public static String COLLECTION = "books";

    @Value("${sqs.queueUrl}")
    private String queueUrl;

    @Autowired
    private MongoOperations mongoOperation;

    @Override
    protected String getAWSQueueUrl() {

        return queueUrl;
    }

    @Override
    protected BookDto insertData(BookDto value) {

        mongoOperation.insert(value, BookRepository.COLLECTION);
        return value;
    }

    @Override
    protected BookDto updateData(BookDto value) {

        mongoOperation.save(value, BookRepository.COLLECTION);
        return value;
    }

    @Override
    protected BookDto deleteData(BookDto value) {

        mongoOperation.remove(value, BookRepository.COLLECTION);
        return value;
    }

    @Override
    public BookDto findById(String id) {

        return mongoOperation.findById(id, BookDto.class, BookRepository.COLLECTION);
    }

    @Override
    public List<BookDto> find() {

        return mongoOperation.findAll(BookDto.class, BookRepository.COLLECTION);
    }

}
