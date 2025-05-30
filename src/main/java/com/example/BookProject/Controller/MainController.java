package com.example.BookProject.Controller;


import com.example.BookProject.Model.Book;
import com.example.BookProject.Model.MyUser;
import com.example.BookProject.Model.ReaderLevel;
import com.example.BookProject.Service.BookService;
import com.example.BookProject.Service.MyUserDetailsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
/*
 * @author Ilia Melia
 */

@RestController // (Controller + Response Body)  Даёт автоматическую сериализацию
@RequestMapping("/api/v1/books")
@AllArgsConstructor
@Validated
public class MainController {

    private final BookService bookService;
    private final MyUserDetailsService myUserDetailsService;
    private final MessageSource messageSource;


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks() {
        List<Book> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/by-id/{id}")
    public ResponseEntity<Book> giveBookById(@PathVariable Long id) { //
        Book book = bookService.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);

    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("by-level/{level}")
    public ResponseEntity<List<Book>> giveBooksByLevel(@PathVariable ReaderLevel level) {
        List<Book> booksByLevelList = bookService.findBooksByLevel(level);
        return ResponseEntity.ok(booksByLevelList);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/by-isbn/{isbn}")
    public ResponseEntity<Book> giveBookByIsbn(@PathVariable String isbn) {
        Book book = bookService.findByIsbn(isbn);
        return ResponseEntity.ok(book);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/by-title/{title}")
    public ResponseEntity<Book> giveBookByTitle(@PathVariable @NotBlank String title) {
        Book book = bookService.findByTitle(title);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/by-topic/{topic}")
    public ResponseEntity<List<Book>> giveBooksByBookTopic(@PathVariable String topic) {
        List<Book> booksByBookTopicList = bookService.findBooksByBookTopic(topic);
        return ResponseEntity.ok(booksByBookTopicList);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save_book")
    public ResponseEntity<String> addBook(@Valid @RequestBody Book book, Locale locale) { //Сериализация запроса в JSON
        bookService.saveBook(book);
        String localizedMessage = messageSource.getMessage("book.saved", new Object[]{book.getTitle()}, locale);
        return ResponseEntity.ok(localizedMessage);
    }


    @PostMapping("/new_user") // from postman
    public ResponseEntity<String> addUser(@Valid @RequestBody MyUser user, Locale locale) {
        bookService.saveUser(user);
        String localizedMessage = messageSource.getMessage("user.saved", new Object[]{user.getName()}, locale);
        return ResponseEntity.ok(localizedMessage);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update_book")
    public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(book);
        return ResponseEntity.ok(updatedBook);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete_book/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id, Locale locale) {
       Book book = bookService.findById(id);
        if (book != null) {
            bookService.deleteBook(id);
            String localizedMessage = messageSource.getMessage("book.deleted", new Object[]{id}, locale);
            return ResponseEntity.ok(localizedMessage);
        } else {
            String localizedMessage = messageSource.getMessage("book.unfound", new Object[]{id}, locale);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(localizedMessage);
        }
    }
}

