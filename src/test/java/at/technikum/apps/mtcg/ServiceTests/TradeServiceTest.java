package at.technikum.apps.mtcg.ServiceTests;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.TradeRepository;
import at.technikum.apps.mtcg.service.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;
    @Mock
    private CardRepository cardRepository;

    private TradeService tradeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tradeService = new TradeService(tradeRepository, cardRepository);
    }
    @Test
    public void testCreateTrade() {
        // Arrange
        UUID cardId = UUID.randomUUID();
        Trade trade = new Trade();
        trade.setCardToTradeId(cardId);
        when(cardRepository.isCardInDeck(cardId)).thenReturn(false); // Assuming the card is not in a deck
        // Act
        tradeService.createTrade(trade);
        // Assert
        verify(cardRepository).isCardInDeck(cardId);
        verify(tradeRepository).save(trade);
    }
}
