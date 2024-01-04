package ru.java.praktikum;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.step.CouriersSteps;
import ru.java.practikum.step.OrdersSteps;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static ru.java.practikum.config.Config.BASE_URI;

//Принять заказ
public class PutOrdersAcceptTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    @DisplayName("Сheck successful order accept")
    public void checkSuccessfulOrderAccept() {
        OrderTrack orderTrack =
                OrdersSteps
                        .postOrder("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                OrdersSteps
                        .getOrder(orderTrack.getTrack())
                        .body().as(OrderData.class);
        CouriersSteps
                .create();
        CourierId courierId =
                CouriersSteps
                        .validLogin()
                        .as(CourierId.class);
        Response response =
                sendPutAcceptOrder(courierId.getId(), orderData.getOrder().getId());
        compareResponseStatusCode(response, SC_OK);
        compareResponseBody(response, "ok", true);
    }
    @Test
    @DisplayName("Сheck order accept without courier id")
    public void checkOrderAcceptWithoutCourierId() {
        OrderTrack orderTrack =
                OrdersSteps
                        .postOrder("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                OrdersSteps
                        .getOrder(orderTrack.getTrack())
                        .body().as(OrderData.class);
        Response response =
                sendPutAcceptOrder(orderData.getOrder().getId());
        compareResponseStatusCode(response, SC_BAD_REQUEST);
        compareResponseBody(response, "message", "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Сheck order accept with incorrect courier id")
    public void checkOrderAcceptWithIncorrectCourierId() {
        OrderTrack orderTrack =
                OrdersSteps
                        .postOrder("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                OrdersSteps
                        .getOrder(orderTrack.getTrack())
                        .body().as(OrderData.class);
        Response response =
                sendPutAcceptOrder("0",orderData.getOrder().getId());
        compareResponseStatusCode(response, SC_NOT_FOUND);
        compareResponseBody(response, "message", "Курьера с таким id не существует");
    }

    @Test
    @DisplayName("Сheck order accept without order id")
    public void checkOrderAcceptWithoutOrderId() {
        OrderTrack orderTrack =
                OrdersSteps
                        .postOrder("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                OrdersSteps
                        .getOrder(orderTrack.getTrack())
                        .body().as(OrderData.class);
        CouriersSteps
                .create();
        CourierId courierId =
                CouriersSteps
                        .validLogin()
                        .as(CourierId.class);
        Response response =
                sendPutAcceptOrder(courierId.getId(), "");
        compareResponseStatusCode(response, SC_BAD_REQUEST);
        compareResponseBody(response, "message", "Недостаточно данных для поиска");
    }
    @Test
    @DisplayName("Сheck order accept with incorrect order id")
    public void checkOrderAcceptWithIncorrectOrderId() {
        CouriersSteps
                .create();
        CourierId courierId =
                CouriersSteps
                        .validLogin()
                        .as(CourierId.class);
        Response response =
                sendPutAcceptOrder(courierId.getId(), "0000");
        compareResponseStatusCode(response, SC_NOT_FOUND);
        compareResponseBody(response, "message", "Заказа с таким id не существует");
    }

    @Test
    @DisplayName("Сheck repeat order accept")
    public void checkRepeatOrderAccept() {
        OrderTrack orderTrack =
                OrdersSteps
                        .postOrder("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                OrdersSteps
                        .getOrder(orderTrack.getTrack())
                        .body().as(OrderData.class);
        CouriersSteps
                .create();
        CourierId courierId =
                CouriersSteps
                        .validLogin()
                        .as(CourierId.class);
        sendPutAcceptOrder(courierId.getId(), orderData.getOrder().getId());
        Response response =
                sendPutAcceptOrder(courierId.getId(), orderData.getOrder().getId());
        compareResponseStatusCode(response, SC_CONFLICT);
        compareResponseBody(response, "message", "Этот заказ уже в работе");
    }




    @Step("Send POST request to /api/v1/orders/accept/id?courierId=")
    public Response sendPutAcceptOrder(String courierId, String orderId) {
        Response response =
                given()
                        //.log().all()
                        .contentType(ContentType.JSON)
                        .queryParam("courierId", courierId)
                        .put("/api/v1/orders/accept/" + orderId);
        return response;
    }

    @Step("Send POST request to /api/v1/orders/accept/id?courierId=")
    public Response sendPutAcceptOrder(String orderId) {
        Response response =
                given()
                        //.log().all()
                        .contentType(ContentType.JSON)
                        .put("/api/v1/orders/accept/" + orderId);
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
