package at.technikum.apps.mtcg.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ScoreboardEntry {
    private String username;
    private int elo;
}
