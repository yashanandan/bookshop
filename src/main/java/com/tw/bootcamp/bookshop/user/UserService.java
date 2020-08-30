package com.tw.bootcamp.bookshop.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService() {
    }

    public User create(CreateUserCommand userCommand) throws InvalidEmailException {
        Optional<User> user = userRepository.findByEmail(userCommand.getEmail());
        if (user.isPresent()) {
            throw new InvalidEmailException();
        }
        User newUser = new User(userCommand);
        return userRepository.save(newUser);
    }
}
