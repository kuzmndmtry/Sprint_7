package ru.java.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

//Удалить курьера
public class DeleteCourierTestClient {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Сheck successful delete of courier")
    public void checkCourierDelete() {
        File json = new File("src/test/resources/CourierTest.json");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
        CourierId courierId =
                given()
                        .header("Content-type", "application/json")
                        .body(json)
                        .post("/api/v1/courier/login")
                        .body()
                        .as(CourierId.class);
        String id = courierId.getId();
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .delete("/api/v1/courier/" + id);
        response.then().statusCode(200)
                .and()
                .assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Сheck delete courier without login")
    public void checkCourierDeleteWithoutLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .delete("/api/v1/courier/");
        response.then().statusCode(400)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для удаления курьера"));

    }

    @Test
    @DisplayName("Сheck delete courier with non-existent login")
    public void checkCourierDeleteWithNonExistentLogin() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .delete("/api/v1/courier/0000");
        response.then().statusCode(404)
                .and()
                .assertThat().body("message", equalTo("Курьера с таким id нет"));

    }




}
