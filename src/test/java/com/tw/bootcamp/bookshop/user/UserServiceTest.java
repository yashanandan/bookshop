package com.tw.bootcamp.bookshop.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateUserWithValidInputs() {
        String email = "testemail@test.com";
        CreateUserCommand userCommand = new CreateUserCommand(email, "foobar");

        User user = userService.create(userCommand);
        Optional<User> fetchedUser = userRepository.findByEmail(email);

        assertTrue(fetchedUser.isPresent());
        assertEquals(user.getId(), fetchedUser.get().getId());
    }
}