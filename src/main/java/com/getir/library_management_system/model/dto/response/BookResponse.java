package com.getir.library_management_system.model.dto.response;

import com.getir.library_management_system.model.enums.Genre;

import java.time.LocalDate;

/**
 * DTO used for returning book details.
 */
public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        LocalDate publicationDate,
        Genre genre
) {}
