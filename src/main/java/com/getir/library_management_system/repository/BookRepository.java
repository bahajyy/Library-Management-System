package com.getir.library_management_system.repository;

import com.getir.library_management_system.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE %:keyword% OR " +
            "LOWER(b.author) LIKE %:keyword% OR " +
            "LOWER(b.isbn) LIKE %:keyword% OR " +
            "LOWER(b.genre) LIKE %:keyword%")
    Page<Book> search(String keyword, Pageable pageable);

    boolean existsByIsbn(String isbn);

}
