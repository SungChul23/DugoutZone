package com.kim.SpringStudy.util;

import java.util.Map;

//구단 별 지역 코드(날씨 예보)
public class RegionCodeMapper {
    public static final Map<String, String> REGION_CODE_MAP = Map.ofEntries(
            Map.entry("LG", "11B10101"),
            Map.entry("두산", "11B10101"),
            Map.entry("키움", "11B10101"),
            Map.entry("SSG", "11B20201"),
            Map.entry("KT", "11B20601"),
            Map.entry("NC", "11H20301"),
            Map.entry("삼성", "11H10701"),
            Map.entry("롯데", "11H20201"),
            Map.entry("KIA", "11F20501"),
            Map.entry("한화", "11C20401")
    );
}
