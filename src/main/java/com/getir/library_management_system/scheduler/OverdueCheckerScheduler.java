package com.getir.library_management_system.scheduler;

import com.getir.library_management_system.service.BorrowingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j // Enables logging
@Component
@RequiredArgsConstructor
public class OverdueCheckerScheduler {

    private final BorrowingService borrowingService;

    // Runs every day at 08:00 AM to check for overdue books
    @Scheduled(cron = "0 0 8 * * *") // every day at 08:00
    public void checkOverdueBooks() {
        var overdueBooks = borrowingService.getAllOverdueBooks();
        overdueBooks.forEach(book ->
                log.warn("Overdue Book: {} borrowed by {}, overdue {} days.",
                        book.getBookTitle(), book.getBorrowerName(), book.getDaysOverdue()));
    }
}
