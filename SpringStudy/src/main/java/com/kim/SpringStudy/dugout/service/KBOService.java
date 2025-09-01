package com.kim.SpringStudy.dugout.service;


import com.kim.SpringStudy.dugout.domain.KBO;
import com.kim.SpringStudy.dugout.repository.KBORepository;
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
