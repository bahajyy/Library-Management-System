package com.getir.library_management_system.model.mapper;

import com.getir.library_management_system.model.dto.request.CreateBookRequest;
import com.getir.library_management_system.model.dto.request.UpdateBookRequest;
import com.getir.library_management_system.model.dto.response.BookResponse;
import com.getir.library_management_system.model.entity.Book;
import org.springframework.stereotype.Component;

/**
 * Converts between Book entities and DTOs.
 */
@Component
public class BookMapper {

    public Book toEntity(CreateBookRequest request) {
        return Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .publicationDate(request.publicationDate())
                .genre(request.genre())
                .build();
    }

    public Book toEntity(UpdateBookRequest request, Book existingBook) {
        existingBook.setTitle(request.title());
        existingBook.setAuthor(request.author());
        existingBook.setIsbn(request.isbn());
        existingBook.setPublicationDate(request.publicationDate());
        existingBook.setGenre(request.genre());
        return existingBook;
    }

    public BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublicationDate(),
                book.getGenre()
        );
    }
}
