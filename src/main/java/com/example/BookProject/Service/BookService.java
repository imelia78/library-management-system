package com.example.BookProject.Service;

import com.example.BookProject.DTO.BookDTO;
import com.example.BookProject.Model.MyUser;
import com.example.BookProject.Model.ReaderLevel;

import java.util.List;


public interface BookService {


    List<BookDTO> findAllBooks();

    List<BookDTO> findBooksByLevel(ReaderLevel readerLevel);

    List<BookDTO> findBooksByBookTopic(String topic);

    BookDTO saveBook(BookDTO bookDTO);

    BookDTO findByTitle(String title);

    BookDTO findByIsbn(String isbn);

    BookDTO findById(Long id);

    BookDTO updateBook(BookDTO bookDTO);

    void deleteBook(Long id);

    void saveUser(MyUser user);


}
