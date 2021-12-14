package com.swoqe.consumer.consumer;

import com.swoqe.consumer.persistence.BookPersistenceService;
import com.swoqe.consumer.persistence.entities.AuthorEntity;
import com.swoqe.consumer.persistence.entities.BookEntity;
import com.swoqe.consumer.persistence.entities.CategoryEntity;
import com.swoqe.data.Book;
import com.swoqe.data.ImageLinks;
import com.swoqe.data.VolumeInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Configuration
@Slf4j
@AllArgsConstructor
public class BookKafkaConsumer {

    private final BookPersistenceService persistenceService;

    @Bean
    public Consumer<KStream<String, Book>> bookService() {
        return kstream -> kstream.foreach((key, book) -> {
            log.info("Consumed: [{}] id: [{}]", book.getVolumeInfo().getTitle(), book.getId());
            try {
                BookEntity persisted = persistenceService.persist(convertToEntity(book));
                log.info("Persisted: [{}] id: [{}]", persisted.getTitle(), persisted.getId());
            } catch (Exception e) {
                log.error("Unable to persist: [{}]", book);
            }
        });
    }

    private BookEntity convertToEntity(Book book) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setApiId(book.getId());

        VolumeInfo volumeInfo = book.getVolumeInfo();
        if (volumeInfo != null) {
            bookEntity.setTitle(volumeInfo.getTitle());
            bookEntity.setSubtitle(volumeInfo.getSubtitle());
            bookEntity.setPublisher(volumeInfo.getPublisher());
            bookEntity.setPublishedDate(volumeInfo.getPublishedDate());
            bookEntity.setDescription(volumeInfo.getDescription());
            bookEntity.setPageCount(volumeInfo.getPageCount());
            bookEntity.setAverageRating(volumeInfo.getAverageRating());
            bookEntity.setRatingsCount(volumeInfo.getRatingsCount());
            bookEntity.setRatingsCount(volumeInfo.getRatingsCount());

            Optional.ofNullable(volumeInfo.getImageLinks()).ifPresent(imageLinks -> {
                bookEntity.setSmallThumbnail(imageLinks.getSmallThumbnail());
                bookEntity.setThumbnail(imageLinks.getThumbnail());
            });

            bookEntity.setLanguage(volumeInfo.getLanguage());

            Optional.ofNullable(volumeInfo.getAuthors()).ifPresent(authors -> authors.forEach(author -> {
                AuthorEntity a = new AuthorEntity();
                a.setFullName(author);
                List<AuthorEntity> authorEntities = Optional.ofNullable(bookEntity.getAuthorEntities())
                        .orElse(new ArrayList<>());
                authorEntities.add(a);
                bookEntity.setAuthorEntities(authorEntities);
            }));


            Optional.ofNullable(volumeInfo.getCategories()).ifPresent(categories -> categories.forEach(category -> {
                CategoryEntity c = new CategoryEntity();
                c.setName(category);
                List<CategoryEntity> categoryEntities = Optional.ofNullable(bookEntity.getCategoryEntities())
                        .orElse(new ArrayList<>());
                categoryEntities.add(c);
                bookEntity.setCategoryEntities(categoryEntities);
            }));
        }

        if (bookEntity.getCategoryEntities() == null)
            bookEntity.setCategoryEntities(new ArrayList<>());
        if (bookEntity.getAuthorEntities() == null)
            bookEntity.setAuthorEntities(new ArrayList<>());

        return bookEntity;
    }
}