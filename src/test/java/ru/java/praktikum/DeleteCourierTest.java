package ru.java.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.client.CourierClient;
import ru.java.practikum.dto.CourierId;
import ru.java.practikum.steps.StepsCourier;
import ru.java.practikum.steps.StepsTest;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;

//Удалить курьера
public class DeleteCourierTest {
    StepsTest stepsTest = new StepsTest();
    private CourierClient courierClient;
    String login = RandomStringUtils.random(10, true, true);
    String password = RandomStringUtils.random(10, true, true);
    String nonExistentId = RandomStringUtils.random(2, false, true);
    String nullId = "";
    String firstName = RandomStringUtils.random(10,true,false);

    @Before
    public void setUp() {
        courierClient = new CourierClient(new StepsCourier());
    }
    @Test
    @DisplayName("Сheck successful delete of courier")
    public void checkCourierDelete() {
        courierClient.create(login, password, firstName);
        CourierId courierId =
                courierClient.login(login, password)
                        .as(CourierId.class);
        Response response =
                courierClient.delete(courierId.getId());
        stepsTest.compareResponseStatusCode(response, SC_OK);
    }
    @Test
    @DisplayName("Сheck delete courier without id")
    public void checkCourierDeleteWithoutLogin() {
        Response response =
                courierClient.delete(nullId);
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для удаления курьер");
    }
    @Test
    @DisplayName("Сheck delete courier with non-existent id")
    public void checkCourierDeleteWithNonExistentLogin() {
        Response response =
                courierClient.delete(nonExistentId);
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Курьера с таким id нет");
    }
    @After
    public void deleteTestCourier() {
        CourierId courierId =
                courierClient.login(login, password)
                        .as(CourierId.class);
        courierClient.delete(courierId.getId());
    }
}
