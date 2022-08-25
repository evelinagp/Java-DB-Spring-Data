package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.config.XmlParser;
import softuni.exam.domain.dto.PictureSeedRootDto;
import softuni.exam.domain.entities.Picture;
import softuni.exam.repository.PictureRepository;
import softuni.exam.util.ValidatorUtil;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PictureServiceImpl implements PictureService {
    public static final String PICTURES_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Football-Info_Training\\src\\main\\resources\\files\\xml\\pictures.xml";

    private final PictureRepository pictureRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidatorUtil validationUtil;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidatorUtil validationUtil) {
        this.pictureRepository = pictureRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public String importPictures() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
    //   PictureSeedRootDto pictureSeedRootDto = xmlParser.fromFile(PICTURES_FILE_PATH, PictureSeedRootDto.class);/*.getPictures().stream()*/
xmlParser.fromFile(PICTURES_FILE_PATH, PictureSeedRootDto.class).getPictures().stream()
                .filter(pictureSeedDto -> {
                    boolean isValid = validationUtil.isValid(pictureSeedDto)
                            && !isEntityExist(pictureSeedDto.getUrl());
                    sb.append(isValid
                                    ? String.format("Successfully imported picture - %s",
                                    pictureSeedDto.getUrl()) : "Invalid picture")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(pictureSeedDto -> modelMapper.map(pictureSeedDto, Picture.class))
                .forEach(pictureRepository::save);
        return sb.toString();
    }
    @Override
    public boolean isEntityExist(String url) {
        return this.pictureRepository.existsByUrl(url);

    }


    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));

    }

    @Override
    public Picture findPicByUrl(String url) {
        return this.pictureRepository.findByUrl(url).orElse(null);

    }
}
