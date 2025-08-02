package com.kim.SpringStudy.service;

import com.kim.SpringStudy.dto.GameDateDTO;
import com.kim.SpringStudy.repository.KBOGameDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameDateServiceImpl implements GameDateService{

   private final KBOGameDateRepository repository;

    @Override
    public List<GameDateDTO> getGamesByDate(String date) {
        return repository.findByGameDate(date)
                .stream()
                .map(GameDateDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAvailableDates() {
        return repository.findDistinctGameDates();
    }

    @Override
    public String getLatestAvailableDate() {
        return repository.findLatestGameDate();
    }

}
