package com.smartnote.backend.note.service;

import com.smartnote.backend.note.domain.Note;
import com.smartnote.backend.note.dto.NoteRequestDto;
import com.smartnote.backend.note.dto.NoteResponseDto;
import com.smartnote.backend.note.repository.NoteRepository;
import com.smartnote.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    // 메모 생성
    public void createNote(NoteRequestDto dto, User user) {
        Note note = new Note();
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        note.setUser(user);

        noteRepository.save(note);
    }

    // 메모 전체 목록 조회
    public List<NoteResponseDto> getUserNotes(User user) {
        List<Note> notes = noteRepository.findAllByUserOrderByCreatedAtDesc(user);
        return notes.stream().map(NoteResponseDto::new).collect(Collectors.toList());
    }

    // 단일 메모 조회
    public NoteResponseDto getNote(Long id, User user) {
        Note note = getNoteOrThrow(id);
        validateOwnership(note, user);
        return new NoteResponseDto(note);
    }

    // 메모 수정
    public void updateNote(Long id, NoteRequestDto dto, User user) {
        Note note = getNoteOrThrow(id);
        validateOwnership(note, user);
        note.setTitle(dto.getTitle());
        note.setContent(dto.getContent());
        noteRepository.save(note);
    }

    // 메모 삭제
    public void deleteNote(Long id, User user) {
        Note note = getNoteOrThrow(id);
        validateOwnership(note, user);
        noteRepository.delete(note);
    }

    // 유틸
    private Note getNoteOrThrow(Long id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
    }

    private void validateOwnership(Note note, User user) {
        if (!note.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }

    // NoteService.java 안에 추가
    public Note getNoteEntity(Long id, User user) {
        Note note = getNoteOrThrow(id);
        validateOwnership(note, user);
        return note;
    }

}