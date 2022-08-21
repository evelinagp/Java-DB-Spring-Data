package com.example.gamestore.services.validators;

import com.example.gamestore.util.messages.GameMessages;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameValidator {

    public GameValidator() {
    }

    //    Title – has to begin with an uppercase letter and must have length between 3 and 100 symbols (inclusively).
    public boolean isValidTitle(String title) {
        //Can not think of a better pattern. Doesn't cover all cases
        Pattern titlePattern = Pattern.compile("^[A-Z]{1,1}[a-z'*]{2,99}(\\s*[A-Z]{1,1}[a-z'*]{2,99}$)*");
        Matcher match = titlePattern.matcher(title);

        return match.find();
    }

    //    Price – must be a positive number with precision up to 2 digits after the floating point.
    public boolean isValidPrice(String price) {
        try {
            BigDecimal parsedPrice = new BigDecimal(price);
            boolean hasAccuratePrecision = parsedPrice.scale() == 2;

            return parsedPrice.doubleValue() > 0 && hasAccuratePrecision;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //    Size – must be a positive number with precision up to 1 digit after the floating point.
    public boolean isValidSize(String size) {
        try {
            BigDecimal parsedSize = new BigDecimal(size);
            boolean hasAccuratePrecision = parsedSize.scale() == 1;

            return parsedSize.doubleValue() > 0 && hasAccuratePrecision;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //    Trailer – only videos from YouTube are allowed.
//    Only their ID, which is a string of exactly 11 characters, should be saved to the database.
    public boolean isValidTrailer(String trailerURL) {
        String youtubeURLRegex = "^((https://www\\.)|(https://)|(www\\.)|(http://)|(http://www\\.)|())" +
                "youtube\\.com/watch\\?v=.{11}$";

        Pattern urlPattern = Pattern.compile(youtubeURLRegex);
        Matcher matcher = urlPattern.matcher(trailerURL);

        return matcher.find();
    }
    //    Thumbnail URL – it should be a plain text starting with http://, https:// or null
    public boolean isValidThumbnail(String thumbnailURl) {
        String thumbnailRegex = "^((https://)|(http://)|()).+$";

        Pattern thumbnailPattern = Pattern.compile(thumbnailRegex);
        Matcher matcher = thumbnailPattern.matcher(thumbnailURl);

        return matcher.find();
    }

    public boolean isValidDescription(String description) {
        return description.length() >= 20;
    }

    //    Matches only dates in the format dd-mm-yyyy
    public boolean isValidReleaseDate(String releaseDate) {
        String dateRegex = "^[0-9]{2}-[0-9]{2}-[0-9]{4}$";
        Pattern datePattern = Pattern.compile(dateRegex);

        return datePattern.matcher(releaseDate).find();
    }

    public boolean validateGame(String[] args) {
        if (!isValidTitle(args[0])) {
            throw new IllegalArgumentException(GameMessages.INVALID_TITLE);
        }

        if (!isValidPrice(args[1])) {
            throw new IllegalArgumentException(GameMessages.INVALID_PRICE);
        }

        if (!isValidSize(args[2])) {
            throw new IllegalArgumentException(GameMessages.INVALID_SIZE);
        }

        if (!isValidTrailer(args[3])) {
            throw new IllegalArgumentException(GameMessages.INVALID_TRAILER);
        }

        if (!isValidThumbnail(args[4])) {
            throw new IllegalArgumentException(GameMessages.INVALID_THUMBNAIL);
        }

        if (!isValidDescription(args[5])) {
            throw new IllegalArgumentException(GameMessages.INVALID_DESCRIPTION);
        }

        if (!isValidReleaseDate(args[6])) {
            throw new IllegalArgumentException(GameMessages.INVALID_DATE);
        }

        return true;
    }
}
