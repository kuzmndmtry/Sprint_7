package ru.java.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.java.practikum.client.CourierClient;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static ru.java.practikum.config.Config.BASE_URI;

// Создание курьера
public class PostCourierTestClient {
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Сheck courier creation")
    @Description("Сhecking the creation of a new courier with a full set of fields")
    public void checkCourierCreationWithAFullSetOfFields() {
        Boolean response =
                courierClient.create("src/test/resources/CourierTest.json")
                        .then()
                        .statusCode(201)
                        .extract()
                        .path("ok");
        Assert.assertEquals(true, response);
    }

    @Test
    @DisplayName("Сheck the creation of a duplicate courier")
    public void checkDuplicateCourierCreation() {
        File json = new File("src/test/resources/CourierTest.json");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().statusCode(409)
                .and()
                .assertThat().body("message", equalTo("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Сheck courier creation without login field ")
    public void checkCourierCreationWithoutLogin() {
        Courier json = new Courier();
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Сheck courier creation without password field ")
    public void checkCourierCreationWithoutPassword() {
        Courier json = new Courier();
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteTestCourier() {
        File json = new File("src/test/resources/CourierTest.json");
        CourierId courierId =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .post("/api/v1/courier/login")
                        .body()
                        .as(CourierId.class);
        String id = courierId.getId();
        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/" + id);
    }

}

