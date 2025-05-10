package com.getir.library_management_system.service.impl;

import com.getir.library_management_system.exception.BusinessException;
import com.getir.library_management_system.exception.NotFoundException;
import com.getir.library_management_system.exception.ResourceNotFoundException;
import com.getir.library_management_system.model.dto.request.CreateBookRequest;
import com.getir.library_management_system.model.dto.request.UpdateBookRequest;
import com.getir.library_management_system.model.dto.response.BookResponse;
import com.getir.library_management_system.model.entity.Book;
import com.getir.library_management_system.model.mapper.BookMapper;
import com.getir.library_management_system.repository.BookRepository;
import com.getir.library_management_system.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    // Creates a new book and returns the response
    @Override
    public BookResponse createBook(CreateBookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("A book with the same ISBN already exists.");
        }
        log.info("Creating a new book with title: {}", request.getTitle());
        Book book = BookMapper.toEntity(request);
        Book savedBook = bookRepository.save(book);
        log.info("Book created with ID: {}", savedBook.getId());
        return BookMapper.toResponse(savedBook);
    }

    // Retrieves a book by its ID
    @Override
    public BookResponse getBook(Long id) {
        log.info("Fetching book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with ID: {}", id);
                    return new ResourceNotFoundException("Book not found");
                });
        log.info("Book found: {}", book.getTitle());
        return BookMapper.toResponse(book);
    }

    // Searches books by keyword with pagination
    @Override
    public Page<BookResponse> searchBooks(String keyword, Pageable pageable) {
        log.info("Searching books with keyword: {}", keyword);
        return bookRepository.search(keyword.toLowerCase(), pageable)
                .map(BookMapper::toResponse);
    }

    // Updates a book's fields except ISBN and returns the updated book
    @Override
    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        log.info("Updating book with ID: {}", id);
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Book not found with ID: {}", id);
                    return new NotFoundException("Book not found");
                });
        if (!book.getIsbn().equals(request.getIsbn())) {
            throw new BusinessException("ISBN cannot be changed.");
        }
        BookMapper.updateEntity(book, request);
        Book updatedBook = bookRepository.save(book);
        log.info("Book updated with ID: {}", updatedBook.getId());
        return BookMapper.toResponse(updatedBook);
    }

    // Deletes a book by ID
    @Override
    public void deleteBook(Long id) {
        log.info("Deleting book with ID: {}", id);
        if (!bookRepository.existsById(id)) {
            log.warn("Cannot delete. Book not found with ID: {}", id);
            throw new NotFoundException("Book not found");
        }
        bookRepository.deleteById(id);
        log.info("Book deleted with ID: {}", id);
    }
}
