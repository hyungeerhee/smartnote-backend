package com.smartnote.backend;

import com.smartnote.backend.User;
import com.smartnote.backend.NoteRequestDto;
import com.smartnote.backend.NoteResponseDto;
import com.smartnote.backend.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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



}