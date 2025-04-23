package com.example.BookProject.Service;

import com.example.BookProject.Model.Book;
import com.example.BookProject.Repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor// В т.ч. Для внедрения через конструктор полей bookRepository
@Validated


public class BookServiceImpl implements BookService {


    private  final BookRepository bookRepository; // DI via @AllArgsConstructor



    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Book findByISBN(Long ISBN) {
        return bookRepository.findByISBN(ISBN).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long ISBN) {
        bookRepository.deleteById(ISBN);
    }
}