package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PictureSeedDto;
import softuni.exam.instagraphlite.models.entity.Picture;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String PICTURES_FILE_PATH = "src/main/resources/files/pictures.json";
    private final PictureRepository pictureRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public PictureServiceImpl(PictureRepository pictureRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(PICTURES_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        //FOR DEBUG->//  PictureSeedDto[] pictureSeedDtos = gson.fromJson(readFromFileContent(), PictureSeedDto[].class);
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readFromFileContent(), PictureSeedDto[].class))
                .filter(pictureSeedDto -> {
                    boolean isValid = validationUtil.isValid(pictureSeedDto)
                            && !isEntityExist(pictureSeedDto.getPath());
                    sb.append(isValid ?
                                    String.format("Successfully imported Picture, with size %.2f",
                                            pictureSeedDto.getSize()) : "Invalid picture")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(pictureSeedDto -> modelMapper.map(pictureSeedDto, Picture.class))
                .forEach(pictureRepository::save);
        return sb.toString();
    }

    @Override
    public String exportPictures() {
        StringBuilder sb = new StringBuilder();
        this.pictureRepository.findBySizeGreaterThanOrderBySizeAsc(30000D)
                .forEach(p -> {
                    sb.append(String.format("%.2f - %s", p.getSize(), p.getPath()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }


    @Override
    public boolean isEntityExist(String path) {
        return this.pictureRepository.existsByPath(path);
    }

    @Override
    public Picture findByPicturePath(String path) {
        return this.pictureRepository.findByPath(path).orElse(null);
    }
}
