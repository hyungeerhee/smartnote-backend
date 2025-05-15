package com.smartnote.backend;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class AiRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "note_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ai_log_note"))
    private Note note;

    @Column(nullable = false, length = 20)
    private String type;

    @Lob
    private String requestText;

    @Lob
    private String resultText;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}