package com.tw.bootcamp.bookshop.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private Validator validator;

    @InjectMocks
    private UserService userService = new UserService();

    @Test
    void shouldCreateUserWithValidInputs() throws InvalidEmailException {
        CreateUserCommand userCredentials = new CreateUserCommandTestBuilder().build();
        User user = new UserTestBuilder().withEmail(userCredentials.getEmail()).build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(userCredentials);

        ArgumentCaptor<User> argCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(argCaptor.capture());
        assertEquals(userCredentials.getEmail(), argCaptor.getValue().getEmail());
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(Role.USER, createdUser.getRole());
    }

    @Test
    void shouldNotCreateUserWhenUserWithSameEmailAlreadyExists() {
        CreateUserCommand userCommand = new CreateUserCommandTestBuilder().build();
        when(userRepository.findByEmail(userCommand.getEmail())).thenReturn(Optional.of(new User()));
        userRepository.save(User.create(userCommand));

        InvalidEmailException createUserException = assertThrows(InvalidEmailException.class,
                () -> userService.create(userCommand));
        assertEquals("User with same email already created", createUserException.getMessage());
    }

    @Test
    void shouldNotCreateUserWhenInputIsInvalid() {
        CreateUserCommand invalidCommand = new CreateUserCommandTestBuilder().withEmptyEmail().build();
        when(validator.validate(any(User.class))).thenThrow(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> userService.create(invalidCommand));
    }

    @Test
    void shouldReturnUserForGivenValidEmail() {
        String validEmailId = "testemail@test.com";
        User user = new UserTestBuilder().build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        assertEquals(user.getEmail(), userService.loadUserByUsername(validEmailId).getUsername());
    }

    @Test
    void shouldReturnUserNotFoundExceptionForGivenInValidEmail() {
        String invalidEmailId = "testemail@test";
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(invalidEmailId));
    }
}