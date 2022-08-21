package com.example.jsonprocessingex9.services.validators;

import com.example.jsonprocessingex9.domain.models.createModels.UserCreateModel;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public UserValidator() {
    }

    public boolean isValidUser(UserCreateModel userCreateModel) {
        return this.isValidLastName(userCreateModel.getLastName());
    }

    private boolean isValidLastName(String lastName) {
        return lastName != null && lastName.length() >= 3;
    }
}