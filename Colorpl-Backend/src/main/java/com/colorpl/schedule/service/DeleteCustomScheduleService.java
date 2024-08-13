package com.colorpl.schedule.service;

import com.colorpl.global.common.exception.ReviewNotFoundException;
import com.colorpl.global.common.exception.ScheduleNotFoundException;
import com.colorpl.global.storage.StorageService;
import com.colorpl.review.service.ReviewService;
import com.colorpl.schedule.domain.CustomSchedule;
import com.colorpl.schedule.repository.CustomScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCustomScheduleService {

    private final CustomScheduleRepository customScheduleRepository;
    private final StorageService storageService;
    private final ReviewService reviewService;

    @Transactional
    public void deleteCustomSchedule(Long id) {

        CustomSchedule customSchedule = customScheduleRepository.findById(id)
            .orElseThrow(() -> new ScheduleNotFoundException(id));

        if (customSchedule.getImage() != null) {
            storageService.deleteFile(customSchedule.getImage());
        }

        if (customSchedule.getReview().isPresent()) {
            reviewService.deleteById(customSchedule.getReview().orElseThrow(
                ReviewNotFoundException::new).getId());
        }

        customScheduleRepository.deleteById(id);
    }
}
