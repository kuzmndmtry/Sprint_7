package ru.java.practikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.java.practikum.client.RestClient;

import java.io.File;

public class StepsOrders extends RestClient {
    private static final String ORDERS = "/api/v1/orders";
    private static final String ORDERS_ACCEPT = "/api/v1/orders/accept/";
    private static final String ORDERS_TRACK = "/api/v1/orders/track";

    @Step("Send GET request to /api/v1/orders")
    public Response sendGetRequestOrders() {
        Response response =
                getDefaultRequestSpecification()
                        .get(ORDERS);
        return response;
    }

    @Step("Send POST request to /api/v1/orders")
    public Response sendPostRequestOrders(String body) {
        File json = new File(body);
        Response response =
                getDefaultRequestSpecification()
                        .body(json)
                        .post(ORDERS);
        return response;
    }

    @Step("Send POST request to /api/v1/orders/accept/id?courierId=")
    public Response sendPutAcceptOrder(String courierId, String orderId) {
        Response response =
                getDefaultRequestSpecification()
                        .queryParam("courierId", courierId)
                        .put(ORDERS_ACCEPT + orderId);
        return response;
    }

    @Step("Send POST request to /api/v1/orders/accept/id?courierId=")
    public Response sendPutAcceptOrder(String orderId) {
        Response response =
                getDefaultRequestSpecification()
                        .put(ORDERS_ACCEPT + orderId);
        return response;
    }

    @Step("Send POST request to /api/v1/orders/track")
    public Response sendGetOrderTrack(String track) {
        Response response =
                getDefaultRequestSpecification()
                        .queryParam("t", track)
                        .get(ORDERS_TRACK);
        return response;
    }
}
