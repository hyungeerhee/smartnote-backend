package com.smartnote.backend.note.controller;

import com.smartnote.backend.note.dto.AskRequestDto;
import com.smartnote.backend.note.dto.TranslateRequestDto;
import com.smartnote.backend.note.service.NoteService;
import com.smartnote.backend.note.domain.Note;
import com.smartnote.backend.user.domain.User;
import com.smartnote.backend.openai.service.OpenAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Tag(name = "AI 기능", description = "GPT를 활용한 요약, 맞춤법 검사, 질문 응답, 번역 기능 API")
public class NoteAiController {

    private final NoteService noteService;
    private final OpenAiService openAiService;

    @Operation(summary = "메모 요약", description = "지정한 메모의 내용을 요약합니다.")
    @PostMapping("/{id}/summary")
    public ResponseEntity<String> summarizeNote(
            @Parameter(description = "요약할 메모 ID", required = true) @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        Note note = noteService.getNoteEntity(id, user);
        String summary = openAiService.generateSummary(note.getContent());
        return ResponseEntity.ok(summary);
    }

    @Operation(summary = "맞춤법 검사", description = "지정한 메모의 내용을 맞춤법 및 문법 교정합니다.")
    @PostMapping("/{id}/spellcheck")
    public ResponseEntity<String> spellcheckNote(
            @Parameter(description = "검사할 메모 ID", required = true) @PathVariable Long id,
            @AuthenticationPrincipal User user) {

        Note note = noteService.getNoteEntity(id, user);
        String corrected = openAiService.checkSpelling(note.getContent());
        return ResponseEntity.ok(corrected);
    }

    @Operation(summary = "GPT 질문", description = "메모 내용을 바탕으로 GPT에게 질문하고 응답을 받습니다.")
    @PostMapping("/{id}/ask")
    public ResponseEntity<String> askQuestionAboutNote(
            @PathVariable Long id,
            @RequestBody AskRequestDto request,
            @AuthenticationPrincipal User user) {
        Note note = noteService.getNoteEntity(id, user);
        String question = request.getQuestion();
        String answer = openAiService.askAboutNote(note.getContent(), question);
        return ResponseEntity.ok(answer);
    }


    @Operation(summary = "메모 번역", description = "지정한 언어로 메모를 번역합니다. 예: en, ja, ko 등")
    @PostMapping("/{id}/translate")
    public ResponseEntity<String> translateNote(
            @PathVariable Long id,
            @RequestBody TranslateRequestDto request,
            @AuthenticationPrincipal User user) {
        Note note = noteService.getNoteEntity(id, user);
        String lang = request.getLang();
        String translated = openAiService.translateNote(note.getContent(), lang);
        return ResponseEntity.ok(translated);
    }
}