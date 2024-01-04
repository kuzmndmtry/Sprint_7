package ru.java.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static ru.java.practikum.config.Config.BASE_URI;

//Принять заказ
public class PutOrdersAcceptTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }
    StepsCourier stepsCourier = new StepsCourier();
    StepsTest stepsTest = new StepsTest();
    StepsOrders stepsOrders = new StepsOrders();

    @Test
    @DisplayName("Сheck successful order accept")
    public void checkSuccessfulOrderAccept() {
        OrderTrack orderTrack =
                stepsOrders.sendPostRequestOrders("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                stepsOrders.getOrderTrack(orderTrack.getTrack())
                        .body().as(OrderData.class);
        stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        CourierId courierId =
                stepsCourier.sendPostRequestCourierLogin("src/test/resources/CourierDataCreate.json")
                        .as(CourierId.class);
        Response response =
                stepsOrders.sendPutAcceptOrder(courierId.getId(), orderData.getOrder().getId());
        stepsTest.compareResponseStatusCode(response, SC_OK);
        stepsTest.compareResponseBody(response, "ok", true);
    }
    @Test
    @DisplayName("Сheck order accept without courier id")
    public void checkOrderAcceptWithoutCourierId() {
        OrderTrack orderTrack =
                stepsOrders.sendPostRequestOrders("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                stepsOrders.getOrderTrack(orderTrack.getTrack())
                        .body().as(OrderData.class);
        Response response =
                stepsOrders.sendPutAcceptOrder(orderData.getOrder().getId());
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для поиска");
    }
    @Test
    @DisplayName("Сheck order accept with incorrect courier id")
    public void checkOrderAcceptWithIncorrectCourierId() {
        OrderTrack orderTrack =
                stepsOrders.sendPostRequestOrders("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                stepsOrders.getOrderTrack(orderTrack.getTrack())
                        .body().as(OrderData.class);
        Response response =
                stepsOrders.sendPutAcceptOrder("0",orderData.getOrder().getId());
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Курьера с таким id не существует");
    }
    @Test
    @DisplayName("Сheck order accept without order id")
    public void checkOrderAcceptWithoutOrderId() {
        stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        CourierId courierId =
                stepsCourier.sendPostRequestCourierLogin("src/test/resources/CourierDataCreate.json")
                        .as(CourierId.class);
        Response response =
                stepsOrders.sendPutAcceptOrder(courierId.getId(), "");
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для поиска");
    }
    @Test
    @DisplayName("Сheck order accept with incorrect order id")
    public void checkOrderAcceptWithIncorrectOrderId() {
        stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        CourierId courierId =
                stepsCourier.sendPostRequestCourierLogin("src/test/resources/CourierDataCreate.json")
                        .as(CourierId.class);
        Response response =
                stepsOrders.sendPutAcceptOrder(courierId.getId(), "0000");
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Заказа с таким id не существует");
    }
    @Test
    @DisplayName("Сheck repeat order accept")
    public void checkRepeatOrderAccept() {
        OrderTrack orderTrack =
                stepsOrders.sendPostRequestOrders("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                stepsOrders.getOrderTrack(orderTrack.getTrack())
                        .body().as(OrderData.class);
        stepsCourier.sendPostRequestCourier("src/test/resources/CourierDataCreate.json");
        CourierId courierId =
                stepsCourier.sendPostRequestCourierLogin("src/test/resources/CourierDataCreate.json")
                        .as(CourierId.class);
        stepsOrders.sendPutAcceptOrder(courierId.getId(), orderData.getOrder().getId());
        Response response =
               stepsOrders.sendPutAcceptOrder(courierId.getId(), orderData.getOrder().getId());
        stepsTest.compareResponseStatusCode(response, SC_CONFLICT);
        stepsTest.compareResponseBody(response, "message", "Этот заказ уже в работе");
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
