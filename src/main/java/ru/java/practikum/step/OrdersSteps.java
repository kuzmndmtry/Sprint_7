package ru.java.practikum.step;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import ru.java.practikum.client.Client;

import java.io.File;

public class OrdersSteps extends Client {
    public static Response getOrders() {
        return getDefaultRequestSpecification()
                .when()
                .get("/api/v1/orders");
    }

    public static Response postOrder(String body) {
        File json = new File(body);
        return getDefaultRequestSpecification()
                .when()
                .body(json)
                .post("/api/v1/orders");
    }

    public static Response getOrder(String track) {
        return getDefaultRequestSpecification()
                .when()
                .queryParam("t", track)
                .get("/api/v1/orders/track");
    }
}
