package com.smartnote.backend.note.repository;

import com.smartnote.backend.note.domain.Note;
import com.smartnote.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUserOrderByCreatedAtDesc(User user);
}