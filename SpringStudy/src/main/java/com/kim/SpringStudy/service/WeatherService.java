package com.kim.SpringStudy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kim.SpringStudy.dto.WeeklyWeatherDTO;
import com.kim.SpringStudy.util.RegionCodeMapper;
import com.kim.SpringStudy.util.WeatherTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class WeatherService {

    // ✅ 디코딩하지 않은 원문 키 그대로
    private final String serviceKey = "/s+rQLB5rUo9hpfs50PP6YRptMJnyTKYVX0RDCBAogExOCk6K4SRoi+eDwvlFKJiaHJ5kNLA6Mf4Fl6dTzzZHA==";
    String encodedKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
            result.add(dto);
        }
        return result;
    }

    private JsonNode callApi(String type, String regId, String tmFc) {
        try {
            String url = "https://apis.data.go.kr/1360000/MidFcstInfoService/" + type +
                    "?serviceKey=" + encodedKey  +
                    "&regId=" + regId +
                    "&tmFc=" + tmFc +
                    "&dataType=JSON";

            System.out.println("🔍 최종 URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "Mozilla/5.0");
            headers.set("Accept", "application/json");

            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            System.out.println("📄 Raw Response:\n" + response.getBody());

            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            System.out.println("❗ API 호출 예외 발생 (" + type + "): " + e.getMessage());
            return null;
        }
    }

    private String getDateAfter(int dayOffset) {
        LocalDate date = LocalDate.now().plusDays(dayOffset);
        String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN); // ex. 금
        return date.format(java.time.format.DateTimeFormatter.ofPattern("MM.dd")) + "(" + dayOfWeek + ")";
    }
}
