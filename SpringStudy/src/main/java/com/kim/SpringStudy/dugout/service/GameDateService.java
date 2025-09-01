package com.kim.SpringStudy.dugout.service;

import com.kim.SpringStudy.dugout.dto.GameDateDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface GameDateService {

    List<GameDateDTO> getGamesByDate(String date);
    List<String> getAvailableDates();
    String getLatestAvailableDate();
}
