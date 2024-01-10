package ru.java.practikum.client;

import io.restassured.response.Response;
import ru.java.practikum.dto.Courier;
import ru.java.practikum.dto.CourierId;
import ru.java.practikum.dto.CourierLogin;
import ru.java.practikum.steps.StepsCourier;

public class CourierClient {
    private final StepsCourier stepsCourier;


    public CourierClient(StepsCourier stepsCourier) {
        this.stepsCourier = stepsCourier;
    }
    public Response create(String login, String password, String firstName) {
        Courier courierRequest = new Courier();
        courierRequest.setLogin(login);
        courierRequest.setPassword(password);
        courierRequest.setFirstName(firstName);
        return stepsCourier.sendPostRequestCourier(courierRequest);
    }

    public Response login(String login, String password) {
        CourierLogin courierLoginRequest  = new CourierLogin();
        courierLoginRequest.setLogin(login);
        courierLoginRequest.setPassword(password);
        return stepsCourier.sendPostRequestCourierLogin(courierLoginRequest);
    }
    public Response delete(String id) {
        CourierId courierIdRequest  = new CourierId();
        courierIdRequest.setId(id);
        return stepsCourier.sendDeleteRequestCourier(courierIdRequest.getId());
    }

}
