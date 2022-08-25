package hiberspring.service.impl;

import com.google.gson.Gson;
import hiberspring.common.Constants;
import hiberspring.domain.dtos.BranchSeedDto;
import hiberspring.domain.entities.Branch;
import hiberspring.repository.BranchRepository;
import hiberspring.service.BranchService;
import hiberspring.service.TownService;
import hiberspring.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class BranchServiceImpl implements BranchService {
    public static final String BRANCH_PATH = Constants.PATH_TO_FILES + "branches.json";

    private final BranchRepository branchRepository;
    private final TownService townService;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, TownService townService, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.branchRepository = branchRepository;
        this.townService = townService;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public Boolean branchesAreImported() {
        return this.branchRepository.count() > 0;
    }


    @Override
    public String readBranchesJsonFile() throws IOException {
        return Files.readString(Path.of(BRANCH_PATH));

    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        BranchSeedDto[] branchSeedDtos = this.gson.fromJson(readBranchesJsonFile(), BranchSeedDto[].class);
        StringBuilder sb = new StringBuilder();
        Arrays.stream(gson.fromJson(readBranchesJsonFile(), BranchSeedDto[].class))
                .filter(branchSeedDto -> {
                    boolean isValid = validationUtil.isValid(branchSeedDto)
                            && !isEntityExist(branchSeedDto.getName())
                            && townService.isEntityExist(branchSeedDto.getTown());
                    sb.append(isValid ? String.format(Constants.SUCCESSFUL_IMPORT_MESSAGE,
                                    Branch.class.getSimpleName(), branchSeedDto.getName()) : Constants.INCORRECT_DATA_MESSAGE)
                            .append(System.lineSeparator());
                    return isValid;
                }).map(branchSeedDto -> {
                    Branch branch = modelMapper.map(branchSeedDto, Branch.class);
                    branch.setTown(townService.findByTownName(branchSeedDto.getTown()));
                    return branch;
                })
                .forEach(branchRepository::save);
        return sb.toString();
    }
    @Override
    public boolean isEntityExist(String name) {
        return branchRepository.existsByName(name);

    }

    @Override
    public Branch findByBranchName(String name) {
        return branchRepository.findByName(name).orElse(null);

    }
}
