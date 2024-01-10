package ru.java.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.client.CourierClient;
import ru.java.practikum.dto.CourierId;
import ru.java.practikum.dto.OrderData;
import ru.java.practikum.dto.OrderTrack;
import ru.java.practikum.steps.*;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

//Принять заказ
public class PutOrdersAcceptTest {
    String login = RandomStringUtils.random(10, true, true);
    String password = RandomStringUtils.random(10, true, true);
    String firstName = RandomStringUtils.random(10,true,false);
    String nonExistentId = RandomStringUtils.random(2, false, true);
    private CourierClient courierClient;
    StepsTest stepsTest = new StepsTest();
    StepsOrders stepsOrders = new StepsOrders();
    @Before
    public void setUp() {
        courierClient = new CourierClient(new StepsCourier());
    }

    @Test
    @DisplayName("Сheck successful order accept")
    public void checkSuccessfulOrderAccept() {
        courierClient.create(login, password, firstName);
        CourierId courierId =
                courierClient.login(login, password)
                        .as(CourierId.class);
        OrderTrack orderTrack =
                stepsOrders.sendPostRequestOrders("src/test/resources/OrderWithColorBlack.json")
                        .as(OrderTrack.class);
        OrderData orderData =
                stepsOrders.sendGetOrderTrack(orderTrack.getTrack())
                        .body().as(OrderData.class);
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
                stepsOrders.sendGetOrderTrack(orderTrack.getTrack())
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
                stepsOrders.sendGetOrderTrack(orderTrack.getTrack())
                        .body().as(OrderData.class);
        Response response =
                stepsOrders.sendPutAcceptOrder(nonExistentId,orderData.getOrder().getId());
        stepsTest.compareResponseStatusCode(response, SC_NOT_FOUND);
        stepsTest.compareResponseBody(response, "message", "Курьера с таким id не существует");
    }
    @Test
    @DisplayName("Сheck order accept without order id")
    public void checkOrderAcceptWithoutOrderId() {
        courierClient.create(login, password, firstName);
        CourierId courierId =
                courierClient.login(login, password)
                        .as(CourierId.class);
        Response response =
                stepsOrders.sendPutAcceptOrder(courierId.getId(), "");
        stepsTest.compareResponseStatusCode(response, SC_BAD_REQUEST);
        stepsTest.compareResponseBody(response, "message", "Недостаточно данных для поиска");
    }
    @Test
    @DisplayName("Сheck order accept with incorrect order id")
    public void checkOrderAcceptWithIncorrectOrderId() {
        courierClient.create(login, password, firstName);
        CourierId courierId =
                courierClient.login(login, password)
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
                stepsOrders.sendGetOrderTrack(orderTrack.getTrack())
                        .body().as(OrderData.class);
        courierClient.create(login, password, firstName);
        CourierId courierId =
                courierClient.login(login, password)
                        .as(CourierId.class);
        stepsOrders.sendPutAcceptOrder(courierId.getId(), orderData.getOrder().getId());
        Response response =
               stepsOrders.sendPutAcceptOrder(courierId.getId(), orderData.getOrder().getId());
        stepsTest.compareResponseStatusCode(response, SC_CONFLICT);
        stepsTest.compareResponseBody(response, "message", "Этот заказ уже в работе");
    }
    @After
    public void deleteTestCourier() {
        CourierId courierId =
                courierClient.login(login, password)
                        .as(CourierId.class);
        courierClient.delete(courierId.getId());
    }
}
