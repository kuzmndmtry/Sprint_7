package ru.java.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.step.CouriersSteps;
import ru.java.practikum.step.OrdersSteps;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.java.practikum.config.Config.BASE_URI;

public class GetOrdersTrackTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Сheck successful get order")
    public void checkSuccessfulGetOrder() {
        OrderTrack orderTrack =
                OrdersSteps
                        .postOrder("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        Response response =
                sendGetOrderTrack(orderTrack.getTrack());
        compareResponseStatusCode(response, SC_OK);
        compareResponseBody(response, "order");
    }
    @Test
    @DisplayName("Сheck get order without track")
    public void checkGetOrderWithoutTrack() {
        Response response =
                sendGetOrderTrack("");
        compareResponseStatusCode(response, SC_BAD_REQUEST);
        compareResponseBody(response, "message", "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Сheck get order wit incorrect track")
    public void checkGetOrderWithIncorrectTrack() {
        Response response =
                sendGetOrderTrack("0000");
        compareResponseStatusCode(response, SC_NOT_FOUND);
        compareResponseBody(response, "message", "Заказ не найден");
    }

    @Step("Send POST request to /api/v1/orders/track")
    public Response sendGetOrderTrack(String track) {
        Response response =
                given()
                        //.log().all()
                        .contentType(ContentType.JSON)
                        .queryParam("t", track)
                        .get("/api/v1/orders/track");
        return response;
    }


    @Step("Compare status code to something")
    public void compareResponseStatusCode(Response response, int statusCode) {
        response
                .then()
                //.log().all()
                .statusCode(statusCode);
    }

    // метод для шага "Сравнить тело ответа":
    @Step("Compare body to something")
    public void compareResponseBody(Response response, String object, String value) {
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

    @Step("Compare body to something")
    public void compareResponseBody(Response response, String object) {
        response
                .then()
                .body(object, notNullValue());
    }
}
