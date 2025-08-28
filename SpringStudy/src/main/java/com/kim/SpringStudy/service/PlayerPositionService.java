package com.kim.SpringStudy.service;

import com.kim.SpringStudy.domain.KBOplayerInfo;
import com.kim.SpringStudy.dto.PlayerPositionDTO;
import com.kim.SpringStudy.repository.KBOplayerInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerPositionService {

    private final KBOplayerInfoRepository playerRepo;

    public ResponseEntity<String> updatePositionSub(PlayerPositionDTO dto) {
        List<KBOplayerInfo> players = playerRepo.findAllByNameKr(dto.getNameKr());

        if (players.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ 선수 없음: " + dto.getNameKr());
        }

        for (KBOplayerInfo player : players) {
            player.setPositionSub(dto.getPositionSub());
        }

        playerRepo.saveAll(players);

        return ResponseEntity.ok("✅ 업데이트 완료 (" + players.size() + "명): " + dto.getNameKr());
    }
}
