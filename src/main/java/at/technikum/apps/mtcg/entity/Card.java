package at.technikum.apps.mtcg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.UUID;
@Data
public class Card {
    @JsonProperty("Id")
    private UUID cardId;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Damage")
    private double damage;
    @JsonProperty("ElementType")
    private String elementType;
    @JsonProperty("CardType")
    private String cardType;
    private boolean inDeck;
    private Integer ownerId; // FK to users table
    private int packId; // FK to packages table
}
