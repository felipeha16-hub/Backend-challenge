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
    //private final PatchUserUseCase patchUserUseCase;
    //private final DeleteUserUseCase deleteUserUseCase;



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



/**
    @PatchMapping("/{id}")
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza parcialmente los datos de un usuario (email, username, contraseña, IDs de pokémones)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o email ya existe en otro usuario",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "usuario_duplicado",
                                    value = "{\"timestamp\":1771947757845,\"status\":400,\"error\":\"Bad Request\",\"message\":\"El usuario ya existe.\",\"path\":\"/api/users/1\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "usuario_no_encontrado",
                                    value = "{\"timestamp\":1771947757845,\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuario no encontrado.\",\"path\":\"/api/users/999\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "error_500",
                                    value = "{\"timestamp\":1771947757845,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Error interno del servidor.\",\"path\":\"/api/users/1\"}"
                            )
                    )
            )
    })
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {
        UserResponseDTO updatedUser = patchUserUseCase.update(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario y todos sus datos asociados de forma permanente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente (sin contenido)"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "usuario_no_encontrado",
                                    value = "{\"timestamp\":1771947757845,\"status\":404,\"error\":\"Not Found\",\"message\":\"Usuario no encontrado.\",\"path\":\"/api/users/999\"}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "error_500",
                                    value = "{\"timestamp\":1771947757845,\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Error interno del servidor.\",\"path\":\"/api/users/1\"}"
                            )
                    )
            )
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        deleteUserUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }**/
}
