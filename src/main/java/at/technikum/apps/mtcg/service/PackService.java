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
    public Pack createPack(Pack pack) {

        packRepository.save(pack);

        for (Card card : pack.getCards()) {
            card.setPackId(pack.getPackId());
            cardRepository.save(card);
        }
        return pack;
    }
}