package com.tw.bootcamp.bookshop.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService() {
    }

    public User create(CreateUserCommand userCommand) {
        User newUser = new User(userCommand);
        return userRepository.save(newUser);
    }
}
