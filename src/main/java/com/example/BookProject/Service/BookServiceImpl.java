package com.example.BookProject.Service;

import com.example.BookProject.Model.Book;
import com.example.BookProject.Model.BookTopic;
import com.example.BookProject.Model.MyUser;
import com.example.BookProject.Model.ReaderLevel;
import com.example.BookProject.Repository.BookRepository;
import com.example.BookProject.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor// В т.ч. Для внедрения через конструктор полей bookRepository
@Validated


public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;// DI via @AllArgsConstructor
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // hiding password

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findBooksByLevel(ReaderLevel readerLevel) {
        return bookRepository.findBooksByReaderLevel(readerLevel);
    }

    @Override
    public List<Book> findBooksByBookTopic(String topic) {
        return bookRepository.findBooksByBookTopic(topic);
    }

    @Override
    public void saveBook(Book book) {

        if (book.getBookTopicList() != null) {
            for (BookTopic topic : book.getBookTopicList()) {
                topic.setBook(book);
            }
        }
        bookRepository.save(book);
    }

        @Override
        public Book findByTitle(String title){
            return bookRepository.findByTitle(title).orElseThrow(EntityNotFoundException::new);
        }

    @Override
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(EntityNotFoundException::new);
    }

    @Override
        public Book findById(Long id){
            return bookRepository.findById(id).orElseThrow(EntityNotFoundException::new); // Заменить на orElse без пробрасывания исклчений
        }

        @Override
        public Book updateBook(Book book){
            return bookRepository.save(book);
        }

        @Override
        public void deleteBook (Long id){
            bookRepository.deleteById(id);
        }

    @Override
    public void saveUser(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}