package at.technikum.apps.mtcg.PackageTest;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.CardRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CardRepoTest {
    @Test
    void save_CreateCard() {
        CardRepository cardRepository = new CardRepository();
        Card newCard = new Card();
        newCard.setCardId(UUID.randomUUID());
        newCard.setName("FireDragon");
        newCard.setDamage(50);
        newCard.setElementType("Fire");
        newCard.setCardType("monster"); // Ensure this matches the allowed value in the check constraint

        Card savedCard = cardRepository.save(newCard);
        assertNotNull(savedCard, "Card should not be null");
        System.out.println("Card created: " + savedCard);
    }
}
