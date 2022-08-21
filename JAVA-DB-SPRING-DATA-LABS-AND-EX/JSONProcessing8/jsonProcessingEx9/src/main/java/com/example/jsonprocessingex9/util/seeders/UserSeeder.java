package com.example.jsonprocessingex9.util.seeders;

import com.example.jsonprocessingex9.domain.models.createModels.ListUserCreateModel;
import com.example.jsonprocessingex9.domain.models.createModels.UserCreateModel;
import com.example.jsonprocessingex9.services.UserService;
import com.example.jsonprocessingex9.util.parsers.Parser;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserSeeder extends SeederImpl {
    private UserService userService;
    private Parser parser;

    public UserSeeder(UserService userService, Parser parser) {
        this.userService = userService;
        this.parser = parser;
    }

    @Override
    public void SeedDbFromJson(String resourceFilename) throws IOException {

        if (this.userService.getAllUsers().size() == 0){

        String usersJson = super.getFileContent(resourceFilename + ".json");

        List<UserCreateModel> users = Arrays.asList(this.parser.fromJSon(usersJson, UserCreateModel[].class));


        this.userService.saveAllToDb(users);
    }
}
    @Override
    public void SeedDbFromXML(String resourceFilename) throws IOException, JAXBException {
        String xml = super.getFileContent(resourceFilename + ".xml");

        ListUserCreateModel listUserCreateModel = this.parser.fromXML(xml, ListUserCreateModel.class);

        this.userService.saveAllToDb(listUserCreateModel.getUsers());
    }
}
