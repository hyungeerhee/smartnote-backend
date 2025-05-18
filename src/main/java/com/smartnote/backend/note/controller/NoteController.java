package com.smartnote.backend.note.controller;

import com.smartnote.backend.note.service.NoteService;
import com.smartnote.backend.note.dto.NoteRequestDto;
import com.smartnote.backend.note.dto.NoteResponseDto;
import com.smartnote.backend.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Note", description = "메모 CRUD 및 파일 업로드 API")
@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @Operation(summary = "메모 생성", description = "제목과 내용을 입력받아 새 메모를 생성합니다.")
    @PostMapping
    public void createNote(@RequestBody NoteRequestDto dto,
                           @AuthenticationPrincipal User user) {
        noteService.createNote(dto, user);
    }

    @Operation(summary = "메모 목록 조회", description = "사용자가 작성한 모든 메모의 목록을 조회합니다.")
    @GetMapping
    public List<NoteResponseDto> getNotes(@AuthenticationPrincipal User user) {
        return noteService.getUserNotes(user);
    }

    @Operation(summary = "메모 상세 조회", description = "지정한 ID에 해당하는 메모의 상세 내용을 조회합니다.")
    @GetMapping("/{id}")
    public NoteResponseDto getNote(
            @Parameter(description = "조회할 메모 ID") @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        return noteService.getNote(id, user);
    }

    @Operation(summary = "메모 수정", description = "ID로 메모를 찾아 제목과 내용을 수정합니다.")
    @PutMapping("/{id}")
    public void updateNote(
            @Parameter(description = "수정할 메모 ID") @PathVariable Long id,
            @RequestBody NoteRequestDto dto,
            @AuthenticationPrincipal User user) {
        noteService.updateNote(id, dto, user);
    }

    @Operation(summary = "메모 삭제", description = "지정한 ID의 메모를 삭제합니다.")
    @DeleteMapping("/{id}")
    public void deleteNote(
            @Parameter(description = "삭제할 메모 ID") @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        noteService.deleteNote(id, user);
    }

    @Operation(summary = "파일 업로드", description = "txt, md, html, java, js 파일을 업로드하여 텍스트를 추출합니다. 추출된 내용은 편집기에 삽입하기 위한 용도로 반환됩니다.")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @Parameter(description = "업로드할 파일") @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) {
        try {
            String extension = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
            if (!List.of("txt", "md", "html", "java", "js").contains(extension)) {
                return ResponseEntity.badRequest().body("허용되지 않은 파일 형식입니다.");
            }

            InputStream inputStream = file.getInputStream();
            String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("파일 처리 중 오류 발생: " + e.getMessage());
        }
    }
}