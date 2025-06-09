package com.example.onlineexamplatform.domain.userCategory.entity;

import com.example.onlineexamplatform.common.entity.BaseEntity;
import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor

/**
 * 사용자와 카테고리 간의 관계를 나타내는 엔티티
 * 사용자가 어떤 시험 카테고리에 응시할 수 있는 권한을 가지고 있는지를 저장
 * 하나의 사용자(User)는 여러 카테고리(Category)에 응시할 수 있으며,
 * 하나의 카테고리도 여러 사용자와 매핑될 수 있으므로 다대다(N:N) 관계를
 * 중간 엔티티(UserCategory)로 표현
 */
public class UserCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    public UserCategory(User user, Category category) {
        this.user = user;
        this.category = category;
    }
}
