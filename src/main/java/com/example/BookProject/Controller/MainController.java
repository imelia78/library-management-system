package com.example.BookProject.Controller;


import com.example.BookProject.DTO.BookDTO;
import com.example.BookProject.Model.MyUser;
import com.example.BookProject.Model.ReaderLevel;
import com.example.BookProject.Service.BookDTOMapper;
import com.example.BookProject.Service.BookService;
import com.example.BookProject.Service.MyUserDetailsService;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    private final BookDTOMapper bookDTOMapper;


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookDTO>> findAllBooks() {
        List<BookDTO> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/by-id/{id}")
    public ResponseEntity<BookDTO> giveBookById(@PathVariable Long id) {
        BookDTO book = bookService.findById(id);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        return ResponseEntity.ok(book);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("by-level/{level}")
    public ResponseEntity<List<BookDTO>> giveBooksByLevel(@PathVariable ReaderLevel level) {
        List<BookDTO> booksByLevelList = bookService.findBooksByLevel(level);
        return ResponseEntity.ok(booksByLevelList);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/by-isbn/{isbn}")
    public ResponseEntity<BookDTO> giveBookByIsbn(@PathVariable String isbn) {
        BookDTO book = bookService.findByIsbn(isbn);
        return ResponseEntity.ok(book);
    }


    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/by-title/{title}")
    public ResponseEntity<BookDTO> giveBookByTitle(@PathVariable @NotBlank String title) {
        BookDTO book = bookService.findByTitle(title);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        return ResponseEntity.ok(book);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/by-topic/{topic}")
    public ResponseEntity<List<BookDTO>> giveBooksByBookTopic(@PathVariable String topic) {
        List<BookDTO> booksByBookTopicList = bookService.findBooksByBookTopic(topic);
        return ResponseEntity.ok(booksByBookTopicList);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save_book")
    public ResponseEntity<BookDTO> saveBook(@Valid @RequestBody BookDTO dto, Locale locale) { //Сериализация запроса в JSON
        BookDTO saved = bookService.saveBook(dto);
        String localizedMessage = messageSource.getMessage("book.saved", new Object[]{dto.title()}, locale);
        //  return ResponseEntity.ok(localizedMessage);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("X-Message", messageSource.getMessage("book.saved", new Object[]{dto.title()}, locale))
                .body(saved);
//  add HTTP status -> CREATED
    }


    @PostMapping("/new_user") // from postman
    public ResponseEntity<String> addUser(@Valid @RequestBody MyUser user, Locale locale) {
        bookService.saveUser(user);
        String localizedMessage = messageSource.getMessage("user.saved", new Object[]{user.getName()}, locale);
        return ResponseEntity.ok(localizedMessage);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update_book")
    public ResponseEntity<BookDTO> updateBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO updatedBook = bookService.updateBook(bookDTO);
        return ResponseEntity.ok(updatedBook);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete_book/{id}")  // PathVariable from url!
    public ResponseEntity<String> deleteBook(@PathVariable Long id, Locale locale) {
        BookDTO book = bookService.findById(id);
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

