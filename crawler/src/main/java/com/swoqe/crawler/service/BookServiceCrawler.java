package com.swoqe.crawler.service;

import com.swoqe.data.Book;
import com.swoqe.data.BookPage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@ConditionalOnProperty(
        value = "crawler.enabled",
        havingValue = "true")
@AllArgsConstructor
@Slf4j
public class BookServiceCrawler implements CommandLineRunner {

    private final String KAFKA_TOPIC = "raw-books";
    private final String GOOGLE_API_URI = "https://www.googleapis.com/books/v1/volumes?q=flowers+inauthor:keyes&key=AIzaSyASm03-9koQgA7fPoyEZ4D7VHdAwtKE7jk&printType=books&startIndex=0&maxResults=10";
    private KafkaTemplate<String, Book> kafkaTemplate;

    @Override
    public void run(String... args) {
        Mono<BookPage> booksMono = WebClient.create()
                .get()
                .uri(GOOGLE_API_URI)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BookPage.class);

        booksMono.subscribe(bookPage -> bookPage.getItems().forEach(book -> {
            kafkaTemplate.send(KAFKA_TOPIC, book);
            log.info("Published: [{}] id: [{}]", book.getVolumeInfo().getTitle(), book.getId());
        }));
    }
}