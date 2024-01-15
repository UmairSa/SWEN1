package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.apps.mtcg.repository.CardRepository;
import at.technikum.apps.mtcg.repository.PackRepository;
import at.technikum.apps.mtcg.repository.UserRepository;
import at.technikum.apps.mtcg.service.*;
import at.technikum.server.ServerApplication;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MtcgApp implements ServerApplication {

    private List<Controller> controllers = new ArrayList<>();

    public MtcgApp() {
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        controllers.add(new UserController(userService, userRepository));

        PackRepository packRepository = new PackRepository();
        CardRepository cardRepository = new CardRepository();
        PackService packService = new PackService(packRepository, cardRepository);
        controllers.add(new PackController(packService));

        TransactionService transactionService = new TransactionService(userRepository, packRepository, cardRepository);
        controllers.add(new TransactionController(transactionService));

        CardService cardService = new CardService(cardRepository);
        controllers.add(new CardController(cardService, userService));

        DeckService deckService = new DeckService(new CardRepository());
        controllers.add(new DeckController(deckService, userService));

        controllers.add(new StatsController(userService));
    }

    @Override
    public Response handle(Request request) {

        for (Controller controller : controllers) {
            if (!controller.supports(request.getRoute())) {
                continue;
            }

            return controller.handle(request);
        }

        Response response = new Response();
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setContentType(HttpContentType.TEXT_PLAIN);
        response.setBody("Route " + request.getRoute() + " not found in app!");

        return response;
    }
}
