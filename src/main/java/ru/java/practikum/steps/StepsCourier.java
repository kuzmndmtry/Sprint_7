package ru.java.practikum.steps;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.java.practikum.client.RestClient;
import ru.java.practikum.dto.Courier;
import ru.java.practikum.dto.CourierLogin;

public class StepsCourier extends RestClient {
    private static final String COURIER = "/api/v1/courier/";
    private static final String COURIER_LOGIN = "/api/v1/courier/login";
    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCourier(Courier courier) {
        Response response =
                getDefaultRequestSpecification()
                        .body(courier)
                        .post(COURIER);
        return response;
    }
    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(CourierLogin courierLogin) {
        Response response =
                getDefaultRequestSpecification()
                        .body(courierLogin)
                        .post(COURIER_LOGIN);
        return response;
    }
    @Step("Send POST request to /api/v1/courier/login")
    public Response sendDeleteRequestCourier(String courierId) {
        Response response =
                getDefaultRequestSpecification()
                        .delete(COURIER + courierId);
        return response;
    }
}
