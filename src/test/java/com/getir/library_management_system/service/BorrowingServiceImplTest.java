package com.getir.library_management_system.service;

import com.getir.library_management_system.exception.BusinessException;
import com.getir.library_management_system.model.dto.request.BorrowBookRequest;
import com.getir.library_management_system.model.dto.response.BookStockUpdate;
import com.getir.library_management_system.model.dto.response.BorrowingResponse;
import com.getir.library_management_system.model.dto.response.OverdueBookResponse;
import com.getir.library_management_system.model.entity.Book;
import com.getir.library_management_system.model.entity.Borrowing;
import com.getir.library_management_system.model.entity.User;
import com.getir.library_management_system.model.enums.BorrowingStatus;
import com.getir.library_management_system.model.mapper.BorrowingMapper;
import com.getir.library_management_system.reactive.BookStockPublisher;
import com.getir.library_management_system.repository.BookRepository;
import com.getir.library_management_system.repository.BorrowingRepository;
import com.getir.library_management_system.repository.UserRepository;
import com.getir.library_management_system.service.impl.BorrowingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BorrowingServiceImplTest {

    @Mock BorrowingRepository borrowingRepository;
    @Mock BookRepository bookRepository;
    @Mock UserRepository userRepository;
    @Mock BorrowingMapper borrowingMapper;
    @Mock BookStockPublisher bookStockPublisher;

    @InjectMocks
    BorrowingServiceImpl borrowingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Book createBook(Long id, int stock) {
        return Book.builder().id(id).title("Book " + id).stock(stock).build();
    }

    private User createUser(Long id) {
        return User.builder().id(id).name("User " + id).build();
    }

    private Borrowing createBorrowing(Book book, User user, BorrowingStatus status, LocalDate dueDate) {
        return Borrowing.builder()
                .id(1L)
                .book(book)
                .user(user)
                .status(status)
                .dueDate(dueDate)
                .build();
    }

    @Test
    void borrowBook_success() {
        BorrowBookRequest request = new BorrowBookRequest(1L, 1L);
        Book book = createBook(1L, 2);
        User user = createUser(1L);
        Borrowing borrowing = createBorrowing(book, user, BorrowingStatus.BORROWED, LocalDate.now().plusDays(14));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowingRepository.countByUserIdAndStatus(1L, BorrowingStatus.BORROWED)).thenReturn(0L);
        when(borrowingRepository.existsByUserIdAndBookIdAndStatus(1L, 1L, BorrowingStatus.BORROWED)).thenReturn(false);
        when(borrowingRepository.existsByUserIdAndStatusAndDueDateBefore(eq(1L), eq(BorrowingStatus.BORROWED), any())).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(borrowingRepository.save(any())).thenReturn(borrowing);
        when(borrowingMapper.toResponse(any())).thenReturn(new BorrowingResponse());

        assertNotNull(borrowingService.borrowBook(request));
        verify(bookStockPublisher).publish(any(BookStockUpdate.class));
    }

    @Test void borrowBook_bookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> borrowingService.borrowBook(new BorrowBookRequest(1L, 1L)));
    }

    @Test void borrowBook_limitExceeded() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(createBook(1L, 1)));
        when(borrowingRepository.countByUserIdAndStatus(1L, BorrowingStatus.BORROWED)).thenReturn(5L);
        assertThrows(BusinessException.class, () -> borrowingService.borrowBook(new BorrowBookRequest(1L, 1L)));
    }

    @Test void borrowBook_alreadyBorrowed() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(createBook(1L, 1)));
        when(borrowingRepository.countByUserIdAndStatus(1L, BorrowingStatus.BORROWED)).thenReturn(1L);
        when(borrowingRepository.existsByUserIdAndBookIdAndStatus(1L, 1L, BorrowingStatus.BORROWED)).thenReturn(true);
        assertThrows(BusinessException.class, () -> borrowingService.borrowBook(new BorrowBookRequest(1L, 1L)));
    }

    @Test void borrowBook_outOfStock() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(createBook(1L, 0)));
        when(borrowingRepository.countByUserIdAndStatus(1L, BorrowingStatus.BORROWED)).thenReturn(0L);
        when(borrowingRepository.existsByUserIdAndBookIdAndStatus(any(), any(), any())).thenReturn(false);
        assertThrows(BusinessException.class, () -> borrowingService.borrowBook(new BorrowBookRequest(1L, 1L)));
    }

    @Test void borrowBook_userOverdue() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(createBook(1L, 1)));
        when(borrowingRepository.countByUserIdAndStatus(1L, BorrowingStatus.BORROWED)).thenReturn(0L);
        when(borrowingRepository.existsByUserIdAndBookIdAndStatus(any(), any(), any())).thenReturn(false);
        when(borrowingRepository.existsByUserIdAndStatusAndDueDateBefore(any(), any(), any())).thenReturn(true);
        assertThrows(BusinessException.class, () -> borrowingService.borrowBook(new BorrowBookRequest(1L, 1L)));
    }

    @Test void borrowBook_userNotFound() {
        Book book = createBook(1L, 1);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowingRepository.countByUserIdAndStatus(any(), any())).thenReturn(0L);
        when(borrowingRepository.existsByUserIdAndBookIdAndStatus(any(), any(), any())).thenReturn(false);
        when(borrowingRepository.existsByUserIdAndStatusAndDueDateBefore(any(), any(), any())).thenReturn(false);
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> borrowingService.borrowBook(new BorrowBookRequest(1L, 1L)));
    }

    @Test void returnBook_success() {
        Book book = createBook(1L, 0);
        Borrowing borrowing = createBorrowing(book, createUser(1L), BorrowingStatus.BORROWED, LocalDate.now());

        when(borrowingRepository.findById(1L)).thenReturn(Optional.of(borrowing));
        when(borrowingMapper.toResponse(any())).thenReturn(new BorrowingResponse());

        assertNotNull(borrowingService.returnBook(1L));
        verify(bookStockPublisher).publish(any(BookStockUpdate.class));
    }

    @Test void returnBook_notFound() {
        when(borrowingRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> borrowingService.returnBook(1L));
    }

    @Test void returnBook_invalidStatus() {
        Borrowing borrowing = createBorrowing(createBook(1L, 1), createUser(1L), BorrowingStatus.RETURNED, LocalDate.now());
        when(borrowingRepository.findById(1L)).thenReturn(Optional.of(borrowing));
        assertThrows(BusinessException.class, () -> borrowingService.returnBook(1L));
    }

    @Test void getMyBorrowingHistory_success() {
        when(borrowingRepository.findByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new Borrowing())));
        when(borrowingMapper.toResponse(any())).thenReturn(new BorrowingResponse());

        assertEquals(1, borrowingService.getMyBorrowingHistory(1L).size());
    }

    @Test void getAllBorrowings_success() {
        when(borrowingRepository.findAll()).thenReturn(List.of(new Borrowing()));
        when(borrowingMapper.toResponse(any())).thenReturn(new BorrowingResponse());

        assertEquals(1, borrowingService.getAllBorrowings().size());
    }

    @Test void getOverdueBorrowings_success() {
        when(borrowingRepository.findByStatusAndDueDateBefore(eq(BorrowingStatus.BORROWED), any(), any()))
                .thenReturn(new PageImpl<>(List.of(new Borrowing())));
        when(borrowingMapper.toResponse(any())).thenReturn(new BorrowingResponse());

        assertEquals(1, borrowingService.getOverdueBorrowings().size());
    }

    @Test void getAllOverdueBooks_success() {
        Book book = createBook(1L, 1);
        User user = createUser(1L);
        Borrowing borrowing = createBorrowing(book, user, BorrowingStatus.BORROWED, LocalDate.now().minusDays(3));
        when(borrowingRepository.findByReturnedFalseAndDueDateBefore(any()))
                .thenReturn(List.of(borrowing));

        List<OverdueBookResponse> result = borrowingService.getAllOverdueBooks();
        assertEquals(1, result.size());
        assertEquals("Book 1", result.get(0).getBookTitle());
    }

    @Test void generateOverdueReport_success() {
        List<Borrowing> borrowings = List.of(
                createBorrowing(null, null, BorrowingStatus.BORROWED, LocalDate.now().minusDays(2)),
                createBorrowing(null, null, BorrowingStatus.BORROWED, LocalDate.now().minusDays(7)),
                createBorrowing(null, null, BorrowingStatus.BORROWED, LocalDate.now().minusDays(15))
        );

        when(borrowingRepository.findByReturnedFalseAndDueDateBefore(any()))
                .thenReturn(borrowings);

        String report = borrowingService.generateOverdueReport();
        assertTrue(report.contains("Total Overdue Books: 3"));
        assertTrue(report.contains("0-5 days overdue:    1"));
        assertTrue(report.contains("6-10 days overdue:   1"));
        assertTrue(report.contains(">10 days overdue:    1"));
    }
}