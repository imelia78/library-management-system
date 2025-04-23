package com.example.BookProject.Controller;


import com.example.BookProject.Model.Book;
import com.example.BookProject.Service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
  * @author Ilia Melia
 */

@RestController // (Controller + Response Body)  Даёт автоматическую сериализацию
@RequestMapping("/api/v1/books")
@AllArgsConstructor
@Validated
public class MainController {

    private final BookService bookService;


    @GetMapping
    public ResponseEntity<List<Book>> findAllBooks() {
        List<Book> books =  bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/by-isbn/{ISBN}")
    public  ResponseEntity<Book> giveBookByISBN(@PathVariable  Long ISBN) { //
        Book book = bookService.findByISBN(ISBN);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);

    }

    @GetMapping("/by-title/{title}")
    public ResponseEntity<Book>  giveBookByTitle( @PathVariable @NotBlank String title){
            Book book =  bookService.findByTitle(title);
            if (book == null) {
            return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(book);
    }

    @PostMapping("/save_book")
   public ResponseEntity<String> addBook(@Valid @RequestBody Book book){ //Сериализация запроса в JSON
        bookService.saveBook(book);
        return ResponseEntity.ok("The book " + book.getTitle() +  " been saved successfully!");
    }

    @PutMapping("/update_book")
   public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book){
      Book updatedBook =  bookService.updateBook(book);
      return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/delete_book/{ISBN}")
   public ResponseEntity<String> deleteBook(@PathVariable  Long ISBN){
        bookService.deleteBook(ISBN);
        return ResponseEntity.ok("The book with this ISBN: " + ISBN + " has been successfully deleted!");
    }

}
