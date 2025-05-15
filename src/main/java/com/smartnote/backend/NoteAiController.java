// NoteAiController.java
package com.smartnote.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteAiController {

    private final NoteService noteService;
    private final OpenAiService openAiService;

    @PostMapping("/{id}/summary")
    public ResponseEntity<String> summarizeNote(@PathVariable Long id, @AuthenticationPrincipal User user) {
        // 1. 사용자의 노트를 불러옴
        Note note = noteService.getNoteEntity(id, user);

        // 2. 메모 내용을 기반으로 요약 요청
        String summary = openAiService.generateSummary(note.getContent());

        // 3. 결과 반환
        return ResponseEntity.ok(summary);
    }
}