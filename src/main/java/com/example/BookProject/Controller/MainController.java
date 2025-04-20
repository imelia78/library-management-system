package com.example.BookProject.Controller;


import com.example.BookProject.Model.Book;
import com.example.BookProject.Service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
/*
  * @author Ilia Melia
 */

@RestController // (Controller + Response Body)  Даёт автоматическую сериаизацию
@RequestMapping("/api/v1/books")
@AllArgsConstructor

public class MainController {

    private final BookService bookService;


    @GetMapping
    public List<Book> findAllBooks() {
        return bookService.findAllBooks();
    }



    @GetMapping("/by-isbn/{ISBN}")
    public Book giveBookByISBN(@PathVariable Long ISBN) { // Для добавления опшионал в сигнатруру( и только в сигнатру) мне нужно изменить интерфейс BookService!!
        return bookService.findByISBN(ISBN);
    }

    @GetMapping("/by-name/{name}")
    public Book giveBookByName(@PathVariable String name){
        return bookService.findByName(name);
    }

    @PostMapping("/save_book")
    Book addBook(@RequestBody Book book){ //Сериализация запроса в JSON
        return bookService.saveBook(book);
    }

    @PutMapping("/update_book")
    Book updateBook(@RequestBody Book book){
        return bookService.updateBook(book);
    }

    @DeleteMapping("/delete_book/{ISBN}")
    void deleteBook(@PathVariable Long ISBN){
        bookService.deleteBook(ISBN);
    }

}
