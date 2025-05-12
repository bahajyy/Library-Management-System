package com.getir.library_management_system.service;

import com.getir.library_management_system.exception.BusinessException;
import com.getir.library_management_system.exception.NotFoundException;
import com.getir.library_management_system.exception.ResourceNotFoundException;
import com.getir.library_management_system.model.dto.request.CreateBookRequest;
import com.getir.library_management_system.model.dto.request.UpdateBookRequest;
import com.getir.library_management_system.model.entity.Book;
import com.getir.library_management_system.model.enums.Genre;
import com.getir.library_management_system.repository.BookRepository;
import com.getir.library_management_system.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class BookServiceImplTest {

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test: createBook success
    @Test
    void shouldCreateBookSuccessfully() {
        CreateBookRequest request = new CreateBookRequest("Title", "Author", "123", LocalDate.now(), Genre.DRAMA, 5);
        Book saved = Book.builder().id(1L).title("Title").author("Author").isbn("123").build();

        when(bookRepository.existsByIsbn("123")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(saved);

        var response = bookService.createBook(request);

        assertEquals("Title", response.getTitle());
        verify(bookRepository).save(any(Book.class));
    }

    // Test: createBook -> ISBN conflict
    @Test
    void shouldThrowExceptionWhenIsbnExists() {
        CreateBookRequest request = new CreateBookRequest("Title", "Author", "123", LocalDate.now(), Genre.DRAMA, 5);
        when(bookRepository.existsByIsbn("123")).thenReturn(true);

        assertThrows(BusinessException.class, () -> bookService.createBook(request));
    }

    // Test: getBook success
    @Test
    void shouldGetBookSuccessfully() {
        Book book = Book.builder().id(1L).title("Sample").isbn("123").build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        var response = bookService.getBook(1L);

        assertEquals("Sample", response.getTitle());
        verify(bookRepository).findById(1L);
    }

    // Test: getBook -> not found
    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> bookService.getBook(1L));
    }

    // Test: searchBooks
    @Test
    void shouldSearchBooksSuccessfully() {
        Pageable pageable = PageRequest.of(0, 5);
        List<Book> books = List.of(Book.builder().id(1L).title("Java 101").build());
        Page<Book> page = new PageImpl<>(books);

        when(bookRepository.search("java", pageable)).thenReturn(page);

        var result = bookService.searchBooks("java", pageable);

        assertEquals(1, result.getTotalElements());
        verify(bookRepository).search("java", pageable);
    }

    // Test: updateBook success
    @Test
    void shouldUpdateBookSuccessfully() {
        Book existing = Book.builder().id(1L).isbn("123").title("Old Title").build();
        UpdateBookRequest request = new UpdateBookRequest("New Title", "New Author", "123", LocalDate.now(), Genre.DRAMA,3);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenReturn(existing);

        var result = bookService.updateBook(1L, request);

        assertEquals("New Title", result.getTitle());
    }

    // Test: updateBook -> ISBN changes fail
    @Test
    void shouldThrowWhenIsbnChangedOnUpdate() {
        Book existing = Book.builder().id(1L).isbn("123").build();
        UpdateBookRequest request = new UpdateBookRequest("Title", "Author", "456", LocalDate.now(), Genre.DRAMA,3);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));

        assertThrows(BusinessException.class, () -> bookService.updateBook(1L, request));
    }

    // Test: updateBook -> not found
    @Test
    void shouldThrowWhenUpdatingNonExistingBook() {
        UpdateBookRequest request = new UpdateBookRequest("Title", "Author", "123", LocalDate.now(), Genre.DRAMA,3);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.updateBook(1L, request));
    }

    // Test: deleteBook success
    @Test
    void shouldDeleteBookSuccessfully() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    // Test: deleteBook -> not found
    @Test
    void shouldThrowWhenDeletingNonExistingBook() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookService.deleteBook(1L));
    }
}
