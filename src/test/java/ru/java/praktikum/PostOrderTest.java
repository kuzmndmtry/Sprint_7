package ru.java.praktikum;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
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


    @Test
    public void checkCreateOrder() {
        Response response =
                sendPostRequestOrders(body);
        compareResponseStatusCode(response,SC_CREATED);
        compareResponseBody(response,"track");
        response.then().statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());

    }

    @Step("Send POST request to /api/v1/orders")
    public Response sendPostRequestOrders(String body) {
        File json = new File(body);
        Response response =
                given()
                        //.log().body()
                        .body(json)
                        .contentType(ContentType.JSON)
                        .post("/api/v1/orders");
        return response;
    }

    @Step("Compare status code to something")
    public void compareResponseStatusCode(Response response, int statusCode) {
        response
                .then()
                .statusCode(statusCode);
    }

    @Step("Compare body to something")
    public void compareResponseBody(Response response, String object, String value) {
        response
                .then()
                .body(object, equalTo(value));
    }

    @Step("Compare body to something")
    public void compareResponseBody(Response response, String object) {
        response
                .then()
                .body(object, notNullValue());
    }
}
