package com.swoqe.consumer.persistence.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "crawler_book")
@Data
public class BookEntity {
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;
    private String apiId;
    private String title;

    @Column(columnDefinition = "text")
    private String subtitle;

    private String publisher;
    private String publishedDate;

    @Column(columnDefinition = "text")
    private String description;

    private int pageCount;
    private int averageRating;
    private int ratingsCount;
    private String smallThumbnail;
    private String thumbnail;
    private String language;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "crawler_book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<AuthorEntity> authorEntities;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "crawler_book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<CategoryEntity> categoryEntities;
}

