package com.swoqe.data;

import lombok.Data;

import java.util.List;

@Data
public class VolumeInfo {
    private String title;
    private String subtitle;
    private List<String> authors;
    private String publisher;
    private String publishedDate;
    private String description;
    private List<IndustryIdentifier> industryIdentifiers;
    private int pageCount;
    private List<String> categories;
    private int averageRating;
    private int ratingsCount;
    private ImageLinks imageLinks;
    private String language;
}

@Data
class IndustryIdentifier {
    private String type;
    private String identifier;

}

@Data
class ImageLinks {
    private String smallThumbnail;
    private String thumbnail;
}
