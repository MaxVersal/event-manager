package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdmCategoryController {

    private final CategoryService service;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return service.createCategory(category);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "catId") Long catId) {
        return service.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<Category> patchCategory(@PathVariable(name = "catId") Long catId, @RequestBody Category category) {
        return service.pathCategory(catId, category);
    }

}
