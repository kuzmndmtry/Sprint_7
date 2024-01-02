package ru.java.practikum.client;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.java.practikum.config.Config.BASE_URI;

public abstract class Client {
    public RequestSpecification getDefaultRequestSpecification() {
        return given()
                .baseUri(BASE_URI)
                .header("Content-type", "application/json");
    }
}
