package com.getir.library_management_system.model.dto.response;

import com.getir.library_management_system.model.enums.Genre;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO used for returning book details.
 */
@Data
@Builder
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publicationDate;
    private Genre genre;
}
