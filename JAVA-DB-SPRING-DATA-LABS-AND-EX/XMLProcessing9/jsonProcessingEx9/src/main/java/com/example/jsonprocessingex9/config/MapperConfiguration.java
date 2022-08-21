package com.example.jsonprocessingex9.config;

import com.example.jsonprocessingex9.domain.entities.Category;
import com.example.jsonprocessingex9.domain.models.view.categoryModels.CategoryStatsModel;
import com.example.jsonprocessingex9.domain.models.view.productsModels.BoughtProductModel;
import com.example.jsonprocessingex9.domain.models.view.productsModels.SimpleProductModel;
import com.example.jsonprocessingex9.domain.models.view.usersModels.UserWithSoldProductsModel;
import com.example.jsonprocessingex9.domain.models.view.usersModels.statsModels.SoldProducts;
import com.example.jsonprocessingex9.domain.models.view.usersModels.statsModels.UserStatsModel;
import com.example.jsonprocessingex9.domain.entities.Product;
import com.example.jsonprocessingex9.domain.entities.User;
import com.example.jsonprocessingex9.domain.models.view.productsModels.SellerProductModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class MapperConfiguration {
    private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();

        //users

        Converter<Product, String> firstNameLastNameCombine = new Converter<>() {
            @Override
            public String convert(MappingContext<Product, String> context) {
                return context.getSource().getSeller().getFirstName() + " " + context.getSource().getSeller().getLastName();
            }
        };

        //without converter modelmapper throws java.lang.IllegalArgumentException: object is not an instance of declaring class
        modelMapper.createTypeMap(User.class, UserWithSoldProductsModel.class)
                .addMapping(User::getSellingProducts, UserWithSoldProductsModel::setSoldProducts);

        Converter<User, UserStatsModel> userStatsModelConverter = new Converter<User, UserStatsModel>() {
            @Override
            public UserStatsModel convert(MappingContext<User, UserStatsModel> context) {

                List<SimpleProductModel> products = context
                        .getSource()
                        .getSellingProducts()
                        .stream()
                        .map(product -> modelMapper.map(product, SimpleProductModel.class))
                        .sorted((p1, p2) -> p2.getPrice().subtract(p1.getPrice()).intValue())
                        .collect(Collectors.toList());

                SoldProducts soldProducts = new SoldProducts();
                soldProducts.setCount(products.size());
                soldProducts.setProducts(products);

                UserStatsModel destination = new UserStatsModel();
                destination.setSoldProducts(soldProducts);
                destination.setAge(context.getSource().getAge());
                destination.setFirstName(context.getSource().getFirstName());
                destination.setLastName(context.getSource().getLastName());

                return destination;
            }
        };

        modelMapper.createTypeMap(User.class, UserStatsModel.class)
                .setConverter(userStatsModelConverter);

        //products

        modelMapper.createTypeMap(Product.class, BoughtProductModel.class)
                .addMapping(src -> src.getBuyer().getFirstName(), BoughtProductModel::setBuyerFirstName)
                .addMapping(src -> src.getBuyer().getLastName(), BoughtProductModel::setBuyerLastName);

        modelMapper.createTypeMap(Product.class, SellerProductModel.class)
                .addMappings(new PropertyMap<Product, SellerProductModel>() {
                    @Override
                    protected void configure() {
                        using(firstNameLastNameCombine).map(source).setSeller("");
                    }
                });

        // categories

        Converter<Category, CategoryStatsModel> categoryStatsModelConverter = new Converter<Category, CategoryStatsModel>() {
            @Override
            public CategoryStatsModel convert(MappingContext<Category, CategoryStatsModel> context) {
                BigDecimal totalRevenue = context.getSource().getProducts()
                        .stream()
                        .map(Product::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                int productsCount = context.getSource().getProducts().size();
                double averagePrice = totalRevenue.doubleValue() / productsCount;

                CategoryStatsModel destination = new CategoryStatsModel();
                destination.setAveragePrice(averagePrice);
                destination.setTotalRevenue(totalRevenue);
                destination.setCategory(context.getSource().getName());
                destination.setProductsCount(productsCount);

                return destination;
            }
        };

        modelMapper.createTypeMap(Category.class, CategoryStatsModel.class)
                .setConverter(categoryStatsModelConverter);

        modelMapper.validate();
    }

    @Bean
    public ModelMapper modelMapper() {
        return modelMapper;
    }
}
