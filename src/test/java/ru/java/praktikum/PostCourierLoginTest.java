package ru.java.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.client.CourierClient;
import ru.java.practikum.dto.Courier;
import ru.java.practikum.dto.CourierId;
import ru.java.practikum.dto.CourierLogin;
import ru.java.practikum.steps.StepsCourier;
import ru.java.practikum.steps.StepsTest;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

//Логин курьера
public class PostCourierLoginTest {
    private CourierClient courierClient;
    StepsTest stepsTest = new StepsTest();
    String login = RandomStringUtils.random(10,true,true);
    String incorrectLogin = RandomStringUtils.random(9,true,true);
    String password = RandomStringUtils.random(10,true,true);
    String incorrectPassword = RandomStringUtils.random(12,true,true);
    String firstName = RandomStringUtils.random(10);

    @Before
    public void setUp() {
        courierClient = new CourierClient(new StepsCourier());
    }

    @Test
    @DisplayName("Check valid login")
    public void checkCourierLoginWithAFullSetOfFields() {
        courierClient.create(login, password, firstName);
        Response response =
                courierClient.login(login, password);
        stepsTest.compareResponseStatusCode(response, SC_OK);
        stepsTest.compareResponseBody(response, "id");
    }

    @Test
    @DisplayName("Check login without login")
    public void checkCourierLoginWithoutLogin() {
        Response response =
                courierClient.login(null, "123");
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Check login without password")
    public void checkCourierLoginWithoutPassword() {
        Response response =
                courierClient.login(login, null);
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для входа");
    }
    @Test
    @DisplayName("Check login with incorrect login")
    public void checkCourierLoginWithIncorrectLogin() {
        Response response =
                courierClient.login(incorrectLogin, password);
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Учетная запись не найдена");
    }
    @Test
    @DisplayName("Check login with incorrect password")
    public void checkCourierLoginWithIncorrectPassword() {
        courierClient.create(login, password, firstName);
        Response response =
                courierClient.login(login, incorrectPassword);
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Учетная запись не найдена");
    }
    @After
    public void deleteTestCourier() {
        CourierId courierId =
                courierClient.login(login, password)
                        .as(CourierId.class);
        courierClient.delete(courierId.getId());
    }
}