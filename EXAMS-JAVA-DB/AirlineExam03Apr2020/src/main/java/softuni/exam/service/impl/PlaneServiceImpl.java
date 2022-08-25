package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PlaneSeedRootDto;
import softuni.exam.models.entity.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlaneServiceImpl implements PlaneService {
    public static final String PLANES_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Airline_Skeleton_Training\\src\\main\\resources\\files\\xml\\planes.xml";

    private final PlaneRepository planeRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PlaneServiceImpl(PlaneRepository planeRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.planeRepository = planeRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;

    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return Files.readString(Path.of(PLANES_FILE_PATH));

    }

    @Override
    public String importPlanes() throws JAXBException, FileNotFoundException {

        //PlaneSeedRootDto planeSeedRootDto = xmlParser.fromFile(PLANES_FILE_PATH, PlaneSeedRootDto.class);
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFile(PLANES_FILE_PATH, PlaneSeedRootDto.class).getPlanes().stream()
                .filter(planeSeedDto -> {
                    boolean isValid = validationUtil.isValid(planeSeedDto)
                            && !isEntityExist(planeSeedDto.getRegisterNumber());
                    sb.append(isValid
                                    ? String.format("Successfully imported Plane %s",
                                    planeSeedDto.getRegisterNumber()) : "Invalid Plane")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(planeSeedDto -> modelMapper.map(planeSeedDto, Plane.class))
                .forEach(planeRepository::save);
        return sb.toString();
    }
    @Override
    public boolean isEntityExist(String registerNumber) {
        return this.planeRepository.existsByRegisterNumber(registerNumber);

    }

    @Override
    public Plane findPlaneByRegisterNumber(String registerNumber) {
        return this.planeRepository.findByRegisterNumber(registerNumber);

    }
}
