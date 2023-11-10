package com.ybe.ybe_toyproject3.global.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
public class KakaoApiComponent {
    @Value("${kakao.key}")
    private String kakaoApiKey;

    @Value("${kakao.url}")
    private String kakaoApiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoApiComponent(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String getLocationNameByPlaceName(String placeName) {
        URI targetUrl = UriComponentsBuilder
                .fromUriString(kakaoApiUrl)
                .queryParam("query", placeName)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "KakaoAK " + kakaoApiKey);
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET, httpEntity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String responseBody = responseEntity.getBody();

            try {
                JsonNode responseNode = objectMapper.readTree(responseBody);
                JsonNode documents = responseNode.path("documents");

                if (documents.isArray() && documents.size() > 0) {
                    JsonNode firstDocument = documents.get(0);
                    return firstDocument.path("place_name").asText();
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
