package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.UserSeedDto;
import softuni.exam.instagraphlite.models.entity.User;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;

@Service
public class UserServiceImpl implements UserService {
    private static final String USERS_FILE_PATH = "src/main/resources/files/users.json";

    private final UserRepository userRepository;
    private final PictureService pictureService;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final PictureRepository pictureRepository;

    public UserServiceImpl(UserRepository userRepository, PictureService pictureService, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper, PictureRepository pictureRepository) {
        this.userRepository = userRepository;
        this.pictureService = pictureService;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.pictureRepository = pictureRepository;
    }


    @Override
    public boolean areImported() {
        return this.userRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(USERS_FILE_PATH));

    }

    @Override
    public String importUsers() throws IOException {
        //FOR DEBUG->UserSeedDto[] userSeedDtos = gson.fromJson(readFromFileContent(), UserSeedDto[].class);

        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readFromFileContent(), UserSeedDto[].class))
                .filter(userSeedDto -> {
                    boolean isValid = validationUtil.isValid(userSeedDto)
                            && pictureService.isEntityExist(userSeedDto.getProfilePicture())
                            && !isEntityExist(userSeedDto.getUsername());
                    sb.append(isValid ?
                                    String.format("Successfully imported User: %s",
                                            userSeedDto.getUsername()) : "Invalid User")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(userSeedDto -> {
                    User user = modelMapper.map(userSeedDto, User.class);
                    user.setProfilePicture(pictureService.findByPicturePath(userSeedDto.getProfilePicture()));
                    return user;
                })
                .forEach(userRepository::save);
        return sb.toString();
    }

    @Override
    public boolean isEntityExist(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User findByName(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public String exportUsersWithTheirPosts() {
        StringBuilder sb = new StringBuilder();
        this.userRepository.findByUsernameAndPostsCountDescUserIdAcsPictureSizeAcs()
                .forEach(user -> {
                    sb.append
                            (String.format("User: %s%n" +
                                    "Post count: %d%n", user.getUsername(), user.getPosts().size()));
                    user.getPosts()
                            .stream()
                            .sorted(Comparator.comparingDouble(p -> p.getPicture().getSize()))
                            .forEach(post -> {
                                sb.append
                                        (String.format("==Post Details:%n" +
                                                        "----Caption: %s%n" +
                                                        "----Picture Size: %.2f%n"
                                                , post.getCaption(), post.getPicture().getSize()));

                            });
                    sb.append(System.lineSeparator());
                });
        return sb.toString();
    }
}
