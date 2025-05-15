package com.smartnote.backend.note.dto;

import com.smartnote.backend.note.domain.Note;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoteResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    public NoteResponseDto(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.content = note.getContent();
        this.createdAt = note.getCreatedAt();
    }
}