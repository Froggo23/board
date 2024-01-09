package com.choe.board;

import lombok.Data;

import java.util.Date;
@Data
public class User {
    int id;
    String username;
    String password;
    String email;
    String phone;

    Date createdAt;



}