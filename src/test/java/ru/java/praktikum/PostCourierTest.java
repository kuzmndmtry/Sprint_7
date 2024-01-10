package ru.java.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.client.CourierClient;
import ru.java.practikum.dto.Courier;
import ru.java.practikum.dto.CourierId;
import ru.java.practikum.steps.StepsCourier;
import ru.java.practikum.steps.StepsTest;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

// Создание курьера POST request to /api/v1/courier"
public class PostCourierTest {
    private CourierClient courierClient;
    StepsTest stepsTest = new StepsTest();
    String login = RandomStringUtils.random(10, true, true);
    String password = RandomStringUtils.random(10, true, true);
    String firstName = RandomStringUtils.random(10);

    @Before
    public void setUp() {
        courierClient = new CourierClient(new StepsCourier());
    }
    @Test
    @DisplayName("Сheck courier creation")
    @Description("Сhecking the creation of a new courier with a full set of fields")
    public void checkCourierCreationWithAFullSetOfFields() {
        Response response =
                courierClient.create(login, password, firstName);
        stepsTest.compareResponseStatusCode(response, SC_CREATED);
        stepsTest.compareResponseBody(response, "ok", true);
    }
    @Test
    @DisplayName("Сheck duplicate courier creation")
    @Description("Сhecking the creation of a duplicate courier with a full set of fields")
    public void checkDuplicateCourierCreation() {
        courierClient.create(login, password, firstName);
        Response response =
                courierClient.create(login, password, firstName);
        stepsTest.compareResponseStatusCode(response, SC_CONFLICT);
        stepsTest.compareResponseBody(response, "message", "Этот логин уже используется");
    }

        @Test
    @DisplayName("Сheck courier creation without login field ")
    public void checkCourierCreationWithoutLogin() {
        Response response =
                courierClient.create(null, password, firstName);
        stepsTest.compareResponseStatusCode(response,SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response,"message","Недостаточно данных для создания учетной записи");
    }
    @Test
    @DisplayName("Сheck courier creation without password field ")
    public void checkCourierCreationWithoutPassword() {
        Response response =
                courierClient.create(login, null, firstName);
        stepsTest.compareResponseStatusCode(response,SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response,"message","Недостаточно данных для создания учетной записи");
    }
    @After
    public void deleteTestCourier() {
        CourierId courierId =
                courierClient.login(login, password)
                        .as(CourierId.class);
        courierClient.delete(courierId.getId());
    }
}
