package ru.java.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static ru.java.practikum.config.Config.BASE_URI;

//Логин курьера
public class PostCourierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }
    StepsCourier stepsCourier = new StepsCourier();
    StepsTest stepsTest = new StepsTest();
    @Test
    @DisplayName("Check valid login")
    public void checkCourierLoginWithAFullSetOfFields() {
        stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        Response response =
                stepsCourier.sendPostRequestCourierLogin("src/test/resources/CourierDataLogin.json");
        stepsTest.compareResponseStatusCode(response, SC_OK);
        stepsTest.compareResponseBody(response, "id");
    }

    @Test
    @DisplayName("Check login without login")
    public void checkCourierLoginWithoutLogin() {
        Response response =
                stepsCourier.sendPostRequestCourierLogin(null, "123");
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для входа");
    }
    @Test
    @DisplayName("Check login without password")
    public void checkCourierLoginWithoutPassword() {
        Response response =
                stepsCourier.sendPostRequestCourierLogin("test", null);
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для входа");
    }
    @Test
    @DisplayName("Check login with incorrect login")
    public void checkCourierLoginWithIncorrectLogin() {
        Response response =
                stepsCourier.sendPostRequestCourierLogin("src/test/resources/CourierDataCreateWithIncorrectPassword.json");
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Учетная запись не найдена");
    }
    @Test
    @DisplayName("Check login with incorrect password")
    public void checkCourierLoginWithIncorrectPassword() {
        stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        Response response =
                stepsCourier.sendPostRequestCourierLogin("src/test/resources/CourierDataCreateWithIncorrectPassword.json");
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Учетная запись не найдена");
    }

    @After
    public void deleteTestCourier() {
    //StepsCourier stepsCourier = new StepsCourier();
    CourierId courierId =
            stepsCourier
                    .sendPostRequestCourierLogin("src/test/resources/CourierDataCreate.json")
                    .as(CourierId.class);
        stepsCourier
                .sendDeleteRequestCourier(courierId.getId());
    }
}

