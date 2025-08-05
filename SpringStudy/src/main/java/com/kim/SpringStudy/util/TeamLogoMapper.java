package com.kim.SpringStudy.util;

import java.util.Map;

public class TeamLogoMapper {

    public static final Map<String, String> teamLogoMap = Map.ofEntries(
            Map.entry("SSG", "ssg"), Map.entry("LG", "lg"),
            Map.entry("DOOSAN", "doosan"), Map.entry("두산", "doosan"),
            Map.entry("KIA", "kia"), Map.entry("KT", "kt"),
            Map.entry("롯데", "lotte"), Map.entry("삼성", "samsung"),
            Map.entry("한화", "hanwha"), Map.entry("NC", "nc"),
            Map.entry("키움", "kiwoom")
    );
}
