package com.example.BookProject.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "books")


public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 10 , max = 13)
    private String isbn;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private  ReaderLevel readerLevel;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BookTopic> bookTopicList;


    @Column(nullable = false)
    @NotNull(message = "book title is mandatory")
     private String title;



    @Min(value = 1, message = "Page count must be at least 1")
    private int pages;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable( // creating intermediate table,which will managed by hibernate
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnoreProperties("books") //avoid recursion
    private Set<Author> authorSet = new HashSet<>();
}
