package br.com.lojacell.api.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    private String name;
    private String password;

}