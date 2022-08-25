package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.SellerIdDto;
import softuni.exam.models.dto.SellerSeedRootDto;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.CarService;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XMLParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class SellerServiceImpl implements SellerService {
    public static final String SELLERS_FILE_PATH = "src/main/resources/files/xml/sellers.xml";

    private final SellerRepository sellerRepository;
    private final XMLParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public SellerServiceImpl(SellerRepository sellerRepository, XMLParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.sellerRepository = sellerRepository;
         this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;

    }


    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(SELLERS_FILE_PATH));

    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        //FOR DEBUG ->        PostSeedRootDto postSeedRootDto = xmlParser.fromFile(SELLERS_FILE_PATH, SellerSeedRootDto.class);
        StringBuilder sb = new StringBuilder();
        xmlParser.fromFile(SELLERS_FILE_PATH, SellerSeedRootDto.class).getSellers().stream()
                .filter(sellerSeedDto -> {
                    boolean isValid = validationUtil.isValid(sellerSeedDto)
                            && !isEntityExist(sellerSeedDto.getEmail());
                    sb.append(isValid
                                    ? String.format("Successfully imported seller %s- %s",
                                    sellerSeedDto.getLastName(),  sellerSeedDto.getEmail()) : "Invalid seller")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(sellerSeedDto -> modelMapper.map(sellerSeedDto, Seller.class))
                .forEach(sellerRepository::save);
        return sb.toString();
    }

    @Override
    public boolean isEntityExist(Long id) {
        return this.sellerRepository.existsById(id);
    }

    @Override
    public Seller findById(Long id) {
        return sellerRepository.findById(id).orElse(null);
    }

    private boolean isEntityExist(String email) {
        return this.sellerRepository.existsByEmail(email);
    }
}
