package com.fastcampus.fastcampusprojectboard.repository;

import com.fastcampus.fastcampusprojectboard.domain.Article;
import com.fastcampus.fastcampusprojectboard.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("newUno", "pw", null, null, null));
        Article article = Article.of(userAccount, "new article", "new content", "#spring");

        // When
        articleRepository.save(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount + 1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);

        // When
        // flush 해주지 않으면 update 이후 데이터를 사용하지 않기 때문에 update 쿼리가 실행되지 않음
        Article savedArticle = articleRepository.saveAndFlush(article);

        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updatedHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size();

        // When
        articleRepository.delete(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentSize);
    }


    // security 와는 분리되어서 동작
    // 기존에는 JpaConfig에서 createdBy 필드에 들어갈 데이터를 임의로 반환했지만,
    // 이제는 인증된 유저가 들어갈 수 있도록 수정했기 때문에 insert 테스트에서 에러가 발생했다.
    // 이를 해결하기 위해 테스트에서 사용할 전용 config 클래스를 생성해주었다.
    @EnableJpaAuditing
    @TestConfiguration  // 테스트할 때만 등록되는 Bean
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("uno");
        }
    }
}
