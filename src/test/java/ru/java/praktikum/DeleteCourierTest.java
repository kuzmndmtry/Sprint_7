package ru.java.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.step.CouriersSteps;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.java.practikum.config.Config.BASE_URI;

//Удалить курьера
public class DeleteCourierTest {
    private CouriersSteps couriersSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Сheck successful delete of courier")
    public void checkCourierDelete() {
        CouriersSteps
                .create();
        CourierId courierId =
                CouriersSteps
                        .validLogin()
                        .as(CourierId.class);
        Response response =
                sendDeleteRequestCourier(courierId.getId());
        compareResponseStatusCode(response, SC_OK);
    }

    @Test
    @DisplayName("Сheck delete courier without login")
    public void checkCourierDeleteWithoutLogin() {
        Response response =
                sendDeleteRequestCourier();
        compareResponseStatusCode(response, SC_BAD_REQUEST);
        compareResponseBody(response,"message", "Недостаточно данных для удаления курьер");

    }

    @Test
    @DisplayName("Сheck delete courier with non-existent login")
    public void checkCourierDeleteWithNonExistentLogin() {
        Response response =
                sendDeleteRequestCourier("0");
        compareResponseStatusCode(response, SC_NOT_FOUND);
        compareResponseBody(response,"message", "Курьера с таким id нет");

    }
    @Step("Send POST request to /api/v1/courier/login")
    public Response sendDeleteRequestCourier(String id) {
        Response response =
                given()
                        //.log().body()
                        .delete("/api/v1/courier/" + id);
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendDeleteRequestCourier() {
        Response response =
                given()
                        //.log().body()
                        .delete("/api/v1/courier/");
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




}
