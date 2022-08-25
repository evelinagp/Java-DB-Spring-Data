package hiberspring.service.impl;

import hiberspring.common.Constants;
import hiberspring.domain.dtos.ProductSeedRootDto;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Product;
import hiberspring.repository.ProductRepository;
import hiberspring.service.BranchService;
import hiberspring.service.ProductService;
import hiberspring.util.ValidationUtil;
import hiberspring.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ProductServiceImpl implements ProductService {
    public static final String PRODUCTS_FILE_PATH = Constants.PATH_TO_FILES + "products.xml";

    private final ProductRepository productRepository;
    private final BranchService branchService;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BranchService branchService, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.productRepository = productRepository;
        this.branchService = branchService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean productsAreImported() {
        return this.productRepository.count() > 0;

    }

    @Override
    public String readProductsXmlFile() throws IOException {
        return Files.readString(Path.of(PRODUCTS_FILE_PATH));

    }

    @Override
    public String importProducts() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(PRODUCTS_FILE_PATH, ProductSeedRootDto.class).getProducts()
                .stream().filter(productSeedDto -> {
                    boolean isValid = validationUtil.isValid(productSeedDto)
                            && !isEntityExist(productSeedDto.getName())
                            && branchService.isEntityExist(productSeedDto.getBranch());
                    sb.append(isValid ? String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,
                                    Product.class.getSimpleName(), productSeedDto.getName()) : Constants.INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());
                    return isValid;
                }).map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setBranch(branchService.findByBranchName(productSeedDto.getBranch()));
                    return product;
                })
                .forEach(productRepository::save);
        return sb.toString();
    }

    private boolean isEntityExist(String name) {
        return productRepository.existsByName(name);

    }
}
