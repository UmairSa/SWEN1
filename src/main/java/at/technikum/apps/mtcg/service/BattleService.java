package at.technikum.apps.mtcg.service;

import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.repository.BattleRepository;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Random;
@RequiredArgsConstructor
public class BattleService {
    private final BattleRepository battleRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public Battle initiateBattle(String player1username, String player2username) {

        User player1 = userRepository.findByUsername(player1username).orElseThrow(()-> new IllegalArgumentException("Player1 not found"));
        User player2 = userRepository.findByUsername(player2username).orElseThrow(()-> new IllegalArgumentException("Player2 not found"));

        int player1Id = player1.getId();
        int player2Id = player2.getId();

        //get players decks
        List<Card> player1Deck = cardRepository.findDeckByUserId(player1Id);
        List<Card> player2Deck = cardRepository.findDeckByUserId(player2Id);

        // Check if both players have valid decks
        if (player1Deck.isEmpty() || player2Deck.isEmpty()) {
            throw new IllegalArgumentException("One or both players do not have a valid deck");
        }
        // Initialize the battle log and outcome variables
        StringBuilder battleLog = new StringBuilder();
        String battleOutcome;

        // Initialize counters for rounds and tracking wins
        int rounds = 0;
        int player1Wins = 0;
        int player2Wins = 0;

        int winThreshold = 4;

        // Battle logic loop
        while (rounds < 100 && !player1Deck.isEmpty() && !player2Deck.isEmpty()) {
            rounds++;
            String roundResult = conductRound(player1Deck, player2Deck);
            battleLog.append("Round ").append(rounds).append(": ").append(roundResult).append("\n");

            // Update win counters based on the round result
            if (roundResult.contains("Player 1 wins")) {
                player1Wins++;
            } else if (roundResult.contains("Player 2 wins")) {
                player2Wins++;
            }

            //Check if winThreshold reached
            if (player1Wins >= winThreshold || player2Wins >= winThreshold) {
                break;
            }

        }

        // Determine the battle outcome
        if (player1Wins > player2Wins) {
            battleOutcome = "Player 1 wins the battle";
            updateStatsAndCards(player1Id, player2Id);  // Update ELO, wins/losses, and card transfers
        } else if (player2Wins > player1Wins) {
            battleOutcome = "Player 2 wins the battle";
            updateStatsAndCards(player2Id, player1Id);  // Update ELO, wins/losses, and card transfers
        } else {
            battleOutcome = "The battle is a draw"; // Handle draw scenario, e.g., ELO updates
        }

        // Save the battle record
        Battle battle = new Battle(player1Id, player2Id, battleLog.toString(), battleOutcome);
        battleRepository.save(battle);

        return battle;
    }

    private String conductRound(List<Card> player1Deck, List<Card> player2Deck) {
        Random random = new Random();
        Card player1Card = player1Deck.get(random.nextInt(player1Deck.size()));
        Card player2Card = player2Deck.get(random.nextInt(player2Deck.size()));

        String roundResult;
        if (player1Card.getDamage() > player2Card.getDamage()) {
            roundResult = "Player 1 wins the round.";
        // Transfer player2Card to player1Deck
            player1Deck.add(player2Card);
            player2Deck.remove(player2Card);
        } else if (player1Card.getDamage() < player2Card.getDamage()) {
            roundResult = "Player 2 wins the round.";
        // Transfer player1Card to player2Deck
            player2Deck.add(player1Card);
            player1Deck.remove(player1Card);
        } else {
            roundResult = "The round is a draw.";
        // No card transfer in case of a draw
        }
        return "Player 1's " + player1Card.getName() + " vs Player 2's " + player2Card.getName() + ": " + roundResult;
    }


    private void updateStatsAndCards(int winnerId, int loserId) {
        // Update stats for the winner
        User winner = userRepository.findById(winnerId).orElseThrow(() -> new IllegalStateException("Winner not found"));
        winner.setWins(winner.getWins() + 1);
        updateEloRating(winner, true);
        userRepository.update(winner);

        // Update stats for the loser
        User loser = userRepository.findById(loserId).orElseThrow(() -> new IllegalStateException("Loser not found"));
        loser.setLosses(loser.getLosses() + 1);
        updateEloRating(loser, false);
        userRepository.update(loser);

        // Transfer all cards from loser's deck to winner's deck
        List<Card> loserCards = cardRepository.findDeckByUserId(loserId);
        for (Card card : loserCards) {
            card.setOwnerId(winnerId);
            cardRepository.updateCardOwner(card.getCardId(), winnerId);
        }
    }

    private void updateEloRating(User user, boolean isWinner) {
        int eloChange = isWinner ? 3 : -5;
        user.setElo(Math.max(user.getElo() + eloChange, 0)); // Ensure that ELO doesn't drop below 0
    }
}

