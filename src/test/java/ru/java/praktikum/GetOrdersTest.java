package ru.java.praktikum;

import io.restassured.response.Response;
import org.junit.Test;
import ru.java.practikum.steps.StepsOrders;
import ru.java.practikum.steps.StepsTest;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrdersTest {

    StepsTest stepsTes = new StepsTest();
    StepsOrders stepsOrders = new StepsOrders();
    @Test
    public void checkGetOrdersListWithoutParams() {
        Response response =
                stepsOrders.sendGetRequestOrders();
        stepsTes.compareResponseStatusCode(response,SC_OK);
        stepsTes.compareResponseBody(response,"orders");
    }

}
