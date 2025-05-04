package com.getir.library_management_system.repository;

import com.getir.library_management_system.model.entity.Borrowing;
import com.getir.library_management_system.model.enums.BorrowingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    Page<Borrowing> findByUserId(Long userId, Pageable pageable);

    Page<Borrowing> findByStatusAndDueDateBefore(BorrowingStatus status, LocalDate currentDate, Pageable pageable);

    List<Borrowing> findByReturnedFalseAndDueDateBefore(LocalDate date);

}
