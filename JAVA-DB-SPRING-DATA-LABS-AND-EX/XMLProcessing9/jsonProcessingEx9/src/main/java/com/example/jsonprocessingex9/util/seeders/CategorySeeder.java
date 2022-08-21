package com.example.jsonprocessingex9.util.seeders;


import com.example.jsonprocessingex9.domain.models.createModels.CategoryCreateModel;
import com.example.jsonprocessingex9.domain.models.createModels.ListCategoryCreateModel;
import com.example.jsonprocessingex9.services.CategoryService;
import com.example.jsonprocessingex9.util.parsers.Parser;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CategorySeeder extends SeederImpl {
    private CategoryService categoryService;
    private Parser parser;

    public CategorySeeder(CategoryService categoryService, Parser parser) {
        this.categoryService = categoryService;
        this.parser = parser;
    }

    @Override
    public void SeedDbFromJson(String resourceFilename) throws IOException {
        String categoriesJson = super.getFileContent(resourceFilename + ".json");
        List<CategoryCreateModel> categories = Arrays.asList(this.parser.fromJSon(categoriesJson, CategoryCreateModel[].class));

        if (categories.size() > 0){
            return;
        }

        this.categoryService.saveAllToDb(categories);
    }

    @Override
    public void SeedDbFromXML(String resourceFilename) throws IOException, JAXBException {
        String categoryXML = super.getFileContent(resourceFilename + ".xml");
        ListCategoryCreateModel categoryList = this.parser.fromXML(categoryXML, ListCategoryCreateModel.class);

        this.categoryService.saveAllToDb(categoryList.getCategories());
    }
}
