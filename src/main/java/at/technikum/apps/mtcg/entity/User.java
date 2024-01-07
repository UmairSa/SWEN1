package at.technikum.apps.mtcg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class User {
    private int id;
    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;
    private int coins;
    private int elo;
}