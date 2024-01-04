package ru.java.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.step.CouriersSteps;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static ru.java.practikum.config.Config.BASE_URI;

// Создание курьера POST request to /api/v1/courier"
public class PostCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Сheck courier creation")
    @Description("Сhecking the creation of a new courier with a full set of fields")
    public void checkCourierCreationWithAFullSetOfFields() {
        Response response =
                sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        compareResponseStatusCode(response,SC_CREATED);
        compareResponseBody(response,"ok",true);
    }
    @Test
    @DisplayName("Сheck duplicate courier creation")
    @Description("Сhecking the creation of a duplicate courier with a full set of fields")
    public void checkDuplicateCourierCreation() {
        sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        Response response =
                sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        compareResponseStatusCode(response,SC_CONFLICT);
        compareResponseBody(response,"message","Этот логин уже используется");
    }
    @Test
    @DisplayName("Сheck courier creation without login field ")
    public void checkCourierCreationWithoutLogin() {
        Response response =
                sendPostRequestCourier("src/test/resources/CourierDataCreateWithoutLogin.json");
        compareResponseStatusCode(response,SC_BAD_REQUEST);
        compareResponseBody(response,"message","Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Сheck courier creation without login field ")
    public void checkCourierCreationWithoutPassword() {
        Response response =
                sendPostRequestCourier("src/test/resources/CourierDataCreateWithoutPassword.json");
        compareResponseStatusCode(response,SC_BAD_REQUEST);
        compareResponseBody(response,"message","Недостаточно данных для создания учетной записи");
    }
    // метод для шага "Отправить запрос":
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCourier(String jsonPath) {
        File json = new File(jsonPath);
        Response response =
                given()
                        //.log().body()
                        .body(json)
                        .contentType(ContentType.JSON)
                        .post("/api/v1/courier");
        return response;
    }

    // метод для шага "Сравнить код ответа":
    @Step("Compare status code to something")
    public void compareResponseStatusCode(Response response, int statusCode) {
        response
                .then()
                .statusCode(statusCode);
    }
    // метод для шага "Сравнить тело ответа":
    @Step("Compare body to something")
    public void compareResponseBody(Response response, String object, String value){
        response
                .then()
                .body(object, equalTo(value));
    }
    @Step("Compare body to something")
    public void compareResponseBody(Response response, String object, boolean value) {
        response
                .then()
                .body(object, equalTo(value));
    }
    @After
    public void deleteTestCourier() {
        CourierId courierId =
                CouriersSteps
                        .validLogin()
                        .as(CourierId.class);
        CouriersSteps
                .delete(courierId.getId());
    }
}
