package com.example.user.infrastructure.controller;

import com.example.user.application.dto.CreateUserDTO;
import com.example.user.application.dto.LoginUserDTO;
import com.example.user.application.dto.UserResponseDTO;
import com.example.user.application.useCases.*;
import jakarta.validation.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Gestión de usuarios y pokémones asociados")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final LoginUserUseCase loginUserUseCase;




    @PostMapping({"/register"})
    @Operation(
            summary = "Register new user",
            description = "Register new user with email, passsword"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Userd created successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data or email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "user_duplicated",
                                    value = "{\"timestamp\":1771947757845,\"status\":400,\"error\":\"Bad Request\",\"message\":\"User already exist.\",\"path\":\"/api/register\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "error_500",
                                    value = "{\"timestamp\":1771947757845,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Internal Server error.\",\"path\":\"/api/users\"}"
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
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody LoginUserDTO dto) {
        UserResponseDTO login = loginUserUseCase.login(dto);
        return ResponseEntity.ok(login);
    }





}
