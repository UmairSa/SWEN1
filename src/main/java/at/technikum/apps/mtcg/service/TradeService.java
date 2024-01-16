package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Trade;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class TradeService {
    private final TradeRepository tradeRepository;
    private final CardRepository cardRepository;

    public void createTrade(Trade trade) {
        if (cardRepository.isCardInDeck(trade.getCardToTradeId())) {
            throw new IllegalStateException("Card is currently in a deck and cannot be traded.");
        }
        tradeRepository.save(trade);
    }
    public List<Trade> getAllTrades() {
        return tradeRepository.findAllTrades();
    }
    public void deleteTrade(UUID tradeId, int userId) {
        //user requesting  delete is the one who created trade?
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(() -> new IllegalArgumentException("Trade not found."));
        if (trade.getUser1Id() != userId) {
            throw new IllegalStateException("User is not authorized to delete this trade.");
        }
        tradeRepository.deleteById(tradeId);
    }

    public void acceptTrade(UUID tradeId, UUID cardIdOffered, int acceptingUserId) {
        Trade trade = tradeRepository.findById(tradeId).orElseThrow(() -> new IllegalArgumentException("Trade not found."));

        if (trade.getUser1Id() == acceptingUserId) {
            throw new IllegalStateException("Cannot trade with yourself.");
        }
        Card offeredCard = cardRepository.findById(cardIdOffered).orElseThrow(() -> new IllegalArgumentException("Card not found."));

        if (offeredCard.getOwnerId() == trade.getUser1Id()) {
            throw new IllegalStateException("Cannot accept a trade with your own card.");
        }
        if (!offeredCard.getCardType().equals(trade.getCardType()) || offeredCard.getDamage() < trade.getMinimumDamage()) {
            throw new IllegalStateException("Offered card does not meet trade requirements.");
        }
        // Update the card ownership
        cardRepository.updateCardOwner(offeredCard.getCardId(), trade.getUser1Id()); // Offered card to trade creator
        cardRepository.updateCardOwner(trade.getCardToTradeId(), acceptingUserId); // Traded card to accepting user

        // Remove the trade from available trades
        tradeRepository.deleteById(tradeId);
    }
}
