package ru.java.practikum.client;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.java.practikum.config.Config.BASE_URI;

public abstract class RestClient {
    protected static RequestSpecification getDefaultRequestSpecification() {
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON);
    }
}
