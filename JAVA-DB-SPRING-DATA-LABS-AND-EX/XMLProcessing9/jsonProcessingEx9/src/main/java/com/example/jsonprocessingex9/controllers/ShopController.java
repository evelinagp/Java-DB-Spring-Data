package com.example.jsonprocessingex9.controllers;

import com.example.jsonprocessingex9.domain.models.view.categoryModels.CategoryStatsModel;
import com.example.jsonprocessingex9.domain.models.view.categoryModels.ListCategoryStatsModel;
import com.example.jsonprocessingex9.domain.models.view.productsModels.ListSellerProductModel;
import com.example.jsonprocessingex9.domain.models.view.productsModels.SellerProductModel;
import com.example.jsonprocessingex9.domain.models.view.usersModels.ListUserWithSoldProductsModel;
import com.example.jsonprocessingex9.domain.models.view.usersModels.UserWithSoldProductsModel;
import com.example.jsonprocessingex9.domain.models.view.usersModels.statsModels.AllUserStatsModel;
import com.example.jsonprocessingex9.domain.models.view.usersModels.statsModels.UserStatsModel;
import com.example.jsonprocessingex9.services.CategoryService;
import com.example.jsonprocessingex9.services.ProductService;
import com.example.jsonprocessingex9.services.UserService;
import com.example.jsonprocessingex9.util.exporters.FileDataWriter;
import com.example.jsonprocessingex9.util.parsers.ParserImpl;
import com.example.jsonprocessingex9.util.parsers.Parser;
import com.example.jsonprocessingex9.util.seeders.CategorySeeder;
import com.example.jsonprocessingex9.util.seeders.ProductSeeder;
import com.example.jsonprocessingex9.util.seeders.Seeder;
import com.example.jsonprocessingex9.util.seeders.UserSeeder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ShopController implements CommandLineRunner {
    private UserService userService;
    private CategoryService categoryService;
    private ProductService productService;

    public ShopController(UserService userService, CategoryService categoryService, ProductService productService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @Override
    public void run(String... args) throws Exception {
        final String CATEGORIES_FILENAME = "categories";
        final String USERS_FILENAME = "users";
        final String PRODUCTS_FILENAME = "products";
//
        Parser parser = new ParserImpl();
//
        Seeder userSeeder = new UserSeeder(this.userService, parser);
        Seeder categoriesSeeder = new CategorySeeder(this.categoryService, parser);
        Seeder productsSeeder = new ProductSeeder(this.productService, this.userService, this.categoryService, parser);

        //choose to import data from either JSON or XML files
        // data is the same in both of them

//        fills the db with data from JSON resource files
//        userSeeder.SeedDbFromJson(USERS_FILENAME);
//        userSeeder.SeedDbFromJson(USERS_FILENAME);
//        categoriesSeeder.SeedDbFromJson(CATEGORIES_FILENAME);
//        productsSeeder.SeedDbFromJson(PRODUCTS_FILENAME);

//        fills the db with data from xml resource files
        userSeeder.SeedDbFromXML(USERS_FILENAME);
        categoriesSeeder.SeedDbFromXML(CATEGORIES_FILENAME);
        productsSeeder.SeedDbFromXML(PRODUCTS_FILENAME);



        //parses model data into both json and xml formats and writes it into files

        FileDataWriter fileDataWriter = new FileDataWriter(parser);
        // #1
//        List<SellerProductModel> products = this.productService
//                .getAllProductsWithoutBuyerInPriceRange(BigDecimal.valueOf(500), BigDecimal.valueOf(1000));
//
//        ListSellerProductModel listSellerProductModel = new ListSellerProductModel();
//        listSellerProductModel.setProducts(products);
//
//        String productsFileName = "products-in-range";
//        fileDataWriter.exportDataToXmlFile(productsFileName, listSellerProductModel);
//        fileDataWriter.exportDataToJsonFile(productsFileName, products);



         //#2
//        List<UserWithSoldProductsModel> sellers = this.userService.getAllUsersWithSales();
//
//        ListUserWithSoldProductsModel listUserWithSoldProductsModel = new ListUserWithSoldProductsModel();
//        listUserWithSoldProductsModel.setSellers(sellers);
//
//        String sellersFilename = "users-sold-products";
//        fileDataWriter.exportDataToJsonFile(sellersFilename, sellers);
//        fileDataWriter.exportDataToXmlFile(sellersFilename, listUserWithSoldProductsModel);

//        #3
//        List<CategoryStatsModel> categoryStats = this.categoryService.getAllCategoryStatistics();
//
//        ListCategoryStatsModel listCategoryStatsModel = new ListCategoryStatsModel();
//        listCategoryStatsModel.setCategories(categoryStats);
//
//        String statsFilename = "categories-by-products";
//        fileDataWriter.exportDataToJsonFile(statsFilename, categoryStats);
//        fileDataWriter.exportDataToXmlFile(statsFilename, listCategoryStatsModel);

//         #4

        List<UserStatsModel> sellerStats = this.userService.getAllUserSellersStats();
        AllUserStatsModel allUserStatsModel = new AllUserStatsModel();

        allUserStatsModel.setUserCount(sellerStats.size());
        allUserStatsModel.setUsers(sellerStats);

        String sellersStatsFilename = "users-and-products";
        fileDataWriter.exportDataToJsonFile(sellersStatsFilename, allUserStatsModel);
        fileDataWriter.exportDataToXmlFile(sellersStatsFilename, allUserStatsModel);
    }
}