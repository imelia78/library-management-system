package com.example.BookProject.Model;
import jakarta.persistence.*;
import java.time.LocalDate;


/**
 * TODO: Will be used in future for handling book reservations.
 */

@Entity
public class BookRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private MyUser user;

    private LocalDate startDate;
    private LocalDate expirationDate;

    private Boolean returned;


}
