package ru.java.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static ru.java.practikum.config.Config.BASE_URI;

public class GetOrdersTrackTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }
    StepsTest stepsTest = new StepsTest();
    StepsOrders stepsOrders = new StepsOrders();

    @Test
    @DisplayName("Сheck successful get order")
    public void checkSuccessfulGetOrder() {
        OrderTrack orderTrack =
                stepsOrders.sendPostRequestOrders("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        Response response =
                stepsOrders.sendGetOrderTrack(orderTrack.getTrack());
        stepsTest.compareResponseStatusCode(response, SC_OK);
        stepsTest.compareResponseBody(response, "order");
    }
    @Test
    @DisplayName("Сheck get order without track")
    public void checkGetOrderWithoutTrack() {
        Response response =
                stepsOrders.sendGetOrderTrack("");
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Сheck get order wit incorrect track")
    public void checkGetOrderWithIncorrectTrack() {
        Response response =
                stepsOrders.sendGetOrderTrack("0000");
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Заказ не найден");
    }
}
