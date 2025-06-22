package com.example.BookProject.Service;

import com.example.BookProject.DTO.BookDTO;
import com.example.BookProject.Model.Author;
import com.example.BookProject.Model.Book;
import com.example.BookProject.Model.BookTopic;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class BookDTOMapper implements Function<Book, BookDTO> {
    @Override
    public BookDTO apply(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPages(),
                book.getTotalCopies(),
                book.getAvailableCopies(),


                book.getReaderLevel(),
                book.getBookTopicList().stream().map(BookTopic::getTopic).toList(),

                book.getAuthorSet().stream().map(Author::getName).collect(Collectors.toSet())
        );
    }

    public Book fromDTO(BookDTO bookDTO) {
        Book book = new Book();
        if (bookDTO.id() != null) {
            book.setId(bookDTO.id());
        }
        book.setTitle(bookDTO.title());
        book.setIsbn(bookDTO.isbn());
        book.setPages(bookDTO.pages());
        book.setTotalCopies(bookDTO.totalCopies());
        book.setAvailableCopies(bookDTO.availableCopies());
        book.setReaderLevel(bookDTO.readerLevel());
        List<BookTopic> topicList = bookDTO.topics().stream()
                .map(topic -> {
                    BookTopic bookTopic = new BookTopic();
                    bookTopic.setTopic(topic);
                    bookTopic.setBook(book);
                    return bookTopic;
                })
                .collect(Collectors.toList());
        book.setBookTopicList(topicList);

        Set<Author> authorSet = bookDTO.authors().stream()
                .map(name -> {
                    Author author = new Author();
                    author.setName(name);
                    return author;
                })
                .collect(Collectors.toSet());
        book.setAuthorSet(authorSet);

        return book;

    }

    public void updateEntityFromDTO(Book book, BookDTO bookDTO) {
        book.setTitle(bookDTO.title());
        book.setIsbn(bookDTO.isbn());
        book.setPages(bookDTO.pages());
        book.setTotalCopies(bookDTO.totalCopies());
        book.setAvailableCopies(bookDTO.availableCopies());
        book.setReaderLevel(bookDTO.readerLevel());

        // Обновляем темы (пересоздаём список и связываем)
        List<BookTopic> topicList = bookDTO.topics().stream()
                .map(topic -> {
                    BookTopic bookTopic = new BookTopic();
                    bookTopic.setTopic(topic);
                    bookTopic.setBook(book);
                    return bookTopic;
                })
                .collect(Collectors.toCollection(ArrayList::new));
        book.setBookTopicList(topicList);

        // Обновляем авторов
        Set<Author> authorSet = bookDTO.authors().stream()
                .map(name -> {
                    Author author = new Author();
                    author.setName(name);
                    return author;
                })
                .collect(Collectors.toCollection(HashSet::new));
        book.setAuthorSet(authorSet);
    }
}
