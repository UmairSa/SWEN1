package at.technikum.apps.mtcg.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Pack {
    @JsonProperty("PackageId")
    private int packId;
    private int price = 5;
    private List<Card> cards;
}
