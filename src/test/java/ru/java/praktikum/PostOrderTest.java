package ru.java.praktikum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class PostOrderTest {
    private final String body;

    public PostOrderTest(String body) {
        this.body = body;
    }

    @Parameterized.Parameters // добавили аннотацию
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
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }


    @Test
    public void checkCreateOrder() {
        File json = new File(body);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/orders");
        response.then().statusCode(201)
                .and()
                .assertThat().body("track", notNullValue());

    }
}
