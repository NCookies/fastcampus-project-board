package com.fastcampus.fastcampusprojectboard.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleUpdateDto (
    String title,
    String content,
    String hashTag
) {
    public static ArticleUpdateDto of(String title, String content, String hashTag) {
        return new ArticleUpdateDto(title, content, hashTag);
    }
}
