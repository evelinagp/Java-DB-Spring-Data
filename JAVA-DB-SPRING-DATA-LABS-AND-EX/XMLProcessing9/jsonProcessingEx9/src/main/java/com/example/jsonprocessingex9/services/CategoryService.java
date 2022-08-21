package com.example.jsonprocessingex9.services;

import com.example.jsonprocessingex9.domain.entities.Category;
import com.example.jsonprocessingex9.domain.models.createModels.CategoryCreateModel;
import com.example.jsonprocessingex9.domain.models.view.categoryModels.CategoryModel;
import com.example.jsonprocessingex9.domain.models.view.categoryModels.CategoryStatsModel;
import com.example.jsonprocessingex9.repositories.CategoryRepository;
import com.example.jsonprocessingex9.services.validators.CategoryValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private ModelMapper mapper;
    private CategoryRepository categoryRepository;
    private CategoryValidator categoryValidator;

    public CategoryService(ModelMapper mapper, CategoryRepository categoryRepository, CategoryValidator categoryValidator) {
        this.mapper = mapper;
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    public void saveAllToDb(List<CategoryCreateModel> categories) {
        categories.stream()
                .filter(this.categoryValidator::isValidCategory)
                .map(category -> this.mapper.map(category, Category.class))
                .forEach(this.categoryRepository::save);
    }

    public void saveToDb(CategoryCreateModel categoryCreateModel) {
        if (!this.categoryValidator.isValidCategory(categoryCreateModel)) {
            return;
        }

        Category category = this.mapper.map(categoryCreateModel, Category.class);

        this.categoryRepository.save(category);
    }

    public List<CategoryModel> getAllCategories() {
        return this.categoryRepository.findAll()
                .stream()
                .map(category -> this.mapper.map(category, CategoryModel.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CategoryStatsModel> getAllCategoryStatistics() {
        List<Category> categories = this.categoryRepository.findAll();

        return categories.stream()
                .map(category -> this.mapper.map(category, CategoryStatsModel.class))
                .collect(Collectors.toList());
    }
}