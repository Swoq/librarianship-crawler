package com.swoqe.consumer.persistence;

import com.swoqe.consumer.persistence.entities.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorJpaRepository extends JpaRepository<AuthorEntity, UUID> {
    Optional<AuthorEntity> findByFullName(String fullName);
}
