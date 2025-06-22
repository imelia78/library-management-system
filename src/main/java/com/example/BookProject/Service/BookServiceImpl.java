package com.example.BookProject.Service;

import com.example.BookProject.DTO.BookDTO;
import com.example.BookProject.Model.Book;
import com.example.BookProject.Model.MyUser;
import com.example.BookProject.Model.ReaderLevel;
import com.example.BookProject.Repository.BookRepository;
import com.example.BookProject.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor// В т.ч. Для внедрения через конструктор полей bookRepository
@Validated


public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;// DI via @AllArgsConstructor
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;// hiding password
    private final BookDTOMapper bookDTOMapper;


    @Override
    public List<BookDTO> findAllBooks() {
        return bookRepository.findAll().stream().map(bookDTOMapper).toList();
    }

    @Override
    public List<BookDTO> findBooksByLevel(ReaderLevel readerLevel) {
        return bookRepository.findBooksByReaderLevel(readerLevel).stream().map(bookDTOMapper).toList();
    }

    @Override
    public List<BookDTO> findBooksByBookTopic(String topic) {

        return bookRepository.findBooksByBookTopic(topic).stream().map(bookDTOMapper).toList();
    }

    @Override
    public BookDTO saveBook(BookDTO bookDTO) {
        Book book = bookDTOMapper.fromDTO(bookDTO);

       Book saved = bookRepository.save(book);
        log.info("Book '{}' was saved to the database", saved.getTitle());
        return bookDTOMapper.apply(saved);
    }

    @Override
    public BookDTO findByTitle(String title) {
        log.debug("Fetching book with Title: {}", title);
        Book book = bookRepository.findByTitle(title)
                .orElseThrow(() -> {
                    log.error("Book with Title {} not found", title);
                    return new EntityNotFoundException("Book not found: id = " + title);
                });
        return bookDTOMapper.apply(book);
    }

    @Override
    public BookDTO findByIsbn(String isbn) {
        log.debug("Fetching book with ISBN: {}", isbn);
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> {
                    log.error("Book with ISBN {} not found", isbn);
                    return new EntityNotFoundException("Book not found: id = " + isbn);
                });
        return bookDTOMapper.apply(book);

    }

    @Override
    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);// Заменить на orElse без пробрасывания исклчений
        return bookDTOMapper.apply(book);
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO) {
        log.debug("Attempting to update book with ID: {}", bookDTO.id());
        Book existingBook = bookRepository.findById(bookDTO.id()).orElseThrow(() -> {
            log.error("Book with ID {} not found for update", bookDTO.id());
            return new EntityNotFoundException("Book with ID " + bookDTO.id() + " not found for update");
        });

        bookDTOMapper.updateEntityFromDTO(existingBook, bookDTO);
        Book savedBook = bookRepository.save(existingBook);
        log.info("Book '{}' (ID: {}) was updated successfully", savedBook.getTitle(), savedBook.getId());
        return bookDTOMapper.apply(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        log.debug("Attempting to delete book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            log.warn("Attempted to delete non-existent book with ID: {}", id);
        } else {
            bookRepository.deleteById(id);
            log.info("Book with ID: {} was deleted", id);
        }
    }

    @Override
    public void saveUser(MyUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        log.info("New user '{}' was registered", user.getName());
    }
}