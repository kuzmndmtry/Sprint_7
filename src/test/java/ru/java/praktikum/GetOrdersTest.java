package ru.java.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.notNullValue;
import static ru.java.practikum.config.Config.BASE_URI;

public class GetOrdersTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }
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
