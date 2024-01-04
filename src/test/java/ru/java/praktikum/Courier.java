package ru.java.praktikum;

import io.restassured.internal.RequestSpecificationImpl;

public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Courier() {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
