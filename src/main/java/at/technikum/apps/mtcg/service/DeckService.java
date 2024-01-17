package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.CardRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DeckService {
    private final CardRepository cardRepository;

    public List<Card> getDeckByUserId(int userId) {
        return cardRepository.findDeckByUserId(userId);
    }

    public void configureDeck(int userId, List<UUID> cardIds) throws IllegalArgumentException {
        if (cardIds.size() != 4) {
            throw new IllegalArgumentException("The deck must consist of exactly 4 cards.");
        }

        List<Card> userCards = cardRepository.findByOwnerId(userId);
        Set<UUID> userCardIds = userCards.stream().map(Card::getCardId).collect(Collectors.toSet());

        for (UUID cardId : cardIds) {
            if (!userCardIds.contains(cardId)) {
                throw new IllegalArgumentException("User does not own card with ID: " + cardId);
            }
        }
        cardRepository.configureDeck(userId, cardIds);
    }
}
