package com.eventsprocessor.service;

import com.eventsprocessor.entity.User;
import com.eventsprocessor.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUser(Long userID) {
        return userRepository.findById(userID);
    }
}
