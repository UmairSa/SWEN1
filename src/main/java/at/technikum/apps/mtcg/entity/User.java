package at.technikum.apps.mtcg.entity;

import lombok.Data;

@Data
public class User {
    private int id;
    private String username;
    private String password;
    private int coins;
    private int elo;
}