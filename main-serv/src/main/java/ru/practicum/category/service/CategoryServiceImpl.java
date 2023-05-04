package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ResponseEntity<Category> createCategory(Category category) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public ResponseEntity<Category> pathCategory(Long catId, Category category) {
        final Category cat = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id=%d was not found", catId)));
        cat.setName(category.getName());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryRepository.save(cat));
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteCategory(Long catId) {
        final Category cat = categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id=%d was not found", catId)));
        log.info("Removing category with id {}", cat.getId());
        categoryRepository.deleteById(cat.getId());
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(String.format("Category with id=%d has been successfully removed", catId));
    }

    @Override
    public ResponseEntity<Category> getCategoryById(Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(categoryRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(String.format("Category with id=%d was not found", id))));
    }

    @Override
    public List<Category> getAllCategories(Integer from, Integer size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size)).toList();
    }


}
