package com.swoqe.data;

import lombok.Data;

import java.util.List;

@Data
public class BookPage {
    private String kind;
    private int totalItems;
    private List<Book> items;
}