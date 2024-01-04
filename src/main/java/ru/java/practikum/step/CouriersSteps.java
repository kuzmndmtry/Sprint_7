package ru.java.practikum.step;

import io.restassured.response.Response;
import ru.java.practikum.client.Client;

import java.io.File;

public class CouriersSteps extends Client {

    public static Response create() {
        File json = new File("src/test/resources/CourierDataCreate.json");
        return getDefaultRequestSpecification()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }

    public static Response validLogin() {
        File json = new File("src/test/resources/CourierDataLogin.json");
        return getDefaultRequestSpecification()
                .body(json)
                .when()
                .post("/api/v1/courier/login");

    }

public static Response delete(String id) {
    return getDefaultRequestSpecification()
            .delete("/api/v1/courier/" + id);
}
public static Response deleteWithoutId() {
    return getDefaultRequestSpecification()
            .delete("/api/v1/courier/" );
}
}
