package com.freshfood.repository;

import com.freshfood.model.ParentCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentCategoryRepository extends JpaRepository<ParentCategory, Integer> {
}
