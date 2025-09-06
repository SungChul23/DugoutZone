package com.kim.SpringStudy.dugout.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//출생년도 파서 유틸
@Component
public class YearResolver {
    private final int MIN_YEAR = LocalDate.now().getYear() - 45;
    private final int MAX_YEAR = LocalDate.now().getYear() - 16;


    public Optional<Integer> resolveYear(String input) {
        Pattern pattern = Pattern.compile("(\\d{2,4})년생");
        Matcher matcher = pattern.matcher(input);
        if (!matcher.find()) return Optional.empty();


        String raw = matcher.group(1);
        if (raw.length() == 4) {
            int y = Integer.parseInt(raw);
            return (y >= MIN_YEAR && y <= MAX_YEAR) ? Optional.of(y) : Optional.empty();
        } else if (raw.length() == 2) {
            int yy = Integer.parseInt(raw);
            int c1 = 1900 + yy;
            int c2 = 2000 + yy;
            boolean c1ok = c1 >= MIN_YEAR && c1 <= MAX_YEAR;
            boolean c2ok = c2 >= MIN_YEAR && c2 <= MAX_YEAR;


            if (c1ok && !c2ok) return Optional.of(c1);
            if (!c1ok && c2ok) return Optional.of(c2);
            if (c1ok) return Optional.of(yy <= 24 ? c2 : c1);
        }
        return Optional.empty();
    }
}