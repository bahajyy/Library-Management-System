package com.getir.library_management_system.model.dto.response;

import com.getir.library_management_system.model.enums.Genre;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private LocalDate publicationDate;
    private Genre genre;
    private Boolean available;
}
