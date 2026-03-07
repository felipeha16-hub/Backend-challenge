package com.example.user.infrastructure.controller;

import com.example.user.application.dto.CreateUserDTO;
import com.example.user.application.dto.LoginUserDTO;
import com.example.user.application.dto.UserResponseDTO;
import com.example.user.application.useCases.*;
import com.example.user.infrastructure.error_management.dto.ErrorResponseDTO;
import jakarta.validation.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Endpoints for user management")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final LoginUserUseCase loginUserUseCase;




    @PostMapping({"/register"})
    @Operation(
            summary = "Register new user",
            description = "Registers a new user using email and password."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "user_duplicated",
                                    value = "{\"timestamp\":1771947757845,\"status\":409,\"error\":\"Conflict\",\"message\":\"User already exist\",\"path\":\"/api/v1/users/register\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "error_500",
                                    value = "{\"timestamp\":1771947757845,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Internal Server error.\",\"path\":\"/api/v1/users/register\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserDTO dto) {
        createUserUseCase.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED).build();
    }




    @PostMapping({"/login"})
    @Operation(
            summary = "Login user",
            description = "Authenticates a user with email and password and returns an access token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Incorrect credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "incorrect_credentials",
                                    value = "{\"timestamp\":1771947757845,\"status\":401,\"error\":\"Unauthorized\",\"message\":\"incorrect credentials\",\"path\":\"/api/v1/users/login\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class),
                            examples = @ExampleObject(
                                    name = "error_500",
                                    value = "{\"timestamp\":1771947757845,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Internal Server Error\",\"path\":\"/api/v1/users/login\"}"
                            )
                    )
            )
    })
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody LoginUserDTO dto) {
        UserResponseDTO login = loginUserUseCase.login(dto);
        return ResponseEntity.ok(login);
    }





}
