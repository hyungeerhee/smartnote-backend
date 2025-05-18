package com.smartnote.backend.note.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class NoteRequestDto {

    @Schema(description = "메모 제목", example = "오늘의 회의 요약")
    private String title;

    @Schema(description = "메모 내용", example = "오전 회의에서 논의된 프로젝트 일정과 업무 분담 정리")
    private String content;
}