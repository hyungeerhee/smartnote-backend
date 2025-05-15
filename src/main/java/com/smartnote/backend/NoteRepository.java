package com.smartnote.backend;

import com.smartnote.backend.Note;
import com.smartnote.backend.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUserOrderByCreatedAtDesc(User user);
}