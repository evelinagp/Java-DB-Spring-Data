package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.OfferSeedRootDto;
import softuni.exam.models.dto.OutputOneOfferTextDTO;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.AgentService;
import softuni.exam.service.ApartmentService;
import softuni.exam.service.OfferService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    public static final String OFFERS_FILE_PATH = "D:\\info\\info_C\\IdeaProjects\\JAVA-DB-SPRING-DATA\\EXAMS-JAVA-DB\\Real Estate Agency_Skeleton3\\src\\main\\resources\\files\\xml\\offers.xml";
    private final ApartmentService apartmentService;
    private final AgentService agentService;
    private final ModelMapper modelMapper;
    private final XMLParser xmlParser;
    private final ValidationUtil validationUtil;
    private final OfferRepository offerRepository;

    public OfferServiceImpl(ApartmentService apartmentService, ApartmentService apartmentService1, AgentService agentService, ModelMapper modelMapper, XMLParser xmlParser, ValidationUtil validationUtil, OfferRepository offerRepository) {
        this.apartmentService = apartmentService;
        this.agentService = agentService;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.offerRepository = offerRepository;
    }

    @Override
    public boolean areImported() {
        return offerRepository.count() > 0;

    }


    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));

    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class).getOffers()
                .stream().filter(offerSeedDto -> {
                    boolean isValid = validationUtil.isValid(offerSeedDto)
                            && agentService.isEntityExists(offerSeedDto.getAgent().getName())
                            && apartmentService.isEntityExists(offerSeedDto.getApartment().getId());
                    sb.append(isValid ? String.format("Successfully imported offer - %.2f",
                                    offerSeedDto.getPrice()) : "Invalid offer")
                            .append(System.lineSeparator());
                    return isValid;
                }).map(offerSeedDto -> {
                    Offer offer = this.modelMapper.map(offerSeedDto, Offer.class);
                    offer.setAgent(agentService.findByAgentName(offerSeedDto.getAgent().getName()));
                    offer.setApartment(apartmentService.findById(offerSeedDto.getApartment().getId()));
                    return offer;
                })
                .forEach(offerRepository::save);
        return sb.toString();
    }

    @Override
    public String exportOffers() {

//        List<OutputOneOfferTextDTO> bestOffers = this.offerRepository.findBestOffers();
//
//        return bestOffers.stream()
//                .map(o -> {
//                    return String.format("Agent %s with offer №%d:\n" +
//                                    "   \t-Apartment area: %.2f\n" +
//                                    "   \t--Town: %s\n" +
//                                    "   \t---Price: %.2f$", o.getFullName(), o.getOfferId(),
//                            o.getApartmentArea(),
//                            o.getTownName(),
//                            o.getPrice().setScale(2, RoundingMode.HALF_UP));
//                }).collect(Collectors.joining(System.lineSeparator()));
//    }

        StringBuilder sb = new StringBuilder();
        this.offerRepository.findBestOffers().stream()
                .forEach(offer -> {
                    sb.append(String.format("Agent %s %s with offer №%d:\n" +
                                            "   \t-Apartment area: %.2f\n" +
                                            "   \t--Town: %s\n" +
                                            "   \t---Price: %.2f$\n"
                                    , offer.getAgent().getFirstName(), offer.getAgent().getLastName(),
                                    offer.getId(), offer.getApartment().getArea(), offer.getApartment().getTown().getTownName(),
                                    offer.getPrice()/*.setScale(2, RoundingMode.HALF_UP)*/))
                            .append(System.lineSeparator());

                });

        return sb.toString();
    }
}

