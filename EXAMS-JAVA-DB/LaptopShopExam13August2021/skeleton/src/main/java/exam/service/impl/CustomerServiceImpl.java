package exam.service.impl;

import com.google.gson.Gson;
import exam.model.dto.CustomerSeedDto;
import exam.model.entity.Customer;
import exam.repository.CustomerRepository;
import exam.service.CustomerService;
import exam.service.TownService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CustomerServiceImpl implements CustomerService {
    public static final String CUSTOMERS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Laptop Shop_Training\\skeleton\\src\\main\\resources\\files\\json\\customers.json";

    private final CustomerRepository customerRepository;
    private final TownService townService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public CustomerServiceImpl(CustomerRepository customerRepository, TownService townService, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.customerRepository = customerRepository;
        this.townService = townService;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count() > 0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(Path.of(CUSTOMERS_FILE_PATH));

    }

    @Override
    public String importCustomers() throws IOException {
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readCustomersFileContent(), CustomerSeedDto[].class))
                .filter(customerSeedDto -> {
                    boolean isValid = validationUtil.isValid(customerSeedDto)
                            && !isEntityExist(customerSeedDto.getEmail())
                            && townService.isEntityExist(customerSeedDto.getTown().getName());
                    sb.append(isValid ? String.format("Successfully imported Customer %s %s - %s",
                                    customerSeedDto.getFirstName(), customerSeedDto.getLastName(), customerSeedDto.getEmail()) : "Invalid Customer")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(customerSeedDto -> {
                    Customer customer = modelMapper.map(customerSeedDto, Customer.class);
                    customer.setTown(townService.findTownByName(customerSeedDto.getTown().getName()));
                    return customer;
                })
                .forEach(customerRepository::save);
        return sb.toString();
    }
    @Override
    public boolean isEntityExist(String email) {
        return customerRepository.existsByEmail(email);

    }
}

