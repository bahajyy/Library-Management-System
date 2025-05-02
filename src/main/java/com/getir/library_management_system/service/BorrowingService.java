package com.getir.library_management_system.service;

import com.getir.library_management_system.model.dto.request.BorrowBookRequest;
import com.getir.library_management_system.model.dto.response.BorrowingResponse;

import java.util.List;

public interface BorrowingService {
    BorrowingResponse borrowBook(BorrowBookRequest request);
    BorrowingResponse returnBook(Long borrowingId);
    List<BorrowingResponse> getMyBorrowingHistory(Long userId);
    List<BorrowingResponse> getAllBorrowings(); // Only for librarians
    List<BorrowingResponse> getOverdueBorrowings(); // Only for librarians
}
