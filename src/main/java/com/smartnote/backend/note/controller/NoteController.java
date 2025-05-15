package com.smartnote.backend.note.controller;

import com.smartnote.backend.note.service.NoteService;
import com.smartnote.backend.note.dto.NoteRequestDto;
import com.smartnote.backend.note.dto.NoteResponseDto;
import com.smartnote.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    // 메모 생성
    @PostMapping
    public void createNote(@RequestBody NoteRequestDto dto, @AuthenticationPrincipal User user) {
        noteService.createNote(dto, user);
    }

    // 메모 목록 조회
    @GetMapping
    public List<NoteResponseDto> getNotes(@AuthenticationPrincipal User user) {
        return noteService.getUserNotes(user);
    }

    // 메모 상세 조회
    @GetMapping("/{id}")
    public NoteResponseDto getNote(@PathVariable Long id, @AuthenticationPrincipal User user) {
        return noteService.getNote(id, user);
    }

    // 메모 수정
    @PutMapping("/{id}")
    public void updateNote(@PathVariable Long id,
                           @RequestBody NoteRequestDto dto,
                           @AuthenticationPrincipal User user) {
        noteService.updateNote(id, dto, user);
    }

    // 메모 삭제
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id, @AuthenticationPrincipal User user) {
        noteService.deleteNote(id, user);
    }

    // 파일 업로드: 편집기에 넣을 텍스트만 추출해서 반환
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user
    ) {
        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
            if (!List.of("txt", "md", "html", "java", "js").contains(extension)) {
                return ResponseEntity.badRequest().body("허용되지 않은 파일 형식입니다.");
            }

            InputStream inputStream = file.getInputStream();
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            // DB 저장 없이 텍스트만 응답
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("파일 처리 중 오류 발생: " + e.getMessage());
        }
    }
}