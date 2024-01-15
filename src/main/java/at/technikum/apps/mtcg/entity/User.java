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

    @JsonProperty("Coins")
    private int coins = 20;

    @JsonProperty("Elo")
    private int elo = 100;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Bio")
    private String bio;

    @JsonProperty("Image")
    private String image;

    private int wins;
    private int losses;
}