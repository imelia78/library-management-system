package com.example.BookProject.ControllerTest;

import com.example.BookProject.DTO.BookDTO;
import com.example.BookProject.Model.ReaderLevel;
import com.example.BookProject.Service.BookService;
import com.example.BookProject.Service.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MainControllerTest {

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private MyUserDetailsService myUserDetailsService;

    @MockitoBean
    private MessageSource messageSource;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser
    void getBooks() throws Exception {
        mockMvc.perform(get("/api/v1/books")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getBookById() throws Exception {
        BookDTO mockBook = new BookDTO(1L, "Book Title", "1234567890", 100,
                5, 3, ReaderLevel.ADVANCED, Collections.emptyList(), Collections.emptySet());
        Mockito.when(bookService.findById(1L)).thenReturn(mockBook);
        mockMvc.perform(get("/api/v1/books/by-id/1")).andExpect(status().isOk());

    }


    @Test
    @WithMockUser
    void getBookByLevel() throws Exception {
        BookDTO mockBook = new BookDTO(1L, "Book Title", "1234567890", 100,
                5, 3, ReaderLevel.ADVANCED, Collections.emptyList(), Collections.emptySet());

        Mockito.when(bookService.findBooksByLevel(ReaderLevel.ADVANCED)).thenReturn(Collections.singletonList(mockBook));

        mockMvc.perform(get("/api/v1/books/by-level/ADVANCED")).andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void getBookByIsbn() throws Exception {
        BookDTO mockBook = new BookDTO(1L, "Book Title", "1234567890", 100,
                5, 3, ReaderLevel.ADVANCED, Collections.emptyList(), Collections.emptySet());

        Mockito.when(bookService.findByIsbn("1234567890")).thenReturn(mockBook);

        mockMvc.perform(get("/api/v1/books/by-isbn/1234567890")).andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void getBookByTitle() throws Exception {
        BookDTO mockBook = new BookDTO(1L, "Book Title", "1234567890", 100,
                5, 3, ReaderLevel.ADVANCED, Collections.emptyList(), Collections.emptySet());

        Mockito.when(bookService.findByTitle("Book Title")).thenReturn(mockBook);

        mockMvc.perform(get("/api/v1/books/by-title/Book Title")).andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void getBookByBookTopic() throws Exception {

        BookDTO mockBook = new BookDTO(1L, "Book Title", "1234567890", 100,
                5, 3, ReaderLevel.ADVANCED, List.of("Algorithms"), Collections.emptySet());


        Mockito.when(bookService.findBooksByBookTopic("Algorithms")).thenReturn(Collections.singletonList(mockBook));

        mockMvc.perform(get("/api/v1/books/by-topic/Algorithms")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
        //ЧТо то не то!
    void saveBook() throws Exception {
        BookDTO mockBook = new BookDTO(1L, "Book Title", "1234567890", 100, 5, 3,
                ReaderLevel.ADVANCED, Collections.emptyList(), Collections.emptySet());

        String bookJson = objectMapper.writeValueAsString(mockBook);

        mockMvc.perform(post("/api/v1/books/save_book").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson).locale(Locale.ENGLISH))
                .andExpect(status().isCreated());
        verify(bookService, times(1)).saveBook(any(BookDTO.class));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateBook() throws Exception {
        BookDTO mockBook = new BookDTO(1L, "Book Title", "1234567890", 100,
                5, 3, ReaderLevel.ADVANCED, Collections.emptyList(), Collections.emptySet());

        Mockito.when(bookService.updateBook(mockBook)).thenReturn(mockBook);

        mockMvc.perform(put("/api/v1/books/update_book").with(csrf()).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockBook)))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBook_successfully() throws Exception {
        var bookId = 1L;
        BookDTO mockBook = new BookDTO(1L, "Book Title",
                "1234567890", 100, 5, 3, ReaderLevel.ADVANCED,
                Collections.emptyList(), Collections.emptySet());

        Mockito.when(bookService.findById(1L)).thenReturn(mockBook);
        Mockito.when(messageSource.getMessage(eq("book.deleted"), any(), any())).thenReturn("Book deleted: 1");


        mockMvc.perform(delete("/api/v1/books/delete_book/{id}", bookId).with(csrf()).locale(Locale.ENGLISH))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(("Book deleted: 1"))));
        verify(bookService, times(1)).deleteBook(bookId);
        verify(bookService, times(1)).findById(bookId);

    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteBook_fail() throws Exception {
        var bookId = 1222L;

        Mockito.when(bookService.findById(bookId)).thenReturn(null);
        Mockito.when(messageSource.getMessage(eq("book.unfound"), any(), any())).thenReturn("Book not found: 1222");

        mockMvc.perform(delete("/api/v1/books/delete_book/{id}", bookId).with(csrf()).locale(Locale.ENGLISH))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Book not found")));

        verify(bookService, times(1)).findById(bookId);
        verify(bookService, never()).deleteBook(any());

    }

}