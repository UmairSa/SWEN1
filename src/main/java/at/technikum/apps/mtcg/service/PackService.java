package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Pack;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class PackService {

    private final PackRepository packRepository;
    private final CardRepository cardRepository;
    public Pack createPack(List<Card> cards) {

        UUID packID = UUID.randomUUID();

        Pack newPack = new Pack();
        newPack.setPackId(packID);
        newPack.setPrice(5.0);
        packRepository.save(newPack);

        for (Card card : cards) {
            card.setPackId(packID);
            cardRepository.save(card);
        }
        return newPack;
    }
}
