package com.fastcampus.fastcampusprojectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목
    @Setter @Column(nullable = false)
    private String title;

    // 본문
    @Setter @Column(nullable = false, length = 10000)
    private String content;

    // 해시태그
    @Setter
    private String hashtag;

    /*
     * 여기에서 ToString.Exclude를 하는 이유
     * Article 클래스의 lombok ToString 옵션을 설정해두었는데,
     * 이렇게 되면 ArticleComment를 Set으로 가지는 구현체를 String으로 구현하고,
     * ArticleComment 내부에 있는 article 객체를 String으로 만들게 되면서 순환참조가 발생한다.
     * 이를 막기 위해 해당 옵션을 사용하는 것
     */
    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();


    // 생성일시
    @CreatedDate @Column(nullable = false)
    private LocalDateTime createdAt;

    // 생성자
    @CreatedBy @Column(nullable = false, length = 100)
    private String createdBy;

    // 수정일시
    @LastModifiedDate @Column(nullable = false)
    private LocalDateTime modifiedAt;

    // 수정자
    @LastModifiedBy @Column(nullable = false, length = 100)
    private String modifiedBy;


    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
