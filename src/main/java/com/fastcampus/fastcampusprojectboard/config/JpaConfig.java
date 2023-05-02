package com.fastcampus.fastcampusprojectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        // Article 테이블의 생성자 데이터를 삽입할 때 사용
        // 현재는 임의로 "uno"라는 값을 넣어주고 있음
        return () -> Optional.of("uno");    // TODO: 스프링 시큐리티로 인증 기능을 붙이게 될 때, 수정해야함
    }
}
