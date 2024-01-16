package at.technikum.apps.mtcg.entity;

import lombok.*;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@RequiredArgsConstructor

public class Battle {
    private int battleId;
    @NonNull
    private int player1Id;
    @NonNull
    private int player2Id;
    @NonNull
    private String battleLog;
    @NonNull
    private String battleOutcome;
    private Timestamp createdAt;
}
