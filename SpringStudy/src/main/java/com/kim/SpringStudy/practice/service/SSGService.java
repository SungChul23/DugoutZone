package com.kim.SpringStudy.practice.service;

import com.kim.SpringStudy.practice.domain.SSG;
import com.kim.SpringStudy.practice.domain.SSGGames;
import com.kim.SpringStudy.practice.repository.SSGGamesRepository;
import com.kim.SpringStudy.practice.repository.SSGRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SSGService {
    private final SSGRepository ssgRepository; // ssg 선수단 레포지 객체
    private final SSGGamesRepository ssgGamesRepository; // ssg 경기일정 레포지 객체;
    public List<SSG> findPlayer(){
        return ssgRepository.findAll();
    }
    public List<SSGGames> findGames(){
        return ssgGamesRepository.findAll();
    }
}
