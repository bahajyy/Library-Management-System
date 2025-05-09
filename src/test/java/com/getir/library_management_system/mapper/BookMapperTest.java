package com.getir.library_management_system.mapper;

import com.getir.library_management_system.model.dto.request.CreateBookRequest;
import com.getir.library_management_system.model.dto.request.UpdateBookRequest;
import com.getir.library_management_system.model.dto.response.BookResponse;
import com.getir.library_management_system.model.entity.Book;
import com.getir.library_management_system.model.enums.Genre;
import com.getir.library_management_system.model.mapper.BookMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookMapperTest {

    @Test
    void toEntity_shouldMapCorrectly() {
        CreateBookRequest request = new CreateBookRequest();
        request.setTitle("Book Title");
        request.setAuthor("Author");
        request.setIsbn("1234567890");
        request.setPublicationDate(LocalDate.of(2020, 1, 1));
        request.setGenre(Genre.FICTION);
        request.setStock(5);

        Book book = BookMapper.toEntity(request);

        assertEquals("Book Title", book.getTitle());
        assertEquals("Author", book.getAuthor());
        assertEquals("1234567890", book.getIsbn());
        assertEquals(LocalDate.of(2020, 1, 1), book.getPublicationDate());
        assertEquals(Genre.FICTION, book.getGenre());
        assertEquals(5, book.getStock());
    }

    @Test
    void updateEntity_shouldUpdateCorrectly() {
        Book book = Book.builder().build();

        UpdateBookRequest request = new UpdateBookRequest();
        request.setTitle("Updated Title");
        request.setAuthor("Updated Author");
        request.setIsbn("9876543210");
        request.setPublicationDate(LocalDate.of(2021, 5, 5));
        request.setGenre(Genre.HISTORY);
        request.setStock(10);

        BookMapper.updateEntity(book, request);

        assertEquals("Updated Title", book.getTitle());
        assertEquals("Updated Author", book.getAuthor());
        assertEquals("9876543210", book.getIsbn());
        assertEquals(LocalDate.of(2021, 5, 5), book.getPublicationDate());
        assertEquals(Genre.HISTORY, book.getGenre());
        assertEquals(10, book.getStock());
    }

    @Test
    void toResponse_shouldMapCorrectly() {
        Book book = Book.builder()
                .id(1L)
                .title("Title")
                .author("Author")
                .isbn("1122334455")
                .publicationDate(LocalDate.of(2019, 3, 15))
                .genre(Genre.SCIENCE)
                .stock(7)
                .build();

        BookResponse response = BookMapper.toResponse(book);

        assertEquals(1L, response.getId());
        assertEquals("Title", response.getTitle());
        assertEquals("Author", response.getAuthor());
        assertEquals("1122334455", response.getIsbn());
        assertEquals(LocalDate.of(2019, 3, 15), response.getPublicationDate());
        assertEquals(Genre.SCIENCE, response.getGenre());
        assertEquals(7, response.getStock());
    }
}
