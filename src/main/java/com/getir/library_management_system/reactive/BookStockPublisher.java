package com.getir.library_management_system.reactive;

import com.getir.library_management_system.model.dto.response.BookStockUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
@Slf4j
public class BookStockPublisher {

    private final Sinks.Many<BookStockUpdate> sink = Sinks.many().multicast().onBackpressureBuffer();

    public void publish(BookStockUpdate update) {
        log.info("Publishing stock update: {}", update);
        sink.tryEmitNext(update);
    }

    public Flux<BookStockUpdate> getUpdates() {
        return sink.asFlux();
    }
}
