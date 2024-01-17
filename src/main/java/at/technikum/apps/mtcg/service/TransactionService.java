package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.Pack;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final PackRepository packRepository;
    private final CardRepository cardRepository;

    public boolean acquirePack(String username, int packId) {
        User user = userRepository.findByUsername(username).orElse(null);
        Pack pack = packRepository.findById(packId);

        if (user == null || pack == null || user.getCoins() < pack.getPrice() || packRepository.isPackageAcquired(packId)) {
            return false;
        }

        user.setCoins(user.getCoins() - pack.getPrice());
        userRepository.update(user);

        // Fetch and update owner of each card in the package
        List<Card> cards = cardRepository.findByPackId(pack.getPackId());
        for (Card card : cards) {
            card.setOwnerId(user.getId());
            card.setInDeck(false);
            cardRepository.updateCardOwner(card.getCardId(), user.getId());
        }
        return true;
    }
}