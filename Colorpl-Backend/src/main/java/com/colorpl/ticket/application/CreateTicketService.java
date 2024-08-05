package com.colorpl.ticket.application;

import com.colorpl.global.common.storage.StorageService;
import com.colorpl.global.common.storage.UploadFile;
import com.colorpl.member.Member;
import com.colorpl.member.repository.MemberRepository;
import com.colorpl.show.domain.detail.Category;
import com.colorpl.ticket.domain.Ticket;
import com.colorpl.ticket.domain.TicketRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Transactional
public class CreateTicketService {

    private final StorageService storageService;
    private final TicketRepository ticketRepository;
    private final MemberRepository memberRepository;

    public Long create(CreateTicketRequest request, MultipartFile attachFile) {



        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));

        UploadFile uploadFile = storageService.storeFile(attachFile);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");

        Ticket ticket = Ticket.builder()
            .filename(uploadFile.getStoreFilename())
            .name(request.getName())
            .theater(request.getTheater())
            .dateTime(LocalDateTime.parse(request.getDateTime(), formatter))
            .seat(request.getSeat())
            .category(Category.fromString(request.getCategory()).orElseThrow())
            .member(member)
            .build();

        return ticketRepository.save(ticket).getId();
    }
}
