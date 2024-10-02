package com.fastcampus.fastcampusprojectboard.dto.response;

import com.fastcampus.fastcampusprojectboard.dto.ArticleCommentDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        String email,
        String nickname,
        String userId,
        Long parentCommentId,
        Set<ArticleCommentResponse> childComments
) {

    public static ArticleCommentResponse of(Long id,
                                            String content,
                                            LocalDateTime createdAt,
                                            String email,
                                            String nickname,
                                            String userId) {
        return ArticleCommentResponse.of(id, content, createdAt, email, nickname, userId, null);
    }

    public static ArticleCommentResponse of(Long id,
                                            String content,
                                            LocalDateTime createdAt,
                                            String email,
                                            String nickname,
                                            String userId,
                                            Long parentCommentId) {

        // 대댓글은 createdAt 기준으로 오름차순 정렬되도록 한다.
        // 만약 createdAt이 동일하다면 (거의 그럴 일은 없겠지만) id 순으로 정렬한다.
        Comparator<ArticleCommentResponse> childCommentComparator = Comparator
                .comparing(ArticleCommentResponse::createdAt)
                .thenComparingLong(ArticleCommentResponse::id);

        // 정렬 순서를 보장하기 위해 TreeSet을 사용한다.
        return new ArticleCommentResponse(id,
                content,
                createdAt,
                email,
                nickname,
                userId,
                parentCommentId,
                new TreeSet<>(childCommentComparator));
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return ArticleCommentResponse.of(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId(),
                dto.parentCommentId()
        );
    }

    public boolean hasParentComment() {
        return parentCommentId != null;
    }

}
