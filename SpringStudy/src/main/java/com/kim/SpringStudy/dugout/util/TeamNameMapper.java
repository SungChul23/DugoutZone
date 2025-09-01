package com.kim.SpringStudy.dugout.util;

import java.util.Map;

//어떤팀은 영어고 어떤팀은 한글 -> 애초에 처음 DB설계 했을떄 모두 영어로 했어야했음
//ex) LG에 접속하면 url 경로는 소문자로 매핑
public class TeamNameMapper {

    private static final Map<String, String> TEAM_MAP = Map.ofEntries(
            Map.entry("LG", "lg"),
            Map.entry("SSG", "ssg"),
            Map.entry("KIA", "kia"),
            Map.entry("KT", "kt"),
            Map.entry("NC", "nc"),
            Map.entry("DOOSAN", "doosan"),
            Map.entry("두산", "doosan"),
            Map.entry("LOTTE", "lotte"),
            Map.entry("롯데", "lotte"),
            Map.entry("SAMSUNG", "samsung"),
            Map.entry("삼성", "samsung"),
            Map.entry("KIWOOM", "kiwoom"),
            Map.entry("키움", "kiwoom"),
            Map.entry("HANWHA", "hanwha"),
            Map.entry("한화", "hanwha")
    );

    private static final Map<String, String> DB_TEAM_NAME_MAP = Map.ofEntries(
            Map.entry("LG", "LG"),
            Map.entry("SSG", "SSG"),
            Map.entry("KIA", "KIA"),
            Map.entry("KT", "KT"),
            Map.entry("NC", "NC"),
            Map.entry("DOOSAN", "두산"),
            Map.entry("두산", "두산"),

            Map.entry("LOTTE", "롯데"),
            Map.entry("롯데", "롯데"),

            Map.entry("SAMSUNG", "삼성"),
            Map.entry("삼성", "삼성"),

            Map.entry("KIWOOM", "키움"),
            Map.entry("키움", "키움"),

            Map.entry("HANWHA", "한화"),
            Map.entry("한화", "한화")
    );
    public static String toViewFolder(String input) {
        if (input == null) return null;
        String key = input.trim().toLowerCase();

        // 소문자 매핑
        return switch (key) {
            case "lg" -> "lg";
            case "ssg" -> "ssg";
            case "kia" -> "kia";
            case "kt" -> "kt";
            case "nc" -> "nc";
            case "doosan", "두산" -> "doosan";
            case "lotte", "롯데" -> "lotte";
            case "samsung", "삼성" -> "samsung";
            case "kiwoom", "키움" -> "kiwoom";
            case "hanwha", "한화" -> "hanwha";
            default -> null;
        };
    }
    public static String toDbTeamName(String input) {
        return DB_TEAM_NAME_MAP.getOrDefault(input.toUpperCase(), null);
    }
}
