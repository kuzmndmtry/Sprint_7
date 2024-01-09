package ru.java.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.java.practikum.client.Client;
import java.io.File;

public class StepsCourier extends Client {
    private static final String COURIER = "/api/v1/courier/";
    private static final String COURIER_LOGIN = "/api/v1/courier/login";
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCourier(String jsonPath) {
        File json = new File(jsonPath);
        Response response =
                getDefaultRequestSpecification()
                        .body(json)
                        .post(COURIER);
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(String jsonPath) {
        File json = new File(jsonPath);
        Response response =
                getDefaultRequestSpecification()
                        .body(json)
                        .post(COURIER_LOGIN);
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(String login, String Password) {
        Courier json = new Courier(login, Password);
        Response response =
                getDefaultRequestSpecification()
                        .body(json)
                        .post(COURIER_LOGIN);
        return response;
    }
    @Step("Send POST request to /api/v1/courier/login")
    public Response sendDeleteRequestCourier(String id) {
        Response response =
                getDefaultRequestSpecification()
                        .delete(COURIER + id);
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendDeleteRequestCourier() {
        Response response =
                getDefaultRequestSpecification()
                        .delete(COURIER);
        return response;
    }
}
