package com.smartnote.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final OpenAiConfig openAiConfig;

    // 메모 내용을 3줄로 요약 요청
    public String generateSummary(String content) {
        String url = openAiConfig.getUrl();

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "넌 사용자의 메모를 정리해주는 AI 요약 도우미야."),
                        Map.of("role", "user", "content", "다음 텍스트 내용을 3줄로 요약해줘: " + content)
                )
        );

        return sendRequest(url, requestBody);
    }

    // 맞춤법과 문법 교정 요청
    public String checkSpelling(String content) {
        String url = openAiConfig.getUrl();

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "user", "content", "다음 문장에서 맞춤법과 문법을 모두 고쳐줘:\n" + content)
                )
        );

        return sendRequest(url, requestBody);
    }

    // 메모 내용을 기반으로 질문에 대한 답변 생성
    public String askAboutNote(String content, String question) {
        String url = openAiConfig.getUrl();

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "넌 사용자의 노트를 이해하고 질문에 답변해주는 AI야."),
                        Map.of("role", "user", "content", "다음은 사용자의 메모야:\n" + content),
                        Map.of("role", "user", "content", "질문: " + question)
                )
        );

        return sendRequest(url, requestBody);
    }

    // 메모 내용을 지정한 언어로 번역
    public String translateNote(String content, String lang) {
        String url = openAiConfig.getUrl();

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "넌 사용자의 메모를 원하는 언어로 자연스럽게 번역해주는 번역 도우미야."),
                        Map.of("role", "user", "content", "다음 문장을 " + lang + " 언어로 번역해줘:\n" + content)
                )
        );

        return sendRequest(url, requestBody);
    }

    // 실제 OpenAI API로 요청을 보내고 응답 결과를 파싱해서 반환
    private String sendRequest(String url, Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiConfig.getKey());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        Map<String, Object> choices = ((List<Map<String, Object>>) response.getBody().get("choices")).get(0);
        Map<String, Object> message = (Map<String, Object>) choices.get("message");

        return (String) message.get("content");
    }
}