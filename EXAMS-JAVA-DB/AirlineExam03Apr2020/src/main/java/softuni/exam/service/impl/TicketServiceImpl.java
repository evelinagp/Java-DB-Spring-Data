package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PlaneSeedRootDto;
import softuni.exam.models.dto.TicketSeedRootDto;
import softuni.exam.models.entity.Plane;
import softuni.exam.models.entity.Ticket;
import softuni.exam.repository.TicketRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.service.PlaneService;
import softuni.exam.service.TicketService;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {
    public static final String TICKETS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Airline_Skeleton_Training\\src\\main\\resources\\files\\xml\\tickets.xml";

    private final TicketRepository ticketRepository;
    private final PlaneService planeService;
    private final PassengerService passengerService;
    private final TownService townService;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TicketServiceImpl(TicketRepository ticketRepository, PlaneService planeService, PassengerService passengerService, TownService townService, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.ticketRepository = ticketRepository;
        this.planeService = planeService;
        this.passengerService = passengerService;
        this.townService = townService;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;

    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return Files.readString(Path.of(TICKETS_FILE_PATH));

    }

    @Override
    public String importTickets() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
     TicketSeedRootDto ticketSeedRootDto = xmlParser.fromFile(TICKETS_FILE_PATH, TicketSeedRootDto.class);
        xmlParser.fromFile(TICKETS_FILE_PATH, TicketSeedRootDto.class).getTickets().stream()
                .filter(ticketSeedDto -> {
                    boolean isValid = validationUtil.isValid(ticketSeedDto)
                            && !isEntityExist(ticketSeedDto.getSerialNumber())
                            && townService.isEntityExist(ticketSeedDto.getFromTown().getName())
                            && townService.isEntityExist(ticketSeedDto.getToTown().getName())
                            && passengerService.isEntityExists(ticketSeedDto.getPassenger().getEmail())
                            && planeService.isEntityExist(ticketSeedDto.getPlane().getRegisterNumber());

                    sb.append(isValid
                                    ? String.format("Successfully imported Ticket %s - %s",
                                    ticketSeedDto.getFromTown().getName(), ticketSeedDto.getToTown().getName()) : "Invalid Ticket")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(ticketSeedDto ->{
                    Ticket ticket = modelMapper.map(ticketSeedDto, Ticket.class);
                    ticket.setFromTown(townService.findByTownName(ticketSeedDto.getFromTown().getName()));
                    ticket.setToTown(townService.findByTownName(ticketSeedDto.getToTown().getName()));
                    ticket.setPassenger(passengerService.findPassengerByEmail(ticketSeedDto.getPassenger().getEmail()));
                    ticket.setPlane(planeService.findPlaneByRegisterNumber(ticketSeedDto.getPlane().getRegisterNumber()));
                    return ticket;
                })
                .forEach(ticketRepository::save);
        return sb.toString();
    }

    private boolean isEntityExist(String serialNumber) {
        return this.ticketRepository.existsBySerialNumber(serialNumber);

    }
}
