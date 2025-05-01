package com.getir.library_management_system.service;

import com.getir.library_management_system.model.dto.request.CreateBookRequest;
import com.getir.library_management_system.model.dto.request.UpdateBookRequest;
import com.getir.library_management_system.model.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookResponse createBook(CreateBookRequest request);

    BookResponse getBook(Long id);

    Page<BookResponse> searchBooks(String keyword, Pageable pageable);

    BookResponse updateBook(Long id, UpdateBookRequest request);

    void deleteBook(Long id);
}
