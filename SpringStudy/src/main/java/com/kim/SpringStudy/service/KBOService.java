package com.kim.SpringStudy.service;


import com.kim.SpringStudy.domain.KBO;
import com.kim.SpringStudy.repository.KBORepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KBOService {

    private final KBORepository kboRepository;

    public List<KBO> getTeamRankByDate(LocalDate date) {
        return kboRepository.findByRecordDateOrderByRankNumAsc(date);
    }

    public List<LocalDate> getAvailableDates() {
        return kboRepository.findAllRecordDates();
    }
}
