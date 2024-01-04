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

//Удалить курьера
public class DeleteCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    StepsCourier stepsCourier = new StepsCourier();
    StepsTest stepsTest = new StepsTest();

    @Test
    @DisplayName("Сheck successful delete of courier")
    public void checkCourierDelete() {
        stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        CourierId courierId =
                stepsCourier.sendPostRequestCourierLogin("src/test/resources/CourierDataLogin.json")
                        .as(CourierId.class);
        Response response =
                stepsCourier.sendDeleteRequestCourier(courierId.getId());
        stepsTest.compareResponseStatusCode(response, SC_OK);
    }
    @Test
    @DisplayName("Сheck delete courier without login")
    public void checkCourierDeleteWithoutLogin() {
        Response response =
                stepsCourier.sendDeleteRequestCourier();
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для удаления курьер");

    }
    @Test
    @DisplayName("Сheck delete courier with non-existent login")
    public void checkCourierDeleteWithNonExistentLogin() {
        Response response =
                stepsCourier.sendDeleteRequestCourier("0");
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Курьера с таким id нет");
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
