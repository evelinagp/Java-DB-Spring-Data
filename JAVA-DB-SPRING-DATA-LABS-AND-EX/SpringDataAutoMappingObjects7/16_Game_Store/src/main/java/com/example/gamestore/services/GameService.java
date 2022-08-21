package com.example.gamestore.services;


import com.example.gamestore.domain.entities.Game;
import com.example.gamestore.domain.models.GameCartModel;
import com.example.gamestore.domain.models.GameCreateModel;
import com.example.gamestore.domain.models.GameEditModel;
import com.example.gamestore.repositories.GameRepository;
import com.example.gamestore.util.messages.CartMessages;
import com.example.gamestore.util.messages.GameMessages;
import com.example.gamestore.services.session.UserSession;
import com.example.gamestore.services.validators.GameValidator;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class GameService {
    private GameRepository gameRepository;
    private UserService userService;
    private ModelMapper modelMapper;
    private UserSession userSession;
    private GameValidator gameValidator;

    public GameService(GameRepository gameRepository, UserService userService, ModelMapper modelMapper, UserSession userSession, GameValidator gameValidator) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userSession = userSession;
        this.gameValidator = gameValidator;
    }

    //7 params way too much. Is it better to accept String[] as sole argument then?
    public String addGame(String[] args) throws ParseException {
        if (!this.userSession.hasUserLogged()) {
            return GameMessages.NO_ADMIN_LOGGED;
        }

        if (!this.userSession.isAdmin()) {
            return GameMessages.UNAUTHORIZED;
        }

        String gameTitle = args[0];
        Game gameExists = this.gameRepository.findGameByTitle(gameTitle);

        if (gameExists != null) {
            return String.format(GameMessages.TITLE_EXISTS, gameTitle);
        }

        try {
            gameValidator.validateGame(args);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }

        double price = Double.parseDouble(args[1]);
        double size = Double.parseDouble(args[2]);
        String trailerID = extractYoutubeVideoID(args[3]);
        String thumbnailURL = (args[4]);
        String description = (args[5]);

        String datePattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattern);
        Date releaseDate = simpleDateFormat.parse(args[6]);

        GameCreateModel gameCreateModel = new GameCreateModel(gameTitle, price, size, trailerID, thumbnailURL, description, releaseDate);
        Game game = this.modelMapper.map(gameCreateModel, Game.class);

        this.gameRepository.save(game);

        return String.format(GameMessages.SUCC_ADDED_GAME, gameCreateModel.getTitle());
    }

    private String extractYoutubeVideoID(String videoURL) {
        return videoURL.substring(videoURL.length() - 11);
    }

    public String editGame(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int gameId = Integer.parseInt(args[0]);
        Game game = this.gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            return "Game not found.";
        }

        GameEditModel gameEditModel = this.modelMapper.map(game, GameEditModel.class);

        //with reflection extracts validator and setter methods for all fields to be edited
        //if one fieldName or value is invalid the process is stopped and the game is not persisted in the db
        for (String argument : Arrays.copyOfRange(args, 1, args.length)) {
            String[] split = argument.split("=");
            String fieldName = split[0].toLowerCase();
            String value = split[1];

            Method fieldSetter = this.getFieldSetter(fieldName);

            if (fieldSetter == null) {
                return String.format("%s is invalid Game field.", fieldName);
            }

            Method validatorMethod = this.getValidatorMethod(fieldName);

            if (!(boolean) validatorMethod.invoke(this.gameValidator, value)) {
                return String.format("%s invalid value for %s", value, fieldName);
            }

            fieldSetter.invoke(gameEditModel, value);
        }

        Game editedGame = this.modelMapper.map(gameEditModel, Game.class);
        this.gameRepository.save(editedGame);

        return String.format("Edited %s", game.getTitle());
    }

    //finds the appropriate method for the fieldName
    //if field is invalid and does not exist in the Game class validatorMethod will be null
    private Method getValidatorMethod(String fieldName) throws NoSuchMethodException {
        //edits the methodNames of
        Method validatorMethod = Arrays.stream(this.gameValidator.getClass().getMethods())
                .filter(method -> (method.getName().replace("isValid", "").toLowerCase()).equals(fieldName))
                .findFirst()
                .orElse(null);

        if (validatorMethod == null) {
            throw new NoSuchMethodException(String.format("No validator method found for fieldName %s", fieldName));
        }

        return validatorMethod;
    }

    private Method getFieldSetter(String fieldName) {
        String SET_PREFIX = "set";
        String fieldMethodName = SET_PREFIX + fieldName;

        //extracts the appropriate setter for fieldName
        //if fieldName is invalid setter is null
        Method setter = Arrays.stream(GameEditModel.class.getDeclaredMethods())
                .filter(method -> (method.getName().toLowerCase()).equals(fieldMethodName))
                .findFirst()
                .orElse(null);

        return setter;
    }

    public String deleteGame(String id) {
        if (!this.userSession.hasUserLogged()) {
            return GameMessages.NO_ADMIN_LOGGED;
        }

        if (!this.userSession.isAdmin()) {
            return GameMessages.UNAUTHORIZED;
        }

        int gameId;
        try {
            gameId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return GameMessages.DELETE_INVALID_ID;
        }

        try {
            this.gameRepository.deleteById(gameId);
        } catch (EmptyResultDataAccessException e) {
            return GameMessages.DELETE_ID_NOT_EXIST;
        }

        return GameMessages.DELETE_SUCCESS;
    }

    public String getTitlesOfAllGames() {
        if (!this.userSession.hasUserLogged()) {
            return GameMessages.NO_USER_LOGGED;
        }

        return this.gameRepository
                .findAll()
                .stream()
                .map(game -> String.format("%s %.2f", game.getTitle(), game.getPrice()))
                .collect(Collectors.joining("\r\n"));
    }

    public String getGameDetails(String title) {
        if (!this.userSession.hasUserLogged()) {
            return GameMessages.NO_USER_LOGGED;
        }

        Game game = this.gameRepository.findGameByTitle(title);

        if (game == null) {
            return String.format("Game with title: %s does not exist.", title);
        }
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String printDate = formatter.format(game.getReleaseDate());

        return String.format("Title: %s\r\nPrice: %.2f\r\nDescription: %s\r\nRelease date: %s",
                game.getTitle(),
                game.getPrice(),
                game.getDescription(),
                printDate);
    }

    public GameCartModel getGameCartModelByTitle(String title) {
        Game game = this.gameRepository.findGameByTitle(title);

        if (game == null) {
            throw new IllegalArgumentException(String.format(CartMessages.GAME_WITH_TITLE_NOT_FOUND, title));
        }

        return this.modelMapper.map(game, GameCartModel.class);
    }

    public String getOwnedGamesTitles() {
        return this.userService.getOwnedGamesTitles();
    }
}
