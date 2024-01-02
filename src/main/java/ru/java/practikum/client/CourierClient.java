package ru.java.practikum.client;

import io.restassured.response.Response;

import java.io.File;

public class CourierClient extends Client {
    public Response create(String filePath) {
        File json = new File(filePath);
        return getDefaultRequestSpecification()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }
}
