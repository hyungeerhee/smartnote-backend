package com.smartnote.backend.note.controller;

import com.smartnote.backend.note.service.NoteService;
import com.smartnote.backend.note.domain.Note;
import com.smartnote.backend.user.domain.User;
import com.smartnote.backend.openai.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteAiController {

    private final NoteService noteService;
    private final OpenAiService openAiService;

    // 특정 메모에 대해 요약 요청
    @PostMapping("/{id}/summary")
    public ResponseEntity<String> summarizeNote(@PathVariable Long id, @AuthenticationPrincipal User user) {
        // 사용자 본인의 메모 가져오기
        Note note = noteService.getNoteEntity(id, user);

        // 메모 내용을 기반으로 요약 실행
        String summary = openAiService.generateSummary(note.getContent());

        // 결과 반환
        return ResponseEntity.ok(summary);
    }

    // 맞춤법 검사 요청
    @PostMapping("/{id}/spellcheck")
    public ResponseEntity<String> spellcheckNote(@PathVariable Long id, @AuthenticationPrincipal User user) {
        // 해당 사용자의 메모 조회
        Note note = noteService.getNoteEntity(id, user);

        // 메모 내용을 기반으로 맞춤법 검사 실행
        String corrected = openAiService.checkSpelling(note.getContent());

        // 결과 반환
        return ResponseEntity.ok(corrected);
    }

    // 메모에 대해 질문하고 답변 받기
    @PostMapping("/{id}/ask")
    public ResponseEntity<String> askQuestionAboutNote(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal User user
    ) {
        // 메모 내용 조회
        Note note = noteService.getNoteEntity(id, user);

        // 프론트에서 전달한 질문 추출
        String question = request.get("question");

        // 질문을 GPT에게 전달하고 응답 받기
        String answer = openAiService.askAboutNote(note.getContent(), question);

        return ResponseEntity.ok(answer);
    }

    // 메모 번역 기능
    @PostMapping("/{id}/translate")
    public ResponseEntity<String> translateNote(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal User user
    ) {
        // 메모 조회
        Note note = noteService.getNoteEntity(id, user);

        // 요청에서 번역 대상 언어 파라미터 받기 (예: en, ja, zh 등)
        String lang = request.get("lang");

        // 메모 내용을 해당 언어로 번역
        String translated = openAiService.translateNote(note.getContent(), lang);

        return ResponseEntity.ok(translated);
    }
}