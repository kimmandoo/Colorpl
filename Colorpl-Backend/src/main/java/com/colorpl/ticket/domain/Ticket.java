package com.colorpl.ticket.domain;

import com.colorpl.show.domain.detail.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Column(name = "TICKET_ID")
    @GeneratedValue
    @Id
    private Long id;

    @Column(name = "TICKET_CATEGORY")
    private Category category;

    @Column(name = "TICKET_NAME")
    private String name;

    @Column(name = "TICKET_DATE_TIME")
    private LocalDateTime dateTime;

    @Column(name = "TICKET_THEATER")
    private String theater;

    @Column(name = "TICKET_IMAGE_PATH")
    private String imagePath;
}
