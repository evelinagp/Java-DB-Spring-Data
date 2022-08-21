package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> findAllGoldBooksTitlesWithCopiesLessThan5000();

    List<String> findAllBookTitlesWithPriceLessThan5OrMoreThan40();

    List<String> findNotReleasedBookTitlesInYear(int year);


    List<String> findAllBooksBeforeDate(LocalDate localDate);

    List<String> findAllBookTitlesWhereTitleContainsString(String contString);

    List<String> findAllTitlesWithAuthorWhoseLastNameStartsWith(String startString);

    int findCountOfBooksWithTitleLengthLongerThan(int titleLength);

    int increaseCopiesByDate(LocalDate localDate, int bookCopies);

    List<String> findBookByTitleAndPrintInfo(String title);
}
