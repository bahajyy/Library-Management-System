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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static com.getir.library_management_system.model.enums.BorrowingStatus.BORROWED;

@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowingServiceImpl implements BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BorrowingMapper borrowingMapper;

    @Override
    public BorrowingResponse borrowBook(BorrowBookRequest request) {
        log.info("User {} is attempting to borrow book {}", request.getUserId(), request.getBookId());

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> {
                    log.warn("Book not found with ID: {}", request.getBookId());
                    return new BusinessException("Book not found");
                });

        long activeBorrowCount = borrowingRepository.countByUserIdAndStatus(request.getUserId(), BorrowingStatus.BORROWED);
        if (activeBorrowCount >= 5) {
            throw new BusinessException("You cannot borrow more than 5 books at the same time.");
        }

        boolean alreadyBorrowed = borrowingRepository.existsByUserIdAndBookIdAndStatus(
                request.getUserId(), request.getBookId(), BorrowingStatus.BORROWED);
        if (alreadyBorrowed) {
            throw new BusinessException("You have already borrowed this book.");
        }

        if (book.getStock() <= 0) {
            throw new BusinessException("Book is out of stock");
        }
        book.setStock(book.getStock() - 1);
        if (borrowingRepository.existsByUserIdAndStatusAndDueDateBefore(request.getUserId(), BORROWED, LocalDate.now())) {
            throw new BusinessException("User has overdue books and cannot borrow new ones.");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found with ID: {}", request.getUserId());
                    return new BusinessException("User not found");
                });

        bookRepository.save(book);
        log.info("Book {} marked as unavailable", book.getId());

        Borrowing borrowing = Borrowing.builder()
                .book(book)
                .user(user)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BORROWED)
                .build();

        borrowingRepository.save(borrowing);
        log.info("Borrowing created with ID: {}", borrowing.getId());

        return borrowingMapper.toResponse(borrowing);
    }

    @Override
    public BorrowingResponse returnBook(Long borrowingId) {
        log.info("Returning book for borrowing ID: {}", borrowingId);

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() -> {
                    log.warn("Borrowing not found with ID: {}", borrowingId);
                    return new BusinessException("Borrowing not found");
                });

        if (borrowing.getStatus() != BORROWED) {
            log.warn("Borrowing ID {} is not currently active", borrowingId);
            throw new BusinessException("Book is not currently borrowed");
        }

        Book book = borrowing.getBook();
        book.setStock(book.getStock() + 1);
        bookRepository.save(book);
        log.info("Book {} marked as available", book.getId());

        borrowing.setStatus(BorrowingStatus.RETURNED);
        borrowing.setReturnDate(LocalDate.now());
        borrowingRepository.save(borrowing);
        log.info("Borrowing {} marked as returned", borrowing.getId());

        return borrowingMapper.toResponse(borrowing);
    }

    @Override
    public List<BorrowingResponse> getMyBorrowingHistory(Long userId) {
        log.info("Fetching borrowing history for user ID: {}", userId);
        List<Borrowing> borrowings = borrowingRepository
                .findByUserId(userId, Pageable.unpaged())
                .getContent();

        return borrowings.stream()
                .map(borrowingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingResponse> getAllBorrowings() {
        log.info("Fetching all borrowing records");
        return borrowingRepository.findAll()
                .stream()
                .map(borrowingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowingResponse> getOverdueBorrowings() {
        log.info("Fetching overdue borrowings");
        List<Borrowing> overdue = (List<Borrowing>) borrowingRepository
                .findByStatusAndDueDateBefore(BORROWED, LocalDate.now(), Pageable.unpaged());

        return overdue.stream()
                .map(borrowingMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OverdueBookResponse> getAllOverdueBooks() {
        LocalDate today = LocalDate.now();
        log.info("Checking for overdue books as of {}", today);

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

    @Override
    public String generateOverdueReport() {
        LocalDate today = LocalDate.now();
        List<Borrowing> overdueList = borrowingRepository.findByReturnedFalseAndDueDateBefore(today);

        long total = overdueList.size();
        long days0to5 = overdueList.stream().filter(b -> ChronoUnit.DAYS.between(b.getDueDate(), today) <= 5).count();
        long days6to10 = overdueList.stream().filter(b -> {
            long days = ChronoUnit.DAYS.between(b.getDueDate(), today);
            return days >= 6 && days <= 10;
        }).count();
        long days11plus = overdueList.stream().filter(b -> ChronoUnit.DAYS.between(b.getDueDate(), today) > 10).count();

        return """
                OVERDUE BOOK REPORT
                -------------------
                Total Overdue Books: %d
                0-5 days overdue:    %d
                6-10 days overdue:   %d
                >10 days overdue:    %d
                
                Report Generated At: %s
                """.formatted(
                total,
                days0to5,
                days6to10,
                days11plus,
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

}
