package at.technikum.apps.mtcg.RepoTests;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CardRepoTest {
    @Mock
    private Database database;
    @Mock
    private ResultSet resultSet;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    private CardRepository cardRepository;
    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException, SQLException {
        MockitoAnnotations.openMocks(this);
        when(database.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1); // Mock successful execution

        cardRepository = new CardRepository();
        Field databaseField = CardRepository.class.getDeclaredField("database");
        databaseField.setAccessible(true);
        databaseField.set(cardRepository, database);
    }
    @Test
    public void testSaveCard() throws SQLException {
        Card card = new Card();
        card.setCardId(UUID.randomUUID());
        card.setName("FireDragon");
        card.setDamage(50.0);
        card.setElementType("Fire");
        card.setCardType("Monster");
        card.setInDeck(false);
        card.setOwnerId(1);
        card.setPackId(10);

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

        Card savedCard = cardRepository.save(card);

        verify(preparedStatement, times(1)).executeUpdate();
        assertNotNull(savedCard);
    }

    @Test
    public void testConfigureDeck() throws SQLException {
        // Arrange
        int userId = 1;
        List<UUID> cardIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        // Act
        cardRepository.configureDeck(userId, cardIds);
        // Assert
        verify(preparedStatement, times(1)).executeUpdate();
    }
    @Test
    public void testFindById() throws SQLException {
        // Arrange
        UUID cardId = UUID.randomUUID();
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true); // Simulate finding the card
        when(resultSet.getString("cardid")).thenReturn(cardId.toString());
        // Act
        Optional<Card> foundCard = cardRepository.findById(cardId);
        // Assert
        assertTrue(foundCard.isPresent());
        assertEquals(cardId, foundCard.get().getCardId());
    }
    @Test
    public void testUpdateCardOwner() throws SQLException {
        UUID cardId = UUID.randomUUID();
        Integer newOwnerId = 2;

        cardRepository.updateCardOwner(cardId, newOwnerId);

        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement).setObject(1, newOwnerId);
        verify(preparedStatement).setObject(2, cardId);
    }
    @Test
    public void testFindByPackId() throws SQLException {
        int packId = 1;
        UUID mockUUID = UUID.randomUUID();
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false); // Simulate two cards found
        when(resultSet.getString("cardid")).thenReturn(mockUUID.toString());
        when(resultSet.getString("name")).thenReturn("FireDragon"); // Mock name

        List<Card> cards = cardRepository.findByPackId(packId);

        assertNotNull(cards);
        assertEquals(2, cards.size());
        verify(preparedStatement).setInt(1, packId);
    }

    @Test
    public void testFindByOwnerId() throws SQLException {
        int ownerId = 1;
        UUID mockUUID = UUID.randomUUID();
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString("cardid")).thenReturn(mockUUID.toString());
        when(resultSet.getString("name")).thenReturn("WaterSprite");

        List<Card> cards = cardRepository.findByOwnerId(ownerId);

        assertNotNull(cards);
        assertEquals(2, cards.size());
        verify(preparedStatement).setInt(1, ownerId);
    }
}