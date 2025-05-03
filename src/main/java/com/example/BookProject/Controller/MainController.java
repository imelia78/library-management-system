package com.example.BookProject.Controller;


import com.example.BookProject.Model.Book;
import com.example.BookProject.Model.ReaderLevel;
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

    @GetMapping("/by-id/{id}")
    public  ResponseEntity<Book> giveBookById(@PathVariable  Long id) { //
        Book book = bookService.findById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);

    }

    @GetMapping("by-level/{level}")
    public ResponseEntity<List<Book>> giveBooksByLevel(@PathVariable ReaderLevel level){
        List<Book> booksByLevelList = bookService.findBooksByLevel(level);
        return ResponseEntity.ok(booksByLevelList);
    }

    @GetMapping("/by-isbn/{isbn}")
    public ResponseEntity<Book> giveBookByIsbn(@PathVariable String isbn){
        Book book = bookService.findByIsbn(isbn);
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

    @GetMapping("/by-topic/{topic}")
    public ResponseEntity<List<Book>> giveBooksByBookTopic(@PathVariable String topic){
     List<Book> booksByBookTopicList = bookService.findBooksByBookTopic(topic);
     return ResponseEntity.ok(booksByBookTopicList);
    }


    @PostMapping("/save_book")
   public ResponseEntity<String> addBook(@Valid @RequestBody Book book){ //Сериализация запроса в JSON
        bookService.saveBook(book);
        return ResponseEntity.ok(STR."The book \{book.getTitle()} has been saved successfully!");
    }

    @PutMapping("/update_book")
   public ResponseEntity<Book> updateBook(@Valid @RequestBody Book book){
      Book updatedBook =  bookService.updateBook(book);
      return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/delete_book/{id}")
   public ResponseEntity<String> deleteBook(@PathVariable  Long id){
        bookService.deleteBook(id);
        return ResponseEntity.ok(STR."The book with  id:\{id}  has been successfully deleted!");
    }

}
