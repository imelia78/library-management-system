package com.example.BookProject.DTO;

import com.example.BookProject.Model.ReaderLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


public record BookDTO(
        Long id,
        @NotBlank String title,
        @Size(min = 10, max = 13) String isbn,
        @Positive Integer pages,
        @Positive Integer totalCopies,
        @Positive Integer availableCopies,
        ReaderLevel readerLevel,
        List<String> topics,       // from BookTopic::getTopic
        Set<String> authors        // from Author::getName
) {
}
