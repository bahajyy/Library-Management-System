package com.getir.library_management_system.repository;

import com.getir.library_management_system.model.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for managing Book entities.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
