package com.getir.library_management_system.model.dto.request;

import com.getir.library_management_system.model.enums.Genre;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO used for creating a new book.
 */
public record CreateBookRequest(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Author is required")
        String author,

        @NotBlank(message = "ISBN is required")
        String isbn,

        @PastOrPresent(message = "Publication date cannot be in the future")
        LocalDate publicationDate,

        @NotNull(message = "Genre is required")
        Genre genre
) {}
