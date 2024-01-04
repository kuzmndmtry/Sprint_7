package ru.java.praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.step.CouriersSteps;
import ru.java.practikum.step.OrdersSteps;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.java.practikum.config.Config.BASE_URI;

public class GetOrdersTest {
    private OrdersSteps ordersSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }


    @Test
    public void checkGetOrdersListWithoutParams() {
        Response response =
                sendGetRequestOrders();
        compareResponseStatusCode(response,SC_OK);
        compareResponseBody(response,"orders");
    }
    @Step("Send POST request to /api/v1/orders")
    public Response sendGetRequestOrders() {
        Response response =
                given()
                        //.log().body()
                        .get("/api/v1/orders");
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
