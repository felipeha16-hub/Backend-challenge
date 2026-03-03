package com.example.user.application.useCases;

import com.example.user.application.dto.CreateUserDTO;
import com.example.user.application.dto.UserResponseDTO;
import com.example.user.application.mapper.UserMapper;
import com.example.user.domain.exceptions.BusinessException;
import com.example.user.domain.exceptions.messages.BusinessErrorMessage;
import com.example.user.domain.model.User;
import com.example.user.domain.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;


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
public class CreateUserUseCase {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public void create(CreateUserDTO dto) {

        //Transform DTO a dominio
        User user = UserMapper.toDomain(dto);

        //Validate that the email does not exist
        if (repository.existsByEmail(user.getEmail())) {
            throw new BusinessException(BusinessErrorMessage.USER_ALREADY_EXISTS);
        }

        // 3. Validate that the password is secure
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new BusinessException(BusinessErrorMessage.INVALID_PASSWORD);
        }

        // 4. Hash the password before persisting
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 5. Save to repository
        User savedUser = repository.save(user);

    }


}
