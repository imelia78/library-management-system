package com.example.BookProject.Service;

import com.example.BookProject.Model.Book;
import com.example.BookProject.Model.MyUser;
import com.example.BookProject.Model.ReaderLevel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {


    List<Book> findAllBooks();

    List<Book> findBooksByLevel(ReaderLevel readerLevel);

    List<Book> findBooksByBookTopic(String topic);

    void saveBook(Book book);

    Book findByTitle(String title);

    Book findByIsbn(String isbn);

    Book findById(Long  id);

    Book updateBook(Book book);

    void deleteBook(Long id);

    void saveUser(MyUser user);


}
