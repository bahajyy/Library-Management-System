package com.getir.library_management_system.model.entity;

import com.getir.library_management_system.model.enums.Genre;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private String isbn;

    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private Boolean available;
}
