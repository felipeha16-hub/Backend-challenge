package com.example.user.application.useCases;


import com.example.user.application.dto.LoginUserDTO;
import com.example.user.application.dto.UserResponseDTO;
import com.example.user.domain.exceptions.BusinessException;
import com.example.user.domain.exceptions.messages.BusinessErrorMessage;
import com.example.user.domain.model.User;
import com.example.user.domain.repository.UserRepository;
import com.example.user.domain.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



@AllArgsConstructor
@Component
public class LoginUserUseCase {

    private final UserRepository repository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO login(LoginUserDTO dto) {


        //Validate email and password

        User user = repository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException(BusinessErrorMessage.INCORRECT_CREDENTIALS));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(BusinessErrorMessage.INCORRECT_CREDENTIALS);
        }

        // Generate token
        String token = tokenService.generateToken(user);
        return  new UserResponseDTO(token);

    }
}
