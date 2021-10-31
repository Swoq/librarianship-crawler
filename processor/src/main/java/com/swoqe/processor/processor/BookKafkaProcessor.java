package com.swoqe.processor.processor;

import com.swoqe.data.Book;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@Slf4j
public class BookKafkaProcessor {

    @Bean
    public Function<KStream<String, Book>, KStream<String, Book>> bookProcessor() {
        return kstream -> kstream.filter((key, book) -> {
            log.info("Filtering: [{}] id: [{}]", book.getVolumeInfo().getTitle(), book.getId());
            return true;
        });

    }
}