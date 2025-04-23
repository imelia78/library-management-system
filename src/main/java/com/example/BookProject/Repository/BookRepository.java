package com.example.BookProject.Repository;

import com.example.BookProject.Model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> { // Удобство в том, что все базовые методы CRUD уже реализованы в нём


    Optional<Book> findByTitle(String title);

    Optional<Book> findByISBN(Long ISBN);
}
