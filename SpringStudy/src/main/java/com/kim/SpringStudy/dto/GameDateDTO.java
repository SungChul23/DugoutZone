package com.kim.SpringStudy.dto;

import com.kim.SpringStudy.domain.KBOGameDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDateDTO {

    private String time;
    private String matchup;
    private String stadium;

    private String homeTeam;
    private String awayTeam;

    public static GameDateDTO fromEntity(KBOGameDate kboGameDate) {
        String matchup = kboGameDate.getMatchup();
        String home = null;
        String away = null;

        if (matchup != null && matchup.contains("vs")) {
            String[] parts = matchup.split("vs");
            if (parts.length == 2) {
                home = parts[0].trim();
                away = parts[1].trim();
            }
        }

        return new GameDateDTO(
                kboGameDate.getTime(),
                matchup,
                kboGameDate.getStadium(),
                home,
                away
        );
    }
}

