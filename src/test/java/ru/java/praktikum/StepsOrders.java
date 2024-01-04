package ru.java.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.java.practikum.client.Client;

import java.io.File;

public class StepsOrders extends Client {


    @Step("Send GET request to /api/v1/orders")
    public Response sendGetRequestOrders() {
        Response response =
                getDefaultRequestSpecification()
                        .get("/api/v1/orders");
        return response;
    }

    @Step("Send POST request to /api/v1/orders")
    public Response sendPostRequestOrders(String body) {
        File json = new File(body);
        Response response =
                getDefaultRequestSpecification()
                        .body(json)
                        .post("/api/v1/orders");
        return response;
    }

    @Step("Send POST request to /api/v1/orders/accept/id?courierId=")
    public Response sendPutAcceptOrder(String courierId, String orderId) {
        Response response =
                getDefaultRequestSpecification()
                        .queryParam("courierId", courierId)
                        .put("/api/v1/orders/accept/" + orderId);
        return response;
    }

    @Step("Send POST request to /api/v1/orders/accept/id?courierId=")
    public Response sendPutAcceptOrder(String orderId) {
        Response response =
                getDefaultRequestSpecification()
                        .put("/api/v1/orders/accept/" + orderId);
        return response;
    }

    @Step("Send GET request to /api/v1/orders/track")
    public static Response getOrderTrack(String track) {
        Response response =
                getDefaultRequestSpecification()
                        .when()
                        .queryParam("t", track)
                        .get("/api/v1/orders/track");
        return response;
    }
    @Step("Send POST request to /api/v1/orders/track")
    public Response sendGetOrderTrack(String track) {
        Response response =
                getDefaultRequestSpecification()
                        .queryParam("t", track)
                        .get("/api/v1/orders/track");
        return response;
    }
}
