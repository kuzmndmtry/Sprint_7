package ru.java.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static ru.java.practikum.config.Config.BASE_URI;

// Создание курьера POST request to /api/v1/courier"
public class PostCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }
    StepsCourier stepsCourier = new StepsCourier();
    StepsTest stepsTest = new StepsTest();

    @Test
    @DisplayName("Сheck courier creation")
    @Description("Сhecking the creation of a new courier with a full set of fields")
    public void checkCourierCreationWithAFullSetOfFields() {
//        StepsCourier stepsCourier = new StepsCourier();
//        StepsTest stepsTest = new StepsTest();
        Response response =
                stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        stepsTest.compareResponseStatusCode(response, SC_CREATED);
        stepsTest.compareResponseBody(response, "ok", true);
    }
    @Test
    @DisplayName("Сheck duplicate courier creation")
    @Description("Сhecking the creation of a duplicate courier with a full set of fields")
    public void checkDuplicateCourierCreation() {
        stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        Response response =
                stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        stepsTest.compareResponseStatusCode(response,SC_CONFLICT);
        stepsTest.compareResponseBody(response,"message","Этот логин уже используется");
    }
    @Test
    @DisplayName("Сheck courier creation without login field ")
    public void checkCourierCreationWithoutLogin() {
        Response response =
        stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreateWithoutLogin.json");
        stepsTest.compareResponseStatusCode(response,SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response,"message","Недостаточно данных для создания учетной записи");
    }
    @Test
    @DisplayName("Сheck courier creation without login field ")
    public void checkCourierCreationWithoutPassword() {
        Response response =
                stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreateWithoutPassword.json");
        stepsTest.compareResponseStatusCode(response,SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response,"message","Недостаточно данных для создания учетной записи");
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
