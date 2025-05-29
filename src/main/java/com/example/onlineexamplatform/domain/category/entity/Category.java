package com.example.onlineexamplatform.domain.category.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Category {

    @Id
    @Enumerated(EnumType.STRING)
    private CategoryName categoryType;
}
