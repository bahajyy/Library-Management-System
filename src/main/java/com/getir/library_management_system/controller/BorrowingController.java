package com.getir.library_management_system.controller;

import com.getir.library_management_system.model.dto.request.BorrowBookRequest;
import com.getir.library_management_system.model.dto.response.BorrowingResponse;
import com.getir.library_management_system.model.dto.response.OverdueBookResponse;
import com.getir.library_management_system.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/borrow")
    @PreAuthorize("hasAuthority('PATRON')")
    public ResponseEntity<BorrowingResponse> borrowBook(@RequestBody BorrowBookRequest request) {
        return ResponseEntity.ok(borrowingService.borrowBook(request));
    }

    @PostMapping("/return/{borrowingId}")
    @PreAuthorize("hasAuthority('PATRON')")
    public ResponseEntity<BorrowingResponse> returnBook(@PathVariable Long borrowingId) {
        return ResponseEntity.ok(borrowingService.returnBook(borrowingId));
    }

    @GetMapping("/my-history/{userId}")
    @PreAuthorize("hasAuthority('PATRON')")
    public ResponseEntity<List<BorrowingResponse>> myHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(borrowingService.getMyBorrowingHistory(userId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<List<BorrowingResponse>> allBorrowings() {
        return ResponseEntity.ok(borrowingService.getAllBorrowings());
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<List<BorrowingResponse>> overdueBorrowings() {
        return ResponseEntity.ok(borrowingService.getOverdueBorrowings());
    }

    @GetMapping("/overdueBooks")
    @PreAuthorize("hasAuthority('LIBRARIAN')")
    public ResponseEntity<List<OverdueBookResponse>> getOverdueBooks() {
        return ResponseEntity.ok(borrowingService.getAllOverdueBooks());
    }

}
