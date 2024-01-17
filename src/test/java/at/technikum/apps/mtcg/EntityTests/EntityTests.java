package at.technikum.apps.mtcg.EntityTests;

import at.technikum.apps.mtcg.entity.*;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class EntityTests {
    @Test
    public void testCardInitialization() {
        UUID cardId = UUID.randomUUID();
        Card card = new Card();
        card.setCardId(cardId);
        card.setName("FireDragon");
        card.setDamage(50.0);
        card.setElementType("Fire");
        card.setCardType("Monster");
        card.setInDeck(false);
        card.setOwnerId(1);
        card.setPackId(10);

        assertEquals(cardId, card.getCardId());
        assertEquals("FireDragon", card.getName());
        assertEquals(50.0, card.getDamage());
        assertEquals("Fire", card.getElementType());
        assertEquals("Monster", card.getCardType());
        assertFalse(card.isInDeck());
        assertEquals(1, card.getOwnerId());
        assertEquals(10, card.getPackId());
    }
    @Test
    public void testPackInitialization() {
        Pack pack = new Pack();
        pack.setPackId(1);
        pack.setPrice(5);
        List<Card> cards = new ArrayList<>();
        pack.setCards(cards);

        assertEquals(1, pack.getPackId());
        assertEquals(5, pack.getPrice());
        assertEquals(cards, pack.getCards());
    }
    @Test
    public void testUserInitialization() {
        User user = new User();
        user.setId(1);
        user.setUsername("player1");
        user.setPassword("pass123");
        user.setCoins(20);
        user.setElo(100);
        user.setName("Player One");
        user.setBio("A formidable player.");
        user.setImage("image_url");
        user.setWins(5);
        user.setLosses(3);

        assertEquals(1, user.getId());
        assertEquals("player1", user.getUsername());
        assertEquals("pass123", user.getPassword());
        assertEquals(20, user.getCoins());
        assertEquals(100, user.getElo());
        assertEquals("Player One", user.getName());
        assertEquals("A formidable player.", user.getBio());
        assertEquals("image_url", user.getImage());
        assertEquals(5, user.getWins());
        assertEquals(3, user.getLosses());
    }
    @Test
    public void testUserStatsInitialization() {
        UserStats userStats = new UserStats(5, 2, 7, 71.43, 1000);

        assertEquals(5, userStats.getWins());
        assertEquals(2, userStats.getLosses());
        assertEquals(7, userStats.getGamesPlayed());
        assertEquals(71.43, userStats.getWinPercentage());
        assertEquals(1000, userStats.getElo());
    }
    @Test
    public void testBattleInitialization() {
        Battle battle = new Battle();
        battle.setBattleId(1);
        battle.setPlayer1Id(10);
        battle.setPlayer2Id(20);
        battle.setBattleLog("Sample log");
        battle.setBattleOutcome("Player 1 wins");
        battle.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        assertEquals(1, battle.getBattleId());
        assertEquals(10, battle.getPlayer1Id());
        assertEquals(20, battle.getPlayer2Id());
        assertEquals("Sample log", battle.getBattleLog());
        assertEquals("Player 1 wins", battle.getBattleOutcome());
        assertNotNull(battle.getCreatedAt());
    }
    @Test
    public void testScoreboardInitialization() {
        Scoreboard scoreboard = new Scoreboard("player1", 1200);

        assertEquals("player1", scoreboard.getUsername());
        assertEquals(1200, scoreboard.getElo());
    }
    @Test
    public void testTradeInitialization() {
        UUID tradeId = UUID.randomUUID();
        UUID cardToTradeId = UUID.randomUUID();
        Trade trade = new Trade();
        trade.setTradeId(tradeId);
        trade.setCardToTradeId(cardToTradeId);
        trade.setCardType("Monster");
        trade.setMinimumDamage(30.0);
        trade.setUser1Id(1);
        trade.setUser2Id(2);

        assertEquals(tradeId, trade.getTradeId());
        assertEquals(cardToTradeId, trade.getCardToTradeId());
        assertEquals("Monster", trade.getCardType());
        assertEquals(30.0, trade.getMinimumDamage());
        assertEquals(1, trade.getUser1Id());
        assertEquals(2, trade.getUser2Id());
    }
}
