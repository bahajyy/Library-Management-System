package com.getir.library_management_system.service.impl;

import com.getir.library_management_system.model.dto.request.CreateBookRequest;
import com.getir.library_management_system.model.dto.request.UpdateBookRequest;
import com.getir.library_management_system.model.dto.response.BookResponse;
import com.getir.library_management_system.model.entity.Book;
import com.getir.library_management_system.model.mapper.BookMapper;
import com.getir.library_management_system.repository.BookRepository;
import com.getir.library_management_system.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Implements book operations with business logic.
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse createBook(CreateBookRequest request) {
        Book savedBook = bookRepository.save(bookMapper.toEntity(request));
        return bookMapper.toResponse(savedBook);
    }

    @Override
    public BookResponse getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return bookMapper.toResponse(book);
    }

    @Override
    public Page<BookResponse> searchBooks(String keyword, Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable)
                .map(book -> book); // For now, just returns all paginated
        return books.map(bookMapper::toResponse);
    }

    @Override
    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        Book updatedBook = bookMapper.toEntity(request, book);
        return bookMapper.toResponse(bookRepository.save(updatedBook));
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
