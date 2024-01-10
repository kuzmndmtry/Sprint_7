package ru.java.praktikum;

import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.java.practikum.steps.StepsOrders;
import ru.java.practikum.steps.StepsTest;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.hamcrest.Matchers.notNullValue;

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
