package com.swoqe.consumer.persistence.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "crawler_author")
@Data
public class AuthorEntity {
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;
    private String fullName;
}
