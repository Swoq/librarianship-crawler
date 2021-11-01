package com.swoqe.crawler.service;

import com.swoqe.data.Book;
import com.swoqe.data.BookPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@ConditionalOnProperty(
        value = "crawler.enabled",
        havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class BookControllerCrawler {

    @Value("${google.api_uri}")
    private String GOOGLE_API_URI;

    @Value("${google.api_key}")
    private String apiKey;

    @Value("${kafka.out_topic}")
    private String KAFKA_TOPIC;

    private final KafkaTemplate<String, Book> kafkaTemplate;

    @GetMapping("/crawl")
    @ResponseStatus(HttpStatus.OK)
    public void run(
            @RequestParam String q,
            @RequestParam(required = false) Optional<String> printType,
            @RequestParam(required = false) Optional<String> startIndex,
            @RequestParam(required = false) Optional<String> maxResults

    ) {
        StringBuilder uriBuilder = new StringBuilder(GOOGLE_API_URI)
                .append("?q=").append(q)
                .append("&key=").append(apiKey);
        printType.ifPresent(value -> uriBuilder.append("&printType=").append(value));
        startIndex.ifPresent(value -> uriBuilder.append("&startIndex=").append(value));
        maxResults.ifPresent(value -> uriBuilder.append("&maxResults=").append(value));

        Mono<BookPage> booksMono = WebClient.create()
                .get()
                .uri(uriBuilder.toString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BookPage.class);

        booksMono.subscribe(bookPage -> bookPage.getItems().forEach(book -> {
            kafkaTemplate.send(KAFKA_TOPIC, book);
            log.info("Published: [{}] id: [{}]", book.getVolumeInfo().getTitle(), book.getId());
        }));
    }
}