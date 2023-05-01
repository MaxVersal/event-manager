package ru.practicum.pub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping("/{catId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable(name = "catId") Long catId) {
        return categoryService.getCategoryById(catId);
    }

    @GetMapping
    public List<Category> getAllCategories(@RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                           @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return categoryService.getAllCategories(from, size);
    }
}
