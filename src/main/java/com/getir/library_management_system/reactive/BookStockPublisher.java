package com.getir.library_management_system.reactive;

import com.getir.library_management_system.model.dto.response.BookStockUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@Slf4j // Enables logging
public class BookStockPublisher {

    // Sink for emitting real-time book stock updates to multiple subscribers
    private final Sinks.Many<BookStockUpdate> sink = Sinks.many().multicast().onBackpressureBuffer();

    // Publishes a new stock update to all subscribers
    public void publish(BookStockUpdate update) {
        log.info("Publishing stock update: {}", update);
        sink.tryEmitNext(update);
    }

    // Returns a Flux stream of stock updates for clients to subscribe
    public Flux<BookStockUpdate> getUpdates() {
        return sink.asFlux();
    }
}
