package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.apps.mtcg.repository.*;
import at.technikum.apps.mtcg.service.*;

public class Injector {
    public static final UserRepository userRepository = new UserRepository();
    private static final CardRepository cardRepository = new CardRepository();
    private static final PackRepository packRepository = new PackRepository();
    private static final TradeRepository tradeRepository = new TradeRepository();
    private static final BattleRepository battleRepository = new BattleRepository();

    public static UserService getUserService() {
        return new UserService(userRepository);
    };
    public static CardService getCardService() {
        return new CardService(cardRepository);
    };
    public static PackService getPackService() {
        return new PackService(packRepository, cardRepository);
    };
    public static TradeService getTradeService() {
        return new TradeService(tradeRepository, cardRepository);
    };
    public static BattleService getBattleService() {
        return new BattleService(battleRepository, userRepository, cardRepository);
    };
    public static DeckService getPeckService() {
        return new DeckService(cardRepository);
    };
    public static TransactionService getTransactionService() {
        return new TransactionService(userRepository, packRepository, cardRepository);
    };

    public static UserController getUserController() {
        return new UserController(getUserService(), userRepository);
    };
    public static CardController getCardController() {
        return new CardController(getCardService(), getUserService());
    };
    public static PackController getPackController() {
        return new PackController(getPackService());
    };
    public static TradeController getTradeController() {
        return new TradeController(getTradeService(), getUserService());
    };
    public static BattleController getBattleController() {
        return new BattleController(getBattleService());
    };
    public static DeckController getDeckController() {
        return new DeckController(getPeckService(), getUserService());
    };
    public static TransactionController getTransactionController() {
        return new TransactionController(getTransactionService());
    };
    public static ScoreboardController getScoreboardController() {
        return new ScoreboardController(getUserService());
    };
    public static StatsController getStatsController() {
        return new StatsController(getUserService());
    };
}