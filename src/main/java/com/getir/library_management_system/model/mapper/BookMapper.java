package com.getir.library_management_system.model.mapper;

import com.getir.library_management_system.model.dto.request.CreateBookRequest;
import com.getir.library_management_system.model.dto.request.UpdateBookRequest;
import com.getir.library_management_system.model.dto.response.BookResponse;
import com.getir.library_management_system.model.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public static Book toEntity(CreateBookRequest request) {
        return Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isbn(request.getIsbn())
                .publicationDate(request.getPublicationDate())
                .genre(request.getGenre())
                .stock(request.getStock())
                .build();
    }

    public static void updateEntity(Book book, UpdateBookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());
        book.setPublicationDate(request.getPublicationDate());
        book.setGenre(request.getGenre());
        book.setStock(request.getStock());
    }

    public static BookResponse toResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publicationDate(book.getPublicationDate())
                .genre(book.getGenre())
                .stock(book.getStock())
                .build();
    }
}
