package hiberspring.service;

import hiberspring.domain.entities.EmployeeCard;

import java.io.IOException;

public interface EmployeeCardService {

    Boolean employeeCardsAreImported();

    String readEmployeeCardsJsonFile() throws IOException;

    String importEmployeeCards(String employeeCardsFileContent) throws IOException;

    boolean isEntityExist(String number);

    EmployeeCard findByCardNumber(String cardNumber);
}
