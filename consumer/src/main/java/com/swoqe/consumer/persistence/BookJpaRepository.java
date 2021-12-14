package com.swoqe.consumer.persistence;

import com.swoqe.consumer.persistence.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookJpaRepository extends JpaRepository<BookEntity, UUID> {
    Optional<BookEntity> findByApiId(String apiId);

}
