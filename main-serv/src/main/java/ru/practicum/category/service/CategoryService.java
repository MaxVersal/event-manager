package ru.practicum.category.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.category.model.Category;

import javax.transaction.Transactional;
import java.util.List;

public interface CategoryService {
    @Transactional
    ResponseEntity<Category> createCategory(Category category);

    @Transactional
    ResponseEntity<Category> pathCategory(Long catId, Category category);

    @Transactional
    ResponseEntity<String> deleteCategory(Long catId);

    @Transactional
    ResponseEntity<Category> getCategoryById(Long id);

    @Transactional
    List<Category> getAllCategories(Integer from, Integer size);
}
