package com.example.BookProject.Model;


import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "books")


public class Book {
    @Id
    private  Long ISBN;

    @Column(nullable = false)
    @NotBlank(message = "book title is mandatory")
     private String title;

    @Column(nullable = false)
    @NotBlank( message = "book should not be anonymous")
    private String author;

    @Min(value = 1, message = "Page count must be at least 1")
    private int pages;
}
