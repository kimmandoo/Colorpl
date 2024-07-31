package com.colorpl.ticket.application;

import com.colorpl.global.common.storage.StorageService;
import com.colorpl.global.common.storage.UploadFile;
import com.colorpl.show.domain.schedule.ShowSchedule;
import com.colorpl.show.domain.schedule.ShowScheduleRepository;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateFormattedTicketService {

    private final ShowScheduleRepository showScheduleRepository;
    private final StorageService storageService;
    private final TicketRepository ticketRepository;

    public Ticket create(Long showScheduleId, MultipartFile attachFile) {
        UploadFile uploadFile = storageService.storeFile(attachFile);
        ShowSchedule showSchedule = showScheduleRepository.findById(showScheduleId).orElseThrow();
        Ticket ticket = Ticket.builder()
            .category(showSchedule.getShowDetail().getCategory())
            .name(showSchedule.getShowDetail().getName())
            .dateTime(showSchedule.getDateTime())
            .filename(uploadFile.getUploadFilename())
            .build();
        return ticketRepository.save(ticket);
    }
}
