package com.product.auth.service;

import com.product.auth.dtos.LoginUserDto;
import com.product.auth.dtos.RegisterUserDto;
import com.product.auth.entity.User;
import com.product.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    public static final String TESTUSER = "testuser";
    public static final String USER = "USER";
    public static final String ENCODED_PASSWORD = "encodedPassword";
    public static final String PASSWORD = "password123";
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = new AuthenticationService(userRepository, authenticationManager, passwordEncoder);
    }

    @Test
    void testSignup() {
        // Arrange
        RegisterUserDto input = new RegisterUserDto();
        input.setUsername(TESTUSER);
        input.setPassword(PASSWORD);
        input.setRole(USER);

        User mockUser = new User();
        mockUser.setUsername(TESTUSER);
        mockUser.setRole(USER);
        mockUser.setPassword(ENCODED_PASSWORD);

        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        User result = authenticationService.signup(input);

        // Assert
        assertNotNull(result);
        assertEquals(TESTUSER, result.getUsername());
        assertEquals(USER, result.getRole());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testAuthenticate() {
        // Arrange
        LoginUserDto input = new LoginUserDto();
        input.setUsername(TESTUSER);
        input.setPassword(PASSWORD);

        User mockUser = new User();
        mockUser.setUsername(TESTUSER);

        when(userRepository.findByusername(TESTUSER)).thenReturn(Optional.of(mockUser));

        // Act
        User result = authenticationService.authenticate(input);

        // Assert
        assertNotNull(result);
        assertEquals(TESTUSER, result.getUsername());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setUsername("user1");

        User user2 = new User();
        user2.setUsername("user2");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        var result = authenticationService.allUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }
}
