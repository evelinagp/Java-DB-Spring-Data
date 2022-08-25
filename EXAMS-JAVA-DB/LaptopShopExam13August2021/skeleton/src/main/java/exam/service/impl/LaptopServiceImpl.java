package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.CustomerSeedDto;
import exam.model.dto.LaptopSeedDto;
import exam.model.entity.Customer;
import exam.model.entity.Laptop;
import exam.repository.LaptopRepository;
import exam.service.LaptopService;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class LaptopServiceImpl implements LaptopService {
    public static final String LAPTOPS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Laptop Shop_Training\\skeleton\\src\\main\\resources\\files\\json\\laptops.json";

    private final LaptopRepository laptopRepository;
    private final ShopService shopService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopService shopService, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.laptopRepository = laptopRepository;
        this.shopService = shopService;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files.readString(Path.of(LAPTOPS_FILE_PATH));

    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readLaptopsFileContent(), LaptopSeedDto[].class))
                .filter(laptopSeedDto -> {
                    boolean isValid = validationUtil.isValid(laptopSeedDto)
                            && !isEntityExist(laptopSeedDto.getMacAddress())
                            && shopService.isEntityExist(laptopSeedDto.getShop().getName());
                    sb.append(isValid ? String.format("Successfully imported Laptop %s - %.2f - %d - %d",
                                    laptopSeedDto.getMacAddress(), laptopSeedDto.getCpuSpeed(), laptopSeedDto.getRam(), laptopSeedDto.getStorage()) : "Invalid Laptop")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(laptopSeedDto -> {
                    Laptop laptop = modelMapper.map(laptopSeedDto, Laptop.class);
                    laptop.setShop(shopService.findShopByName(laptopSeedDto.getShop().getName()));
                    return laptop;
                })
                .forEach(laptopRepository::save);
        return sb.toString();
    }

    private boolean isEntityExist(String macAddress) {
        return laptopRepository.existsByMacAddress(macAddress);

    }

    @Override
    public String exportBestLaptops() {
        StringBuilder sb = new StringBuilder();

        this.laptopRepository.findBestLaptops().forEach(l -> {
            sb.append(String.format("Laptop - %s\n" +
                                    "*Cpu speed - %.2f\n" +
                                    "**Ram - %d\n" +
                                    "***Storage - %d\n" +
                                    "****Price - %.2f\n" +
                                    "#Shop name - %s\n" +
                                    "##Town - %s\n", l.getMacAddress(), l.getCpuSpeed(), l.getRam(), l.getStorage(), l.getPrice(),
                            l.getShop().getName(), l.getShop().getTown().getName()))
                    .append(System.lineSeparator());
        });

        return sb.toString();
    }
}
