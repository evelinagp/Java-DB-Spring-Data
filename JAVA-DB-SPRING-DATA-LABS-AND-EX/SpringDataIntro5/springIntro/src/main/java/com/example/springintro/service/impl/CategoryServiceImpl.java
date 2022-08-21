package com.example.springintro.service.impl;

import com.example.springintro.model.entity.Category;
import com.example.springintro.repository.CategoryRepository;
import com.example.springintro.service.CategoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CategoryServiceImpl implements CategoryService {

    public static final String CATEGORIES_FILE_PATH = "src/main/resources/files/categories.txt";

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void seedCategories() throws IOException {
        if (categoryRepository.count() > 0) {
            return;
        }

        Files.readAllLines(Path.of(CATEGORIES_FILE_PATH)).stream().filter(r -> !r.isEmpty())
                .forEach(categoryName -> {
                    Category category = new Category(categoryName);

                    categoryRepository.save(category);
                });
    }

    @Override
    public Set<Category> getRandomCategories() {
        Set<Category> categories = new HashSet<>();

        int randomInt = ThreadLocalRandom.current()
                .nextInt(1, 3);
        long catRepoCount = categoryRepository.count();

        for (int i = 0; i < randomInt; i++) {
            long randomId = ThreadLocalRandom.current()
                    .nextLong(1, catRepoCount + 1);
            Category category = categoryRepository.findById(randomId).orElse(null);
            categories.add(category);
        }
        return categories;
    }
}

