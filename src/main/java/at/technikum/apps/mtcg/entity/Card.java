package at.technikum.apps.mtcg.entity;

import lombok.Data;
import java.util.UUID;
@Data
public class Card {
    private UUID cardId;
    private String name;
    private double damage;
    private String elementType;
    private String cardType;
    private Integer ownerId; // FK to users table
    private UUID packId; // FK to packages table
    private boolean inDeck;
}
