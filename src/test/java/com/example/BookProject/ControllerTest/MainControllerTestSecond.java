package com.example.BookProject.ControllerTest;

import com.example.BookProject.Controller.MainController;
import com.example.BookProject.DTO.BookDTO;
import com.example.BookProject.Model.ReaderLevel;
import com.example.BookProject.Model.MyUser;
import com.example.BookProject.Service.BookDTOMapper;
import com.example.BookProject.Service.BookService;
import com.example.BookProject.Service.MyUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MainControllerTestSecond {
    @Mock
    private BookService bookService;
    @Mock
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private MessageSource messageSource;
    @Mock
    private BookDTOMapper bookDTOMapper;

    @InjectMocks
    private MainController mainController;


    private BookDTO mockBook() {
        return new BookDTO(1L, "Book Title", "1234567890", 100, 5, 3,
                ReaderLevel.ADVANCED, List.of("Algorithms"), Collections.emptySet());
    }

    @Test
    void findAllBooks_returnsBooks() {
        List<BookDTO> books = List.of(mockBook());
        when(bookService.findAllBooks()).thenReturn(books);

        ResponseEntity<List<BookDTO>> response = mainController.findAllBooks();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(books, response.getBody());
    }

    @Test
    void giveBookById_returnsBook() {
        BookDTO book = mockBook();
        when(bookService.findById(1L)).thenReturn(book);

        ResponseEntity<BookDTO> response = mainController.giveBookById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void giveBookById_notFound() {
        when(bookService.findById(99L)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> mainController.giveBookById(99L));
    }

    @Test
    void giveBooksByLevel_returnsList() {
        when(bookService.findBooksByLevel(ReaderLevel.ADVANCED)).thenReturn(List.of(mockBook()));

        ResponseEntity<List<BookDTO>> response = mainController.giveBooksByLevel(ReaderLevel.ADVANCED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void giveBookByIsbn_returnsBook() {
        when(bookService.findByIsbn("1234567890")).thenReturn(mockBook());

        ResponseEntity<BookDTO> response = mainController.giveBookByIsbn("1234567890");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1234567890", response.getBody().isbn());
    }

    @Test
    void giveBookByTitle_found() {
        BookDTO book = mockBook();
        when(bookService.findByTitle("Book Title")).thenReturn(book);

        ResponseEntity<BookDTO> response = mainController.giveBookByTitle("Book Title");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void giveBookByTitle_notFound() {
        when(bookService.findByTitle("Unknown")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> mainController.giveBookByTitle("Unknown"));
    }

    @Test
    void giveBooksByBookTopic_returnsList() {
        List<BookDTO> list = List.of(mockBook());
        when(bookService.findBooksByBookTopic("Algorithms")).thenReturn(list);

        ResponseEntity<List<BookDTO>> response = mainController.giveBooksByBookTopic("Algorithms");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void saveBook_returnsCreated() {
        BookDTO dto = mockBook();
        when(bookService.saveBook(dto)).thenReturn(dto);
        when(messageSource.getMessage(eq("book.saved"), any(), any())).thenReturn("Book saved");

        ResponseEntity<BookDTO> response = mainController.saveBook(dto, Locale.ENGLISH);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dto, response.getBody());
        assertEquals("Book saved", response.getHeaders().getFirst("X-Message"));
        verify(bookService).saveBook(dto);
    }

    @Test
    void addUser_returnsLocalizedMessage() {
        MyUser user = new MyUser();
        user.setName("Alice");

        doNothing().when(bookService).saveUser(user);
        when(messageSource.getMessage(eq("user.saved"), any(), any())).thenReturn("User saved: Alice");

        ResponseEntity<String> response = mainController.addUser(user, Locale.ENGLISH);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User saved: Alice", response.getBody());
    }

    @Test
    void updateBook_returnsUpdatedBook() {
        BookDTO book = mockBook();
        when(bookService.updateBook(book)).thenReturn(book);

        ResponseEntity<BookDTO> response = mainController.updateBook(book);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
    }

    @Test
    void deleteBook_successful() {
        BookDTO book = mockBook();
        when(bookService.findById(1L)).thenReturn(book);
        when(messageSource.getMessage(eq("book.deleted"), any(), any())).thenReturn("Book deleted: 1");

        ResponseEntity<String> response = mainController.deleteBook(1L, Locale.ENGLISH);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book deleted: 1", response.getBody());
        verify(bookService).deleteBook(1L);
    }

    @Test
    void deleteBook_notFound() {
        when(bookService.findById(999L)).thenReturn(null);
        when(messageSource.getMessage(eq("book.unfound"), any(), any())).thenReturn("Book not found: 999");

        ResponseEntity<String> response = mainController.deleteBook(999L, Locale.ENGLISH);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book not found: 999", response.getBody());
        verify(bookService, never()).deleteBook(anyLong());
    }
}
