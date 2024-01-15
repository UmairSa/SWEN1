package at.technikum.apps.mtcg.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserStats {
    private int wins;
    private int losses;
    private int gamesPlayed;
    private double winPercentage;
    private int elo;
}
