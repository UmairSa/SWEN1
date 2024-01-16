package at.technikum.apps.mtcg.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Scoreboard {
    private String username;
    private int elo;
}
