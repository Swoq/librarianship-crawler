package com.swoqe.consumer.persistence;

import com.swoqe.data.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookJpaRepository extends JpaRepository<Book, UUID> {
}
