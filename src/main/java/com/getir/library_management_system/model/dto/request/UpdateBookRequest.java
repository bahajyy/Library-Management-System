package com.getir.library_management_system.model.dto.request;

import com.getir.library_management_system.model.enums.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateBookRequest {

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
