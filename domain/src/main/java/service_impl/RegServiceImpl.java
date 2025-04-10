package service_impl;

import dto.RegRequest;
import dto.RegResponse;
import entity.UserSQL;
import repository.UserRepository;
import service_interface.RegService;

import java.util.Optional;

public class RegServiceImpl implements RegService {
    private final UserRepository userRepository;

    public RegServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public RegResponse registration(RegRequest request) {
        Optional<UserSQL> userSQLOptional = userRepository.findByUsername(request.getLogin());
        if (userSQLOptional.isPresent())
            return new RegResponse(false);
        else
            return new RegResponse(true);
    }
}
