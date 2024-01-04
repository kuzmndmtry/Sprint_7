package ru.java.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;
import static ru.java.practikum.config.Config.BASE_URI;

@RunWith(Parameterized.class)
public class PostOrderTest {
    private final String body;

    public PostOrderTest(String body) {
        this.body = body;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][] {
                {"src/test/resources/OrderWithColorBlack.json"},
                {"src/test/resources/OrderWithColorGrey.json"},
                {"src/test/resources/OrderWithAllColor.json"},
                {"src/test/resources/OrderWithoutColor.json"},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }
    StepsTest stepsTest = new StepsTest();
    StepsOrders stepsOrders = new StepsOrders();
    @Test
    public void checkCreateOrder() {
        Response response =
                stepsOrders.sendPostRequestOrders(body);
        stepsTest.compareResponseStatusCode(response,SC_CREATED);
        stepsTest.compareResponseBody(response,"track");
    }
}
