package com.fastcampus.fastcampusprojectboard.domain.projection;

import com.fastcampus.fastcampusprojectboard.domain.UserAccount;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

// API 문서에서 확인할 수 있는 정보들을 직접 설정
@Projection(name = "withoutPassword", types = { UserAccount.class })
public interface UserAccountProjection {
    String getUserId();
    String getEmail();
    String getNickname();
    String getMemo();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getModifiedAt();
    String getModifiedBy();
}
