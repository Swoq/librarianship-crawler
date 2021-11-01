package com.swoqe.consumer.persistence;

import com.swoqe.consumer.persistence.entities.AuthorEntity;
import com.swoqe.consumer.persistence.entities.BookEntity;
import com.swoqe.consumer.persistence.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookPersistenceService implements PersistenceService<BookEntity> {

    private final BookJpaRepository bookJpaRepository;
    private final AuthorJpaRepository authorJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public BookEntity persist(BookEntity bookEntity) {
        List<AuthorEntity> authors = bookEntity.getAuthorEntities().stream()
                .map(authorEntity -> authorJpaRepository
                        .findByFullName(authorEntity.getFullName())
                        .orElseGet(() -> {
                            authorEntity.setId(UUID.randomUUID());
                            return authorEntity;
                        }))
                .collect(Collectors.toList());

        List<CategoryEntity> categories = bookEntity.getCategoryEntities().stream()
                .map(categoryEntity -> categoryJpaRepository
                        .findByName(categoryEntity.getName())
                        .orElseGet(() -> {
                            categoryEntity.setId(UUID.randomUUID());
                            return categoryEntity;
                        }))
                .collect(Collectors.toList());

        UUID bookId = bookJpaRepository.findByApiId(bookEntity.getApiId())
                .map(BookEntity::getId)
                .orElseGet(UUID::randomUUID);

        bookEntity.setId(bookId);
        bookEntity.setCategoryEntities(categories);
        bookEntity.setAuthorEntities(authors);

        return bookJpaRepository.save(bookEntity);
    }

    @Override
    public Iterable<BookEntity> persistAll(Iterable<BookEntity> entities) {
        return bookJpaRepository.saveAll(entities);
    }
}
