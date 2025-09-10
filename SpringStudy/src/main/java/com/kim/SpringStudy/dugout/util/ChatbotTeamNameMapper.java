package com.kim.SpringStudy.dugout.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChatbotTeamNameMapper {

    //링크 해시맵으로 입력 순서를 유지
    //동일한 팀의 여러 별칭을 등록할 때, 어떤 걸 먼저 잡아줄지 순서를 보장하려는 목적
    private static final Map<String, String> teamMap = new LinkedHashMap<>();

    static {

        // LG
        teamMap.put("lg", "LG");
        teamMap.put("lg트윈스", "LG");
        teamMap.put("LG트윈스", "LG");

        // SSG
        teamMap.put("ssg", "SSG");
        teamMap.put("ssg랜더스", "SSG");

        // KIA
        teamMap.put("kia", "KIA");
        teamMap.put("기아", "KIA");
        teamMap.put("기아타이거즈", "KIA");

        // 롯데
        teamMap.put("롯데", "롯데");
        teamMap.put("롯데자이언츠", "롯데");
        teamMap.put("lotte", "롯데");

        // 키움
        teamMap.put("키움", "키움");
        teamMap.put("키움히어로즈", "키움");
        teamMap.put("wo", "키움");
        teamMap.put("kiwoom", "키움");

        // KT
        teamMap.put("kt", "KT");
        teamMap.put("kt위즈", "KT");

        // NC
        teamMap.put("nc", "NC");
        teamMap.put("nc다이노스", "NC");

        // 한화
        teamMap.put("한화", "한화");
        teamMap.put("한화이글스", "한화");
        teamMap.put("hanwha", "한화");

        // 두산
        teamMap.put("두산", "두산");
        teamMap.put("두산베어스", "두산");
        teamMap.put("doosan", "두산");

        // 삼성
        teamMap.put("삼성", "삼성");
        teamMap.put("삼성라이온즈", "삼성");
        teamMap.put("samsung", "삼성");
    }

    public static String resolve(String input) {
        if (input == null) return null;
        String normalized = input.toLowerCase().replaceAll("\\s", "");

        // 부분 문자열 포함 검색
        for (Map.Entry<String, String> entry : teamMap.entrySet()) {
            if (normalized.contains(entry.getKey())) {
                return entry.getValue(); // DB 기준 값 리턴
            }
        }
        return null; // 매칭 실패 시
    }
}
