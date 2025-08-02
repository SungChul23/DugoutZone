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


    public static GameDateDTO fromEntity(KBOGameDate kboGameDate) {
        return new GameDateDTO(
                kboGameDate.getTime(),
                kboGameDate.getMatchup(),
                kboGameDate.getStadium()
        );
    }
}
