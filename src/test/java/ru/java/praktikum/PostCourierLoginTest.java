package ru.java.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.step.CouriersSteps;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.java.practikum.config.Config.BASE_URI;

//Логин курьера

public class PostCourierLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Check valid login")
    public void checkCourierLoginWithAFullSetOfFields() {
        CouriersSteps
                .create();
        Response response =
                sendPostRequestCourierLogin("src/test/resources/CourierDataLogin.json");
        compareResponseStatusCode(response, SC_OK);
        compareResponseBody(response, "id");
    }

    @Test
    @DisplayName("Check login without login")
    public void checkCourierLoginWithoutLogin() {
        Response response =
                sendPostRequestCourierLogin(null, "123");
        compareResponseStatusCode(response, SC_BAD_REQUEST);
        compareResponseBody(response, "message", "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Check login without password")
    public void checkCourierLoginWithoutPassword() {
        Response response =
                sendPostRequestCourierLogin("test", null);
        compareResponseStatusCode(response, SC_BAD_REQUEST);
        compareResponseBody(response, "message", "Недостаточно данных для входа");
    }
    @Test
    @DisplayName("Check login with incorrect login")
    public void checkCourierLoginWithincorrectLogin() {
        Response response =
                sendPostRequestCourierLogin("src/test/resources/CourierDataLoginWithIncorrectPassword.json");
        compareResponseStatusCode(response, SC_NOT_FOUND);
        compareResponseBody(response, "message", "Учетная запись не найдена");
    }
    @Test
    @DisplayName("Check login with incorrect password")
    public void checkCourierLoginWithIncorrectPassword() {
        CouriersSteps
                .create();
        Response response =
                sendPostRequestCourierLogin("ivanovii", "incorrectPassword");
        compareResponseStatusCode(response, SC_NOT_FOUND);
        compareResponseBody(response, "message", "Учетная запись не найдена");
    }


    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(String jsonPath) {
        File json = new File(jsonPath);
        Response response =
                given()
                        //.log().body()
                        .body(json)
                        .contentType(ContentType.JSON)
                        .post("/api/v1/courier/login");
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(String login, String Password) {
        Courier json = new Courier(login, Password);
        Response response =
                given()
                        .body(json)
                        .contentType(ContentType.JSON)
                        .post("/api/v1/courier/login");
        return response;
    }

    @Step("Compare status code to something")
    public void compareResponseStatusCode(Response response, int statusCode) {
        response
                .then()
                .statusCode(statusCode);
    }

    @Step("Compare body to something")
    public void compareResponseBody(Response response, String object, String value) {
        response
                .then()
                .body(object, equalTo(value));
    }

    @Step("Compare body to something")
    public void compareResponseBody(Response response, String object) {
        response
                .then()
                .body(object, notNullValue());
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

