package com.getir.library_management_system.reactive;

import com.getir.library_management_system.model.dto.response.BookStockUpdate;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class BookStockPublisherTest {

    @Test
    void publish_shouldEmitStockUpdate() {
        // Arrange
        BookStockPublisher publisher = new BookStockPublisher();
        BookStockUpdate update = new BookStockUpdate(1L, "Sample Title", 5);

        // Act & Assert
        StepVerifier.create(publisher.getUpdates())
                .then(() -> publisher.publish(update))
                .expectNext(update)
                .thenCancel()
                .verify();
    }
}
