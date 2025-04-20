package com.example.BookProject.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Setter
@Getter


@Entity
@Table(name = "Books")
public class Book {
    @Id
    Long ISBN;
    @Column(nullable = false)
    String name;

    String author;

    int pages;

    public Book(String author, Long ISBN, String name, int pages) {
        this.author = author;
        this.ISBN = ISBN;
        this.name = name;
        this.pages = pages;
    }
}
