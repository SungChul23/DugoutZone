package com.kim.SpringStudy.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//예보 발표 시각 유틸(기상청 API 필수 파라미터)
public class WeatherTimeUtils {
    public static String getTmFc() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime base = now.getHour() < 6
                ? now.minusDays(1).withHour(18).withMinute(0)
                : now.withHour(6).withMinute(0);
        return base.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }
}
