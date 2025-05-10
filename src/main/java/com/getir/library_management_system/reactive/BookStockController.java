package com.getir.library_management_system.reactive;

import com.getir.library_management_system.model.dto.response.BookStockUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/reactive/books")
@RequiredArgsConstructor
public class BookStockController {

    private final BookStockPublisher publisher;

    // Streams real-time book stock updates using Server-Sent Events (SSE)
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BookStockUpdate> streamBookStock() {
        return publisher.getUpdates();
    }
}
