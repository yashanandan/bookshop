package com.tw.bootcamp.bookshop.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.tw.bootcamp.bookshop.user.UserTestBuilder.buildCreateUserCommand;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldCreateUserWithValidInputs() throws InvalidEmailException {
        CreateUserCommand userCommand = buildCreateUserCommand();

        User user = userService.create(userCommand);
        Optional<User> fetchedUser = userRepository.findByEmail(userCommand.getEmail());

        assertTrue(fetchedUser.isPresent());
        assertEquals(user.getId(), fetchedUser.get().getId());
    }

    @Test
    void shouldNotCreateUserWhenUserWithSameEmailAlreadyExists() {
        CreateUserCommand userCommand = buildCreateUserCommand();
        userRepository.save(new User(userCommand));

        InvalidEmailException createUserException = assertThrows(InvalidEmailException.class,
                () -> userService.create(userCommand));
        assertEquals("User with same email already created", createUserException.getMessage());
    }
}