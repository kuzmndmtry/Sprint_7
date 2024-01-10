package ru.java.practikum.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CourierLogin {
    private String login;
    private String password;

    public CourierLogin(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
