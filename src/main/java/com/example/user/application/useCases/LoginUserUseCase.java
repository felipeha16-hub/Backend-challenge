package com.example.user.application.useCases;


import com.example.user.application.dto.LoginUserDTO;
import com.example.user.application.dto.UserResponseDTO;
import com.example.user.application.mapper.UserMapper;
import com.example.user.domain.exceptions.BusinessException;
import com.example.user.domain.exceptions.messages.BusinessErrorMessage;
import com.example.user.domain.model.User;
import com.example.user.domain.repository.UserRepository;
import com.example.user.domain.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;



@AllArgsConstructor
@Component
public class LoginUserUseCase {

    private final UserRepository repository;
    private final TokenService tokenService;

    public UserResponseDTO login(LoginUserDTO dto) {

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
