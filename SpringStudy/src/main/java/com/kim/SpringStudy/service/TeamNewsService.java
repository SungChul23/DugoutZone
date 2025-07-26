package com.kim.SpringStudy.service;


import com.kim.SpringStudy.dto.NewsDTO;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TeamNewsService {

    @Value("${naver.api.client-id}")
    private String clientId;
    @Value("${naver.api.client-secret}")
    private String clientSecret;

    //구단 별 뉴스
    public List<NewsDTO> getNewsByTeam(String teamKeyword, int page, int display) {
        try {
            String query = URLEncoder.encode(teamKeyword + " 야구", StandardCharsets.UTF_8);
            int start = (page - 1) * display + 1;
            String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + query
                    + "&display=" + display
                    + "&start=" + start
                    + "&sort=date";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiURL))
                    .header("X-Naver-Client-Id", clientId)
                    .header("X-Naver-Client-Secret", clientSecret)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject json = new JSONObject(response.body());
            JSONArray items = json.getJSONArray("items");

            List<NewsDTO> newsList = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                NewsDTO news = new NewsDTO();
                news.setTitle(item.getString("title").replaceAll("<.*?>", ""));
                news.setUrl(item.getString("link"));
                news.setSummary(item.getString("description").replaceAll("<.*?>", ""));
                news.setPubDate(item.getString("pubDate"));
                newsList.add(news);
            }
            return newsList;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


}
