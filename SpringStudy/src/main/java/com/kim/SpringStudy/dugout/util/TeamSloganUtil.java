package com.kim.SpringStudy.dugout.util;

import java.util.Map;

public class TeamSloganUtil {

    // 구단 슬로건 매핑
    private static final Map<String, String> SLOGAN_MAP = Map.ofEntries(
            Map.entry("KIA", "압도하라! / V13 ALWAYS KIA TIGERS"),
            Map.entry("SAMSUNG", "BLUE BLOOD, LIONS PRIDE!"),
            Map.entry("LG", "무적 LG! 끝까지 TWINS! 승리를 향해, 하나의 트윈스!"),
            Map.entry("DOOSAN", "HUSTLE DOOGETHER TEAM DOOSAN 2025"),
            Map.entry("KT", "UP! GREAT KT"),
            Map.entry("SSG", "NO LIMITS AMAZING LANDERS"),
            Map.entry("LOTTE", "투혼투지: 승리를 위한 전진"),
            Map.entry("HANWHA", "RIDE THE STORM"),
            Map.entry("NC", "거침없이 가자 LIGHT, NOW!"),
            Map.entry("KIWOOM", "도약 영웅의 서막")
    );

    public static String getSlogan(String teamName) {
        if (teamName == null) return "";
        return SLOGAN_MAP.getOrDefault(teamName.toUpperCase(), "");
    }
}
