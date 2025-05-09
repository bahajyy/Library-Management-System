package com.getir.library_management_system.model.dto.request;

import com.getir.library_management_system.model.enums.Genre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO used for creating a new book.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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

        @NotNull(message = "Stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        private Integer stock;

}

