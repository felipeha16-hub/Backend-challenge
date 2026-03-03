package com.example.user.application.useCases;

import com.example.user.application.dto.CreateUserDTO;
import com.example.user.application.dto.UserResponseDTO;
import com.example.user.application.mapper.UserMapper;
import com.example.user.domain.exceptions.BusinessException;
import com.example.user.domain.exceptions.messages.BusinessErrorMessage;
import com.example.user.domain.model.User;
import com.example.user.domain.repository.UserRepository;
import com.example.user.domain.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/**
 * CreateUserUseCase: Caso de uso para crear un nuevo usuario
 *
 * Validaciones:
 * - El email no debe estar duplicado (400 Bad Request)
 * - La contraseña debe tener al menos 8 caracteres (400 Bad Request)
 * - El email debe ser válido (delegado a @Email en DTO)
 */
@AllArgsConstructor
@Component
public class LoginUserUseCase {

    private final UserRepository repository;
    private final TokenService tokenService;

    public UserResponseDTO create(CreateUserDTO dto) {

        //Transform DTO a dominio
        User user = UserMapper.toDomain(dto);

        //Validate email and password
        if (!repository.existsByEmail(user.getEmail())) {
            throw new BusinessException(BusinessErrorMessage.USER_NOT_FOUND);
        }

        if (repository.passwordMatches(user.getEmail(),user.getPassword())) {
            throw new BusinessException(BusinessErrorMessage.INCORRECT_PASSWORD);
        }



        // Generate token
        String token = tokenService.generateToken(user);

        // 6. Retornar DTO de respuesta
        return UserMapper.toDTO(token);
    }
}
