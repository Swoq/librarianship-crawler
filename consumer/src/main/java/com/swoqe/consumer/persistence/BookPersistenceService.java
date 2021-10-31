package com.swoqe.consumer.persistence;

import com.swoqe.data.Book;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@AllArgsConstructor
public class BookPersistenceService implements PersistenceService<Book> {

    private final BookJpaRepository bookJpaRepository;

    @Override
    public Book persist(Book entity) {
        return bookJpaRepository.save(entity);
    }

    @Override
    public Iterable<Book> persistAll(Iterable<Book> entities) {
        return bookJpaRepository.saveAll(entities);
    }
}
