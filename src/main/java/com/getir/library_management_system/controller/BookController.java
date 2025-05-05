package com.getir.library_management_system.controller;

import com.getir.library_management_system.model.dto.request.CreateBookRequest;
import com.getir.library_management_system.model.dto.request.UpdateBookRequest;
import com.getir.library_management_system.model.dto.response.BookResponse;
import com.getir.library_management_system.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that handles all book-related endpoints.
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Slf4j
public class BookController {

    private final BookService bookService;

    @PostMapping
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody CreateBookRequest request) {
        log.info("Request to create a new book: {}", request.getTitle());
        BookResponse response = bookService.createBook(request);
        log.info("Book created with ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('LIBRARIAN', 'PATRON')")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        log.info("Fetching book details for ID: {}", id);
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('LIBRARIAN', 'PATRON')")
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("Searching books with keyword: '{}', page: {}, size: {}", keyword, page, size);
        return ResponseEntity.ok(bookService.searchBooks(keyword, PageRequest.of(page, size)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBookRequest request) {
        log.info("Updating book with ID: {}", id);
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        log.info("Deleting book with ID: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
