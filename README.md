# Backend Challenge

You will develop a basic notification management system for authenticated users.

The system must allow each user to manage and send notifications through different channels.


## Deployed App Running on HEROKU
- [Swagger](https://takehomechallenge-d883931b8e0b.herokuapp.com/swagger-ui/index.html#/)

### Badges
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/felipeha16-hub/Backend-challenge/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/felipeha16-hub/Backend-challenge/tree/main)

[![Coverage Status](https://coveralls.io/repos/github/felipeha16-hub/Backend-challenge/badge.svg?branch=main)](https://coveralls.io/github/felipeha16-hub/Backend-challenge?branch=main)

### Features

- Create new Users
- Login User to get JWT token
- Create Notification
- Get Notifications list
- Update Notification
- Delete Notification

## Pre - Requisites
- Docker installed
- Docker compose
- Ports free: 8080 and 5432

## How to run the APP

```
./up_dev.sh
```

## How to run the Test

If you want to run the tests and see results in console you can run the command below

```
./up_test.sh
```

If you would like to view the test results more visually, you have another option: run the following command and press any key to open the results folder and then the index.html file.

This script allows you to run the tests and view the results visually, regardless of your operating system: Windows, Linux, or macOS.

```
./up_test.ps1
```




## Areas to improve

- Data should be moved from test to and external final
- Generic method should be used to mock endpoints
- A Seed migration would be useful to have an already working app with data
- The ORM is being used with Synchronize instead of migrations. Migrations would be the best option
- The time for JWT could be less than 24 hours, but for testing purposes it was left as is. However, considering a deployment in a production environment, this should be much shorter.
- In the JWT security filter, a new ArrayList<>() could be added if permissions or roles needed to be added in the future.
- Timestamp could be other format more friendly


## Techs

- Java : 21
- Spring boot : 3.4.2
- JPA - HYBERNATE
- Postgres

## Decisions made

- Clean Architecture: To be able to handle further in the future in a proper way.



- JPA - HYBERNATE: JPA-Hibernate is the standard in Spring Boot that reduces repetitive code, improves security, and facilitates maintenance.
 


- Docker: To make portable



- Tests: E2E tests were performed because it was the scenario closest to the reality of the end user.

    - Realistic Validation: Complete flows executed against a test database to ensure JWT persistence integrity and security.
    - Testing Tools: JUnit 5, Mockito, and AssertJ.



- Technical decisions:

    - In the case where a user wants to delete or update a notification 404 instead of 403 to avoid resource enumeration. This way, I don't reveal information about the existence of other users' notifications, reinforcing system privacy.
    - Two methods were implemented for logging notifications:
      - Logs for templates, payloads, or recording of numbers or dates of submissions
      - Storing data in the database with a new status column that indicates whether the notification was successful.
    - To increase the coverage of tests for EmailNotificationAdapter.java, a Unit Test was implemented to simulate an email service and validate errors within it.

    
## Route

- Local : [Swagger](http://localhost:8080/swagger-ui/index.html#/)

## Env vars should be defined

To find an example of the values you can use .env.example


