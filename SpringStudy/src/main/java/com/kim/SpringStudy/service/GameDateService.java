package com.kim.SpringStudy.service;

import com.kim.SpringStudy.dto.GameDateDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface GameDateService {

    List<GameDateDTO> getGamesByDate(String date);
    List<String> getAvailableDates();
    String getLatestAvailableDate();
}
