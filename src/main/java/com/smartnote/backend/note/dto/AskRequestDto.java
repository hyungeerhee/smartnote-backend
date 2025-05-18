package com.smartnote.backend.note.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AskRequestDto {

    @Schema(description = "GPT에게 보낼 질문", example = "이 메모에서 핵심 주제는 무엇인가요?")
    private String question;
}