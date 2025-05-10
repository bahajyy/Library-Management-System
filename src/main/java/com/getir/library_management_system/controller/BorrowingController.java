package com.getir.library_management_system.controller;

import com.getir.library_management_system.model.dto.request.BorrowBookRequest;
import com.getir.library_management_system.model.dto.response.BorrowingResponse;
import com.getir.library_management_system.model.dto.response.OverdueBookResponse;
import com.getir.library_management_system.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller that handles borrowing and returning books.
 */
@RestController
@RequestMapping("/api/borrowings")
@RequiredArgsConstructor
@Slf4j // Enables logging
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/borrow")
    @PreAuthorize("hasAuthority('PATRON')") // Only patrons can borrow books
    public ResponseEntity<BorrowingResponse> borrowBook(@RequestBody BorrowBookRequest request) {
        log.info("User {} is attempting to borrow book with ID {}", request.getUserId(), request.getBookId());
        BorrowingResponse response = borrowingService.borrowBook(request);
        log.info("Book borrowed successfully with borrowing ID {}", response.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/return/{borrowingId}")
    @PreAuthorize("hasAuthority('PATRON')") // Only patrons can return books
    public ResponseEntity<BorrowingResponse> returnBook(@PathVariable Long borrowingId) {
        log.info("Returning book for borrowing ID {}", borrowingId);
        BorrowingResponse response = borrowingService.returnBook(borrowingId);
        log.info("Book returned successfully for borrowing ID {}", borrowingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-history/{userId}")
    @PreAuthorize("hasAuthority('PATRON')") // Only patrons can view their borrowing history
    public ResponseEntity<List<BorrowingResponse>> myHistory(@PathVariable Long userId) {
        log.info("Fetching borrowing history for user ID {}", userId);
        return ResponseEntity.ok(borrowingService.getMyBorrowingHistory(userId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('LIBRARIAN')") // Only librarians can view all borrowings
    public ResponseEntity<List<BorrowingResponse>> allBorrowings() {
        log.info("Fetching all borrowings (Librarian access)");
        return ResponseEntity.ok(borrowingService.getAllBorrowings());
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('LIBRARIAN')") // Only librarians can view overdue borrowings
    public ResponseEntity<List<BorrowingResponse>> overdueBorrowings() {
        log.info("Fetching overdue borrowings");
        return ResponseEntity.ok(borrowingService.getOverdueBorrowings());
    }

    @GetMapping("/overdueBooks")
    @PreAuthorize("hasAuthority('LIBRARIAN')") // Only librarians can view detailed overdue books
    public ResponseEntity<List<OverdueBookResponse>> getOverdueBooks() {
        log.info("Fetching detailed overdue book list");
        return ResponseEntity.ok(borrowingService.getAllOverdueBooks());
    }

    @GetMapping("/overdueBooks/report")
    @PreAuthorize("hasAuthority('LIBRARIAN')") // Only librarians can generate overdue report
    public ResponseEntity<String> getOverdueReport() {
        log.info("Generating overdue book report");
        return ResponseEntity.ok(borrowingService.generateOverdueReport());
    }

}
