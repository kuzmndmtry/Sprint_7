package ru.java.praktikum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

//Принять заказ
public class PutOrdersAcceptTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("Сheck successful order accept")
    public void checkSuccessfulOrderAccept() {


    }
}
