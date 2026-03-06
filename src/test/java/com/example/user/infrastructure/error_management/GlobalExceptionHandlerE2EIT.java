package com.example.user.infrastructure.error_management;


import com.example.user.application.dto.*;
import com.example.user.infrastructure.persistence.JpaNotificationRepository;
import com.example.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")

public class GlobalExceptionHandlerE2EIT {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private JpaNotificationRepository jpaNotificationRepository;


    @BeforeEach
    void setUp() {
        jpaNotificationRepository.deleteAll();
        jpaUserRepository.deleteAll();
    }

    // Mock the JpaNotificationRepository to throw an exception when save is called, to test the 500 error handling in the controller
    @MockitoBean
    private JpaNotificationRepository mockNotificationRepository;

    @Test
    @DisplayName("E2E POST /notifications/create - Returns 500 when database fails unexpectedly")
    void shouldReturn500WhenUnexpectedDatabaseErrorOccurs() {
        //get Token
        String token = getToken("error500@example.com", "password123");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // force the repository to throw an exception when save is called
        // this simulates an unexpected database error, such as a connection failure
        when(mockNotificationRepository.save(any())).thenThrow(new RuntimeException("Database connection lost"));

        CreateNotificationDTO dto = new CreateNotificationDTO("Test", "Content", "Email");
        HttpEntity<CreateNotificationDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/notifications/create", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);


    }


    //Helper method to get token
    public String getToken(String email, String password) {
        //User create to log in
        restTemplate.postForEntity("/api/v1/users/register", new CreateUserDTO( email, password), Void.class);
        assertThat(jpaUserRepository.findByEmail(email)).isPresent();
        //User login
        ResponseEntity<UserResponseDTO> createResponseLogin = restTemplate.postForEntity("/api/v1/users/login", new LoginUserDTO(email, password), UserResponseDTO.class);
        //get the token from the response
        return createResponseLogin.getBody().getToken();
    }

}
