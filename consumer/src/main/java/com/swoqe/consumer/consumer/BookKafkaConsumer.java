package com.swoqe.consumer.consumer;

import com.swoqe.consumer.persistence.BookPersistenceService;
import com.swoqe.data.Book;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
            Book persisted = persistenceService.persist(book);
            log.info("Persisted: [{}] id: [{}]", persisted.getVolumeInfo().getTitle(), persisted.getId());
        });
    }
}