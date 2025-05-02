package com.getir.library_management_system.model.dto.request;

import com.getir.library_management_system.model.enums.Genre;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO used for creating a new book.
 */
@Data
public class CreateBookRequest {

        @NotBlank
        private String title;

        @NotBlank
        private String author;

        @NotBlank
        private String isbn;

        @NotNull
        private LocalDate publicationDate;

        @NotNull
        private Genre genre;
}

