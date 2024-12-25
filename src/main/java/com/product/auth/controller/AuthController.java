package com.product.auth.controller;


import com.product.auth.dtos.LoginUserDto;
import com.product.auth.dtos.RegisterUserDto;
import com.product.auth.entity.User;
import com.product.auth.responses.LoginResponse;
import com.product.auth.service.AuthenticationService;
import com.product.auth.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/v1/auth")
@RestController
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "Add new user", description = "Add new user with username and password with role USER or ADMIN")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful User Added", content = @Content(schema = @Schema(implementation = AuthController.AuthResponse.class))), @ApiResponse(responseCode = "401", description = "Invalid User", content = @Content(schema = @Schema(implementation = AuthController.ErrorResponse.class)))})
    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @Operation(summary = "Login and generate JWT token", description = "Authenticate a user with username and password, and generate a JWT token.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful authentication", content = @Content(schema = @Schema(implementation = AuthController.AuthResponse.class))), @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content(schema = @Schema(implementation = AuthController.ErrorResponse.class)))})
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @Operation(summary = "Find All Users", description = "Find All Users")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Get All Users", content = @Content(schema = @Schema(implementation = AuthController.AuthResponse.class))), @ApiResponse(responseCode = "401", description = "Invalid username or password", content = @Content(schema = @Schema(implementation = AuthController.ErrorResponse.class)))})
    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(authenticationService.allUsers());
    }

    // Inner class for authentication response
    private record AuthResponse(String token) {
    }

    // Inner class for error response
    private record ErrorResponse(String message) {
    }
}