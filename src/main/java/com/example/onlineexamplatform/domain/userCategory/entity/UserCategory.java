package com.example.onlineexamplatform.domain.userCategory.entity;

import com.example.onlineexamplatform.domain.category.entity.Category;
import com.example.onlineexamplatform.domain.category.entity.CategoryName;
import com.example.onlineexamplatform.domain.user.entity.User;
import jakarta.persistence.*;

@Entity
public class UserCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Category categoryType;
}
