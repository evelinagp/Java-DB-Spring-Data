package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PostSeedRootDto;
import softuni.exam.instagraphlite.models.entity.Post;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PostServiceImpl implements PostService {
    private static final String POSTS_FILE_PATH = "src/main/resources/files/posts.xml";

    private final PostRepository postRepository;
    private final PictureService pictureService;
    private final UserService userService;
    private final XMLParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, PictureService pictureService, UserService userService, XMLParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.pictureService = pictureService;
        this.userService = userService;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean areImported() {
        return this.postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files.readString(Path.of(POSTS_FILE_PATH));

    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        //FOR DEBUG ->        PostSeedRootDto postSeedRootDto = xmlParser.fromFile(POSTS_FILE_PATH, PostSeedRootDto.class);
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFile(POSTS_FILE_PATH, PostSeedRootDto.class).getPost().stream()
                .filter(postSeedDto -> {
                    boolean isValid = validationUtil.isValid(postSeedDto)
                            && pictureService.isEntityExist(postSeedDto.getPicture().getPath())
                            && userService.isEntityExist(postSeedDto.getUser().getUsername());
                    sb.append(isValid
                                    ? String.format("Successfully imported Post, made by %s",
                                    postSeedDto.getUser().getUsername()) : "Invalid Post")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(postSeedDto -> {
                    Post post = modelMapper.map(postSeedDto, Post.class);
                    post.setPicture(pictureService.findByPicturePath(postSeedDto.getPicture().getPath()));
                    post.setUser(userService.findByName(postSeedDto.getUser().getUsername()));
                    return post;
                }).forEach(postRepository::save);
        return sb.toString();
    }
}
