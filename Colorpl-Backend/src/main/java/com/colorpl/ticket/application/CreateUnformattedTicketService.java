package com.colorpl.ticket.application;

import com.colorpl.global.common.storage.StorageService;
import com.colorpl.global.common.storage.UploadFile;
import com.colorpl.show.domain.detail.Category;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateUnformattedTicketService {

    private final StorageService storageService;
    private final TicketRepository ticketRepository;

    public Ticket create(CreateUnformattedTicketRequest request, MultipartFile attachFile) {
        UploadFile uploadFile = storageService.storeFile(attachFile);
        Ticket ticket = Ticket.builder()
            .category(Category.fromString(request.getCategory()).orElseThrow())
            .name(request.getName())
            .dateTime(LocalDateTime.parse(request.getDateTime()))
            .theater(request.getTheater())
            .filename(uploadFile.getStoreFilename())
            .build();
        return ticketRepository.save(ticket);
    }
}
