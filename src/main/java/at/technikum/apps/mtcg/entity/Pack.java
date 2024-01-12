package at.technikum.apps.mtcg.entity;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Pack {
    private UUID packId;
    private double price = 5;
    private Integer userId;
    private List<Card> cards;
}
