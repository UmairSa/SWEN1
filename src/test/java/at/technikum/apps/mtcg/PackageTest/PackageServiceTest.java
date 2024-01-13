package at.technikum.apps.mtcg.PackageTest;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Pack;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackRepository;
import at.technikum.apps.mtcg.service.PackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PackageServiceTest {

    @Mock
    private PackRepository packRepository;
    @Mock
    private CardRepository cardRepository;

    private PackService packageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        packageService = new PackService(packRepository, cardRepository);
    }

    @Test
    public void createPackageTest() {
        // Create mock cards
        Card card1 = new Card();
        card1.setCardId(UUID.randomUUID());
        card1.setName("FireDragon");
        card1.setDamage(50);
        card1.setElementType("Fire");
        card1.setCardType("Monster");

        Card card2 = new Card();
        card2.setCardId(UUID.randomUUID());
        card2.setName("WaterSpell");
        card2.setDamage(30);
        card2.setElementType("Water");
        card2.setCardType("Spell");

        List<Card> cards = Arrays.asList(card1, card2);

        // Call createPackage method
        Pack createdPackage = packageService.createPack(cards);

        // Assertions
        assertNotNull(createdPackage, "Created package should not be null");
        assertNotNull(createdPackage.getPackId(), "Package ID should not be null");
        assertEquals(5.0, createdPackage.getPrice(), "Package price should be 5.0");

        // Verify interactions with repositories
        verify(packRepository, times(1)).save(any(Pack.class));
        verify(cardRepository, times(cards.size())).save(any(Card.class));
    }
}