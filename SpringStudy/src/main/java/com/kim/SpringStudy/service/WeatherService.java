package com.kim.SpringStudy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kim.SpringStudy.dto.WeeklyWeatherDTO;
import com.kim.SpringStudy.util.RegionCodeMapper;
import com.kim.SpringStudy.util.WeatherTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // 인증키 (디코딩된 키)
    private final String rawServiceKey = "ygDZXhJPFrHyakEPJLCrFMaRy9IN6cDU38RWPzBumZaoEhyqBlw5GF5rR9Fe3fk+CYWap4XQ/zt3g14Eb7RXcg==";

    public List<WeeklyWeatherDTO> getWeatherForTeam(String teamName) {
        String regId = RegionCodeMapper.REGION_CODE_MAP.getOrDefault(teamName.toUpperCase(), null);
        if (regId == null) return Collections.emptyList();

        String tmFc = WeatherTimeUtils.getTmFc();

        System.out.println("✅ regId: " + regId);
        System.out.println("✅ tmFc: " + tmFc);

        JsonNode land = callApi("getMidLandFcst", regId, tmFc);
        JsonNode temp = callApi("getMidTa", regId, tmFc);

        System.out.println("✅ land null? " + (land == null));
        System.out.println("✅ temp null? " + (temp == null));

        if (land == null || temp == null) {
            System.out.println("❌ API 응답 실패. 빈 리스트 리턴");
            return Collections.emptyList();
        }

        JsonNode landItem = land.path("response").path("body").path("items").path("item").get(0);
        JsonNode tempItem = temp.path("response").path("body").path("items").path("item").get(0);

        List<WeeklyWeatherDTO> result = new ArrayList<>();
        for (int i = 5; i <= 10; i++) {
            WeeklyWeatherDTO dto = new WeeklyWeatherDTO();
            dto.setDay(i);
            dto.setDate(getDateAfter(i));
            dto.setWeatherAm(landItem.path("wf" + i + "Am").asText(null));
            dto.setWeatherPm(landItem.path("wf" + i + "Pm").asText(null));
            dto.setMinTemp(tempItem.path("taMin" + i).asInt());
            dto.setMaxTemp(tempItem.path("taMax" + i).asInt());

            // 🌧️ 강수 확률
            if (i <= 7) {
                dto.setRainProbAm(landItem.path("rnSt" + i + "Am").asInt());
                dto.setRainProbPm(landItem.path("rnSt" + i + "Pm").asInt());
            } else {
                // 8~10일 후는 오전/오후 구분 없이 하나의 값 제공
                int rain = landItem.path("rnSt" + i).asInt();
                dto.setRainProbAm(rain);
                dto.setRainProbPm(rain);
            }
            result.add(dto);
        }

        return result;
    }

    private JsonNode callApi(String type, String regId, String tmFc) {
        try {
            String encodedKey = URLEncoder.encode(rawServiceKey, StandardCharsets.UTF_8);
            String urlStr = "https://apis.data.go.kr/1360000/MidFcstInfoService/" + type +
                    "?serviceKey=" + encodedKey +
                    "&regId=" + regId +
                    "&tmFc=" + tmFc +
                    "&dataType=JSON";

            System.out.println("🔍 최종 URL: " + urlStr);

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = conn.getResponseCode();
            System.out.println("✅ 응답 코드: " + responseCode);

            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    (responseCode >= 200 && responseCode <= 300) ? conn.getInputStream() : conn.getErrorStream()
            ));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) sb.append(line);
            rd.close();
            conn.disconnect();

            String body = sb.toString();
            System.out.println("📄 응답 본문:\n" + body);

            // XML 에러 응답 체크
            if (body.contains("<OpenAPI_ServiceResponse>")) {
                System.out.println("❗ XML 응답 감지 → 인증 오류일 수 있음");
                return null;
            }

            return objectMapper.readTree(body);
        } catch (Exception e) {
            System.out.println("❗ API 호출 예외 발생 (" + type + "): " + e.getMessage());
            return null;
        }
    }

    private String getDateAfter(int dayOffset) {
        LocalDate date = LocalDate.now().plusDays(dayOffset);
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        return date.format(java.time.format.DateTimeFormatter.ofPattern("MM.dd")) + "(" + dayOfWeek + ")";
    }
}
