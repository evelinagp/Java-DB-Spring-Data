package com.example.gamestore.services;

import com.example.gamestore.domain.entities.enums.UserType;
import com.example.gamestore.domain.models.UserCreateModel;
import com.example.gamestore.domain.models.UserModel;
import com.example.gamestore.util.exceptions.InvalidUserCredentials;
import com.example.gamestore.util.messages.AuthorizationMessages;
import com.example.gamestore.services.session.UserSession;
import com.example.gamestore.services.validators.RegisterValidator;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService{
    private UserService userService;
    private UserSession userSession;

    public AuthenticationServiceImpl(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

//    Too many if statements.
//    Can't think of a better implementation other than throwing exceptions in RegValidator.
    @Override
    public String register(String email, String password, String confirmPassword, String fullName) {
        if (this.userService.isRegisteredUser(email)) {
            return AuthorizationMessages.EMAIL_INUSE;
        }

        try {
            RegisterValidator.validateCredentials(email, fullName, password, confirmPassword);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        UserType userType = this.userService.isFirstToRegister()
                ? UserType.ADMINISTRATOR
                : UserType.BASIC;

        UserCreateModel userCreateModel = new UserCreateModel(fullName, email, password, userType);
        this.userService.registerUser(userCreateModel);

        return String.format(AuthorizationMessages.REGISTER_SUCCESS, fullName);
    }

    @Override
    public String login(String email, String password) {
        if (this.userSession.hasUserLogged()) {
            return AuthorizationMessages.USER_ALREADY_LOGGED;
        }

        UserModel user;
        try {
            user = this.userService.getUserByEmailPassword(email, password);
        }catch (InvalidUserCredentials e) {
            return e.getMessage();
        }

        this.userSession.login(user);

        return String.format(AuthorizationMessages.LOGIN_SUCCESS, user.getFullName());
    }

    @Override
    public String logout() {
        if (this.userSession.hasUserLogged()) {
            String fullName = this.userSession.getUser().getFullName();
            this.userSession.logout();

            return String.format(AuthorizationMessages.LOGOUT_SUCCESS, fullName);
        }

        return AuthorizationMessages.LOGOUT_NO_USER_LOGGED;
    }
}
