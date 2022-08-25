package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ApartmentSeedRootDto;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ApartmentServiceImpl implements ApartmentService {
    public static final String APARTMENTS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Real Estate Agency_Skeleton3\\src\\main\\resources\\files\\xml\\apartments.xml";
    private final ApartmentRepository apartmentRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;
    private final ValidationUtil validationUtil;
    private TownRepository townRepository;

    @Autowired
    public ApartmentServiceImpl(ApartmentRepository apartmentRepository, TownService townService, ModelMapper modelMapper, Gson gson, XMLParser xmlParser, ValidationUtil validationUtil, TownRepository townRepository) {
        this.apartmentRepository = apartmentRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.townRepository = townRepository;
    }

    @Override
    public boolean areImported() {
        return apartmentRepository.count() > 0;

    }

    @Override
    public String readApartmentsFromFile() throws IOException {
        return Files.readString(Path.of(APARTMENTS_FILE_PATH));

    }

    @Override
    public String importApartments() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFile(APARTMENTS_FILE_PATH, ApartmentSeedRootDto.class).getApartments()
                .stream().filter(apartmentSeedDto -> {
                    boolean isValid = validationUtil.isValid(apartmentSeedDto)
                            && !isEntityExistByArea(apartmentSeedDto.getArea()) || !isEntityExist(apartmentSeedDto.getTown())
                            && townService.isEntityExist(apartmentSeedDto.getTown());

                    sb.append(isValid ? String.format("Successfully imported apartment %s - %.2f",
                                    apartmentSeedDto.getApartmentType(), apartmentSeedDto.getArea()) : "Invalid apartment")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(apartmentSeedDto -> {
                    Apartment apartment = this.modelMapper.map(apartmentSeedDto, Apartment.class);
                    apartment.setTown(townService.findByTownName(apartmentSeedDto.getTown()));
                    return apartment;
                })
                .forEach(apartmentRepository::save);
        return sb.toString();

    }

    @Override
    public boolean isEntityExist(String town) {
        return apartmentRepository.existsByTown_TownName(town);

    }

    @Override
    public Apartment findById(Long id) {
        return apartmentRepository.findById(id).orElse(null);
    }

    @Override
    public boolean isEntityExists(Long id) {
        return apartmentRepository.existsById(id);
    }

    @Override
    public boolean isEntityExistByArea(double area) {
        return apartmentRepository.existsByArea(area);

    }
}
