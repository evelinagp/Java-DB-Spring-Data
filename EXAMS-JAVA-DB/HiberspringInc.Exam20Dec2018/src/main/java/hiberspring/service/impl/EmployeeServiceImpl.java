package hiberspring.service.impl;

import hiberspring.common.Constants;
import hiberspring.domain.dtos.EmployeeSeedRootDto;
import hiberspring.domain.dtos.ProductSeedRootDto;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.domain.entities.Product;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.BranchService;
import hiberspring.service.EmployeeCardService;
import hiberspring.service.EmployeeService;
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
public class EmployeeServiceImpl implements EmployeeService {
    public static final String EMPLOYEES_FILE_PATH = Constants.PATH_TO_FILES + "employees.xml";

    private final EmployeeRepository employeeRepository;
    private final BranchService branchService;
    private final EmployeeCardService employeeCardService;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BranchService branchService, EmployeeCardService employeeCardService, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.employeeRepository = employeeRepository;
        this.branchService = branchService;
        this.employeeCardService = employeeCardService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count() > 0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return Files.readString(Path.of(EMPLOYEES_FILE_PATH));

    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();

        xmlParser.fromFile(EMPLOYEES_FILE_PATH, EmployeeSeedRootDto.class)
                .getEmployees()
                .stream().filter(employeeSeedDto -> {
                    boolean isValid = validationUtil.isValid(employeeSeedDto)
                            && !isEntityExist(employeeSeedDto.getCard())
                            && branchService.isEntityExist(employeeSeedDto.getBranch())
                            && employeeCardService.isEntityExist(employeeSeedDto.getCard());
                    sb.append(isValid ? String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,
                                    Employee.class.getSimpleName(), employeeSeedDto.getFirstName() +" "+ employeeSeedDto.getLastName()) : Constants.INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());
                    return isValid;
                }).map(employeeSeedDto -> {
                    Employee employee = modelMapper.map(employeeSeedDto, Employee.class);
                    employee.setBranch(branchService.findByBranchName(employeeSeedDto.getBranch()));
                    employee.setCard(employeeCardService.findByCardNumber(employeeSeedDto.getCard()));
                    return employee;
                })
                .forEach(employeeRepository::save);
        return sb.toString();
    }

    private boolean isEntityExist(String card) {
        return employeeRepository.existsByCard_Number(card);

    }

    @Override
    public String exportProductiveEmployees() {
        StringBuilder sb = new StringBuilder();
        this.employeeRepository.findByBranchWithAtLeastOneProductOrderByFirstNameAscPositionLengthDesc()
                .forEach(e ->
                        sb.append(String.format("Name: %s\n" +
                                "Position: %s\n" +
                                "Card Number: %s\n", e.toString(), e.getPosition(), e.getCard().getNumber()))
                                .append("-------------------------\n"));

        return sb.toString();
    }
}
