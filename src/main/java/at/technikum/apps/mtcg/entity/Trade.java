package at.technikum.apps.mtcg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.UUID;

@Data
public class Trade {
    @JsonProperty("Id")
    private UUID tradeId;
    @JsonProperty("CardToTrade")
    private UUID cardToTradeId;
    @JsonProperty("Type")
    private String cardType;
    @JsonProperty("MinimumDamage")
    private double minimumDamage;
    private int user1Id;
    private Integer user2Id; // This could be null if the trade is not yet accepted
}
