package ru.java.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.java.practikum.client.Client;
import java.io.File;

public class StepsCourier extends Client {
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCourier(String jsonPath) {
        File json = new File(jsonPath);
        Response response =
                getDefaultRequestSpecification()
                        .body(json)
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(String jsonPath) {
        File json = new File(jsonPath);
        Response response =
                getDefaultRequestSpecification()
                        .body(json)
                        .post("/api/v1/courier/login");
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(String login, String Password) {
        Courier json = new Courier(login, Password);
        Response response =
                getDefaultRequestSpecification()
                        .body(json)
                        .post("/api/v1/courier/login");
        return response;
    }
    @Step("Send POST request to /api/v1/courier/login")
    public Response sendDeleteRequestCourier(String id) {
        Response response =
                getDefaultRequestSpecification()
                        .delete("/api/v1/courier/" + id);
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendDeleteRequestCourier() {
        Response response =
                getDefaultRequestSpecification()
                        .delete("/api/v1/courier/");
        return response;
    }
}
