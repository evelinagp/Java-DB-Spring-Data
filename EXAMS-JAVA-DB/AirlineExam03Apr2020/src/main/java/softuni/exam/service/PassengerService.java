package softuni.exam.service;

import softuni.exam.models.dto.PassengerEmailDto;
import softuni.exam.models.entity.Passenger;

import java.io.IOException;

//ToDo - Before start App implement this Service and set areImported to return false
public interface PassengerService {

    boolean areImported();

    String readPassengersFileContent() throws IOException;

    String importPassengers() throws IOException;

    boolean isEntityExists(String email);

    String getPassengersOrderByTicketsCountDescendingThenByEmail();

    Passenger findPassengerByEmail(String email);
}