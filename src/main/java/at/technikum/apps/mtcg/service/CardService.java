package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.repository.CardRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    public List<Card> getCardsByUserId(int userId) {
        return cardRepository.findByOwnerId(userId);
    }
}
