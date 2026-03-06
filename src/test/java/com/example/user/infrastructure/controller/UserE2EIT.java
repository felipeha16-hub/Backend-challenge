package com.example.user.infrastructure.controller;

import com.example.user.application.dto.*;
import com.example.user.infrastructure.persistence.JpaNotificationRepository;
import com.example.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserE2EIT {


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


    @Test
    @DisplayName("E2E POST /users/register & /users/login - Success")
    void shouldRegisterAndLoginUserSuccessfully() {
        // POST: create User
        CreateUserDTO createRequest = new CreateUserDTO( "brock@pewter.com", "password123");

        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/api/v1/users/register", createRequest, Void.class);
        //201
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Check that the user was actually created in the database
        assertThat(jpaUserRepository.findByEmail("brock@pewter.com")).isPresent();

        LoginUserDTO createRequestLogin = new LoginUserDTO( "brock@pewter.com", "password123");

        // POST: Login User
        ResponseEntity<UserResponseDTO> createResponseLogin = restTemplate.postForEntity("/api/v1/users/login", createRequestLogin, UserResponseDTO.class
        );
        //200
        assertThat(createResponseLogin.getStatusCode()).isEqualTo(HttpStatus.OK);

        //check that the response contains a token
        String token = createResponseLogin.getBody().getToken();
        assertThat(token).isNotNull();

    }


    @Test
    @DisplayName("E2E POST /users/login - Returns 401 with wrong password or email")
    void shouldReturn401WhenLoginWithInvalidPasswordorEmail(){

        // POST: create User
        CreateUserDTO createRequest = new CreateUserDTO( "brock@pewter.com", "password123");
        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/api/v1/users/register", createRequest, Void.class);

        //201
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        LoginUserDTO incorrectPassword = new LoginUserDTO( "brock@pewter.com", "password123as");
        LoginUserDTO incorrectEmail = new LoginUserDTO( "brockdsd@pewter.com", "password123");

        //POST: Login User with wrong password
        ResponseEntity<UserResponseDTO> response1 = restTemplate.postForEntity("/api/v1/users/login", incorrectPassword, UserResponseDTO.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        //POST: Login User with wrong email
        ResponseEntity<UserResponseDTO> response2 = restTemplate.postForEntity("/api/v1/users/login", incorrectEmail, UserResponseDTO.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }




    @Test
    @DisplayName("E2E POST /notifications/create - Create notification with Email channel and validate that the status is SENT")
    void shouldCreateNotificationWitEmailChannelWhenAuthenticated() {

        //check that the response contains a token
        String token = getToken("juan@example.com","pasword123");
        assertThat(token).isNotNull();

        // Email Channel
        CreateNotificationDTO dtoEmail = new CreateNotificationDTO("Test", "Content", "Email");

        // Set the Authorization header with the token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<CreateNotificationDTO> request = new HttpEntity<>(dtoEmail, headers);
        ResponseEntity<NotificationResponseDTO> responseNotification = restTemplate.postForEntity("/api/v1/notifications/create", request, NotificationResponseDTO.class);

        assertThat(responseNotification.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long notificationId = responseNotification.getBody().getId();
        assertThat(jpaNotificationRepository.findById(notificationId)).isPresent();
        assertThat(jpaNotificationRepository.findById(notificationId)).get().extracting("status").isEqualTo("SENT");

    }


    @Test
    @DisplayName("E2E POST /notifications/create - Create notification with SMS channel and validate that the status is SENT and the content is cut when more than 160 characters")
    void shouldCreateNotificationWitSMSChannelWhenAuthenticated(){
        //check that the response contains a token
        String token = getToken("juan@example.com","pasword123");
        assertThat(token).isNotNull();

        // Set the Authorization header with the token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        
        //SMS Channel
        CreateNotificationDTO dtoSMS = new CreateNotificationDTO("Test SMS", "Content SMS with more of 160 chracteres to validate if cant to cut lenght of mesages content so .........................................................................................................................", "SMS");

        // Create notification with SMS channel
        HttpEntity<CreateNotificationDTO> requestSMS = new HttpEntity<>(dtoSMS, headers);
        ResponseEntity<NotificationResponseDTO> responseNotificationSMS = restTemplate.postForEntity("/api/v1/notifications/create", requestSMS, NotificationResponseDTO.class);

        assertThat(responseNotificationSMS.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long notificationIdSMS = responseNotificationSMS.getBody().getId();
        assertThat(jpaNotificationRepository.findById(notificationIdSMS)).isPresent();
        assertThat(jpaNotificationRepository.findById(notificationIdSMS)).get().extracting("status").isEqualTo("SENT");
        assertThat(jpaNotificationRepository.findById(notificationIdSMS)).get().extracting("content").asString().hasSize(160);


    }

    @Test
    @DisplayName("E2E POST /notifications/create - Create notification with Push Notification channel and validate that the status is SENT")
    void shouldCreateNotificationWitPushNotificationChannelWhenAuthenticated(){

        //check that the response contains a token
        String token = getToken("juan@example.com","pasword123");
        assertThat(token).isNotNull();

        // Set the Authorization header with the token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        //Push Notification Channel
        CreateNotificationDTO dtoPush = new CreateNotificationDTO("Test Push", "Content Push", "Push Notification");
        // Create notification with Push Notification channel
        HttpEntity<CreateNotificationDTO> requestPush = new HttpEntity<>(dtoPush, headers);
        ResponseEntity<NotificationResponseDTO> responseNotificationPush = restTemplate.postForEntity("/api/v1/notifications/create", requestPush, NotificationResponseDTO.class);

        assertThat(responseNotificationPush.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long notificationIdPush = responseNotificationPush.getBody().getId();
        assertThat(jpaNotificationRepository.findById(notificationIdPush)).isPresent();
        assertThat(jpaNotificationRepository.findById(notificationIdPush)).get().extracting("status").isEqualTo("SENT");

    }



    @Test
    @DisplayName("E2E CRUD /notifications - Create, Get, Update and Delete notification and validate the responses and the database changes")
    void shouldGetNotificationsByUserWhenAuthenticated() {

        //Get Token
        String token = getToken("jua@example.com", "pasword123");
        assertThat(token).isNotNull();

        //Create notification
        CreateNotificationDTO dtoEmail = new CreateNotificationDTO("Test", "Content", "Email");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        HttpEntity<CreateNotificationDTO> request = new HttpEntity<>(dtoEmail, headers);

        ResponseEntity<NotificationResponseDTO> responseNotification = restTemplate.postForEntity("/api/v1/notifications/create", request, NotificationResponseDTO.class);

        assertThat(responseNotification.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        //Get notification by User
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);
        ResponseEntity<List<NotificationResponseDTO>> response = restTemplate.exchange("/api/v1/notifications", org.springframework.http.HttpMethod.GET, getRequest, new ParameterizedTypeReference<List<NotificationResponseDTO>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);



        //Patch notification
        Long notificationId = response.getBody().get(0).getId();
        UpdateNotificationDTO dtoUpdate = new UpdateNotificationDTO("Title actualizado", "Content actualizado", "Email");

        HttpEntity<UpdateNotificationDTO> requestUpdate = new HttpEntity<>(dtoUpdate, headers);
        ResponseEntity<NotificationResponseDTO> responseUpdate = restTemplate.exchange(
                "/api/v1/notifications/update/" + notificationId, org.springframework.http.HttpMethod.PATCH, requestUpdate, NotificationResponseDTO.class);

        assertThat(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseUpdate.getBody().getTitle()).isEqualTo("Title actualizado");


        //Delete notification
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<Void> responseDelete = restTemplate.exchange("/api/v1/notifications/delete/" + notificationId, org.springframework.http.HttpMethod.DELETE, deleteRequest, Void.class);

        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(jpaNotificationRepository.findById(notificationId)).isNotPresent();

    }

    @Test
    @DisplayName("E2E return 400 when create User with invaliod email or password")
    void shouldReturn400WhenCreateUserWithInvalidEmailOrPassword() {


        CreateUserDTO invalidEmail = new CreateUserDTO( "brock", "password123");
        CreateUserDTO invalidPassword = new CreateUserDTO( "brock@mail.com", "1");

        //POST: Create user with invalid email
        ResponseEntity<Void> createResponse = restTemplate.postForEntity("/api/v1/users/register", invalidEmail, Void.class);
        //400
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);



        // Check that the user was actually created in the database
        assertThat(jpaUserRepository.findByEmail("brock")).isNotPresent();

        //POST: Create user with invalid password
        ResponseEntity<String> createResponse2 = restTemplate.postForEntity("/api/v1/users/register", invalidPassword, String.class);
        //400
        assertThat(createResponse2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(createResponse2.getBody()).contains("Password should have 8 characters");

        // Check that the user was actually created in the database
        assertThat(jpaUserRepository.findByEmail("brock@mail.com")).isNotPresent();

    }

    @Test
    @DisplayName("E2E return 400 when create notification with invalid data")
    void shouldReturn400WhenCreateNotificationWithInvalidData() {
        //Get Token
        String token = getToken("juan@example", "pasword123");
        assertThat(token).isNotNull();

        // Create notification with invalid channel
        CreateNotificationDTO invalidChannel = new CreateNotificationDTO("Test Invalid", "Content Invalid", "Invalid Channel");

        // Set the Authorization header with the token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<CreateNotificationDTO> requestInvalidChannel = new HttpEntity<>(invalidChannel, headers);
        ResponseEntity<String> responseNotificationInvalid = restTemplate.postForEntity(
                "/api/v1/notifications/create",
                requestInvalidChannel,
                String.class);

        assertThat(responseNotificationInvalid.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseNotificationInvalid.getBody()).contains("Invalid notification channel, valid channels : Email, SMS, Push Notification ");

        // Create notification with empty title
        CreateNotificationDTO emptyTitle = new CreateNotificationDTO("", "Content", "Email");
        HttpEntity<CreateNotificationDTO> requestEmptyTitle = new HttpEntity<>(emptyTitle, headers);
        ResponseEntity<String> responseNotificationEmptyTitle = restTemplate.postForEntity(
                "/api/v1/notifications/create",
                requestEmptyTitle,
                String.class);

        assertThat(responseNotificationEmptyTitle.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);


        // Create notification with empty content
        CreateNotificationDTO emptyContent = new CreateNotificationDTO("Test", "", "Email");
        HttpEntity<CreateNotificationDTO> requestEmptyContent = new HttpEntity<>(emptyContent, headers);
        ResponseEntity<String> responseNotificationEmptyContent = restTemplate.postForEntity(
                "/api/v1/notifications/create",
                requestEmptyContent,
                String.class);

        assertThat(responseNotificationEmptyContent.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    

    @Test
    @DisplayName("E2E return 404 when User try to update or delete a notification that not exist")
    void shouldReturn404WhenUpdateOrDeleteNotificationThatNotExist() {
        //Get Token
        String token = getToken("juan@example", "pasword123");
        assertThat(token).isNotNull();

        // Set the Authorization header with the token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        // Try to update a notification that not exist
        UpdateNotificationDTO dtoUpdate = new UpdateNotificationDTO("Title actualizado", "Content actualizado", "Email");
        HttpEntity<UpdateNotificationDTO> requestUpdate = new HttpEntity<>(dtoUpdate, headers);
        ResponseEntity<String> responseUpdate = restTemplate.exchange(
                "/api/v1/notifications/update/9999999", org.springframework.http.HttpMethod.PATCH, requestUpdate, String.class);

        assertThat(responseUpdate.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);


        // Try to delete a notification that not exist
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<String> responseDelete = restTemplate.exchange("/api/v1/notifications/delete/9999", org.springframework.http.HttpMethod.DELETE, deleteRequest, String.class);

        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

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