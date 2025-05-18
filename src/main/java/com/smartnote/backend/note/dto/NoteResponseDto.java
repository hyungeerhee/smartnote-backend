package com.smartnote.backend.note.dto;

import com.smartnote.backend.note.domain.Note;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoteResponseDto {

    @Schema(description = "메모 ID", example = "1")
    private Long id;

    @Schema(description = "메모 제목", example = "오늘의 회의 요약")
    private String title;

    @Schema(description = "메모 내용", example = "오전 회의에서 논의된 프로젝트 일정과 업무 분담 정리")
    private String content;

    @Schema(description = "메모 생성일", example = "2025-05-17T09:30:00")
    private LocalDateTime createdAt;

    public NoteResponseDto(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.createdAt = note.getCreatedAt();
    }
}