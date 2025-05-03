package com.example.BookProject.Repository;

import com.example.BookProject.Model.Book;
import com.example.BookProject.Model.BookTopic;
import com.example.BookProject.Model.ReaderLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> { // Удобство в том, что все базовые методы CRUD уже реализованы в нём

    Optional<Book> findByTitle(@NotBlank(message = "book title is mandatory") String title);

    Optional<Book> findByIsbn(@NotBlank @Size(min = 10, max = 13) String isbn);

    List<Book> findBooksByReaderLevel(@NotNull ReaderLevel readerLevel);// in case of null returns Collections.emptyList()

    @Query("SELECT b FROM Book b JOIN b.bookTopicList list WHERE list.topic = :topic")
    List<Book> findBooksByBookTopic(@Param("topic") String topic);  //The @Param("topic") annotation associates a method parameter (String topic) with that named parameter in JPQL
}
