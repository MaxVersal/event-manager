package ru.practicum.category.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryService {

    ResponseEntity<Category> createCategory(Category category);

    ResponseEntity<Category> pathCategory(Long catId, Category category);

    ResponseEntity<String> deleteCategory(Long catId);

    ResponseEntity<Category> getCategoryById(Long id);

    List<Category> getAllCategories(Integer from, Integer size);
}
