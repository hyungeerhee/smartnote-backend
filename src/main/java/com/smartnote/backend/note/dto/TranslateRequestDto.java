package com.smartnote.backend.note.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TranslateRequestDto {

    @Schema(description = "번역할 언어 코드", example = "en")
    private String lang;
}