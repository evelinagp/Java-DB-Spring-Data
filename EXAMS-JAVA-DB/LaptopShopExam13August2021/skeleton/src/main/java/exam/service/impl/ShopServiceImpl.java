package exam.service.impl;

import exam.model.dto.ShopSeedRootDto;
import exam.model.entity.Shop;
import exam.repository.ShopRepository;
import exam.service.ShopService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import exam.util.XMLParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ShopServiceImpl implements ShopService {
    public static final String SHOPS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Laptop Shop_Training\\skeleton\\src\\main\\resources\\files\\xml\\shops.xml";

    private final ShopRepository shopRepository;
    private final TownService townService;
    private final XMLParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public ShopServiceImpl(ShopRepository shopRepository, TownService townService, XMLParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.shopRepository = shopRepository;
        this.townService = townService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(Path.of(SHOPS_FILE_PATH));

    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFile(SHOPS_FILE_PATH, ShopSeedRootDto.class).getShops()
                .stream().filter(shopSeedDto -> {
                    boolean isValid = validationUtil.isValid(shopSeedDto)
                            && !isEntityExist(shopSeedDto.getName())
                            && townService.isEntityExist(shopSeedDto.getTown().getName());
                    sb.append(isValid ? String.format("Successfully imported Shop %s - %.2f",
                                    shopSeedDto.getName(), shopSeedDto.getIncome()) : "Invalid shop")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(shopSeedDto -> {
                    Shop shop = modelMapper.map(shopSeedDto, Shop.class);
                    shop.setTown(townService.findTownByName(shopSeedDto.getTown().getName()));
                    return shop;
                })
                .forEach(shopRepository::save);
        return sb.toString();
    }
    @Override
    public boolean isEntityExist(String name) {
        return shopRepository.existsByName(name);

    }

    @Override
    public Shop findShopByName(String name) {
        return shopRepository.findByName(name).orElse(null);

    }
}
