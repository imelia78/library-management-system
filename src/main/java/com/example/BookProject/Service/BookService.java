package com.example.BookProject.Service;

import com.example.BookProject.Model.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {

    List<Book> findAllBooks();

    void saveBook(Book book);

    Book findByTitle(String title);

    Book findByISBN(Long ISBN);

    Book updateBook(Book book);

    void deleteBook(Long ISBN);
}
