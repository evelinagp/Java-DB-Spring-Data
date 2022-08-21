package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();
//        printAllBooksAfterYear(2000);
//        printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
//        printAllAuthorsAndNumberOfTheirBooks();
//        printAllBooksByAuthorNameOrderByReleaseDate("George", "Powell");
        System.out.println("Please select ex number:");
        int exNum = Integer.parseInt(bufferedReader.readLine());

        switch (exNum) {
            case 1:
                booksTitlesByAgeRestriction1();
                break;
            case 2:
                goldenBooks2();
                break;
            case 3:
                BooksByPrice3();
                break;
            case 4:
                notReleasedBooks4();
                break;
            case 5:
                booksReleasedBeforeDate5();
                break;
            case 6:
                authorsSearch6();
                break;
            case 7:
                booksSearch7();
                break;
            case 8:
                booksTitlesSearch8();
                break;
            case 9:
                countBooks9();
                break;
            case 10:
                totalBooksCopies10();
                break;
            case 11:
                reducedBook11();
                break;
//            case 12:
//                increaseBooksCopies12();
//                break;
        }
    }

    private void increaseBooksCopies12() throws IOException {
        System.out.println("Please enter date in format dd MMM yyyy");
        LocalDate localDate = LocalDate.parse(bufferedReader.readLine(),
                DateTimeFormatter.ofPattern("dd MMM yyyy"));
        System.out.println("Please enter number of book copies:");
        int bookCopies = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService.increaseCopiesByDate(localDate, bookCopies));
    }

    private void reducedBook11() throws IOException {
        System.out.println("Please enter book title:");
        String title = bufferedReader.readLine().trim();

      bookService.findBookByTitleAndPrintInfo(title).forEach(System.out::println);

    }

    private void totalBooksCopies10() {
        System.out.println(authorService.findAllAuthorsAndTheirTotalCopies());
    }

    private void countBooks9() throws IOException {
        System.out.println("Please enter title length:");
        int titleLength = Integer.parseInt(bufferedReader.readLine());

        int countOfBooksWithTitleLongerThan = bookService.findCountOfBooksWithTitleLengthLongerThan(titleLength);
        System.out.println(countOfBooksWithTitleLongerThan);
    }

    private void booksTitlesSearch8() throws IOException {
        System.out.println("Please enter author last name starts with string:");
        String startString = bufferedReader.readLine();
        bookService.findAllTitlesWithAuthorWhoseLastNameStartsWith(startString)
                .forEach(System.out::println);

    }

    private void booksSearch7() throws IOException {
        System.out.println("Please enter the containing string:");
        String contString = bufferedReader.readLine();
        bookService.findAllBookTitlesWhereTitleContainsString(contString)
                .forEach(System.out::println);
    }

    private void authorsSearch6() throws IOException {
        System.out.println("Please enter first name ends with string:");
        String endString = bufferedReader.readLine();
        authorService.findAuthorFirstNameEndsWithString(endString)
                .forEach(System.out::println);

    }

    private void booksReleasedBeforeDate5() throws IOException {
        System.out.println("Please enter date in format dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(bufferedReader.readLine(),
                DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        bookService.findAllBooksBeforeDate(localDate)
                .forEach(System.out::println);
    }

    private void notReleasedBooks4() throws IOException {
        System.out.println("Please enter year:");
        int year = Integer.parseInt(bufferedReader.readLine());

        bookService.findNotReleasedBookTitlesInYear(year)
                .forEach(System.out::println);

    }

    private void BooksByPrice3() {
        bookService.findAllBookTitlesWithPriceLessThan5OrMoreThan40()
                .forEach(System.out::println);

    }

    private void goldenBooks2() {
        bookService.findAllGoldBooksTitlesWithCopiesLessThan5000()
                .forEach(System.out::println);
    }

    private void booksTitlesByAgeRestriction1() throws IOException {
        System.out.println("Enter age restriction:");
        AgeRestriction ageRestriction = AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase());

        bookService.findAllTitlesWithAgeRestriction(ageRestriction)
                .forEach(System.out::println);
    }

    private void printAllBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService.findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService.getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService.findAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService.findAllBooksAfterYear(year).stream().map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}
