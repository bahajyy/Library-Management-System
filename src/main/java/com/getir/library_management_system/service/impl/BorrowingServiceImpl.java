package com.getir.library_management_system.service.impl;

import com.getir.library_management_system.exception.BusinessException;
import com.getir.library_management_system.model.dto.request.BorrowBookRequest;
import com.getir.library_management_system.model.dto.response.BorrowingResponse;
import com.getir.library_management_system.model.dto.response.OverdueBookResponse;
import com.getir.library_management_system.model.entity.Book;
import com.getir.library_management_system.model.entity.Borrowing;
import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.model.enums.BorrowingStatus;
import com.getir.library_management_system.model.mapper.BorrowingMapper;
import com.getir.library_management_system.repository.BookRepository;
import com.getir.library_management_system.repository.BorrowingRepository;
import com.getir.library_management_system.repository.UserRepository;
import com.getir.library_management_system.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingMapper borrowingMapper;

    @Override
    public BorrowingResponse borrowBook(BorrowBookRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BusinessException("Book not found"));

        if (!Boolean.TRUE.equals(book.getAvailable())) {
            throw new BusinessException("Book is not available");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException("User not found"));

        book.setAvailable(false);
        bookRepository.save(book);

        Borrowing borrowing = Borrowing.builder()
                .book(book)
                .user(user)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BorrowingStatus.BORROWED)
                .build();

        borrowingRepository.save(borrowing);
        return borrowingMapper.toResponse(borrowing);
    }

    @Override
    public BorrowingResponse returnBook(Long borrowingId) {
        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> new BusinessException("Borrowing not found"));

        if (borrowing.getStatus() != BorrowingStatus.BORROWED) {
            throw new BusinessException("Book is not currently borrowed");
        }

        Book book = borrowing.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        borrowing.setStatus(BorrowingStatus.RETURNED);
        borrowing.setReturnDate(LocalDate.now());
        borrowingRepository.save(borrowing);

        return borrowingMapper.toResponse(borrowing);
    }

    @Override
    public List<BorrowingResponse> getMyBorrowingHistory(Long userId) {
        List<Borrowing> borrowings = borrowingRepository
                .findByUserId(userId, Pageable.unpaged()) // veya kendi pageable'ını ver
                .getContent();
        return borrowings.stream()
                .map(borrowingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingResponse> getAllBorrowings() {
        return borrowingRepository.findAll()
                .stream()
                .map(borrowingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingResponse> getOverdueBorrowings() {
        List<Borrowing> overdue = (List<Borrowing>) borrowingRepository.findByStatusAndDueDateBefore(BorrowingStatus.BORROWED, LocalDate.now(), Pageable.unpaged());
        return overdue.stream()
                .map(borrowingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OverdueBookResponse> getAllOverdueBooks() {
        LocalDate today = LocalDate.now();
        List<Borrowing> overdueList = borrowingRepository.findByReturnedFalseAndDueDateBefore(today);

        return overdueList.stream()
                .map(borrowing -> OverdueBookResponse.builder()
                        .borrowingId(borrowing.getId())
                        .bookTitle(borrowing.getBook().getTitle())
                        .borrowerName(borrowing.getUser().getName())
                        .dueDate(borrowing.getDueDate())
                        .daysOverdue(ChronoUnit.DAYS.between(borrowing.getDueDate(), today))
                        .build())
                .toList();
    }

}
