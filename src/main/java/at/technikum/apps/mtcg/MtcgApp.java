package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.*;
import at.technikum.server.ServerApplication;
import at.technikum.server.http.HttpContentType;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import lombok.Data;
import java.util.Arrays;
import java.util.List;

@Data
public class MtcgApp implements ServerApplication {
    private final List<Controller> controllers;
    public MtcgApp() {

        controllers = Arrays.asList(
                Injector.getUserController(),
                Injector.getCardController(),
                Injector.getDeckController(),
                Injector.getPackController(),
                Injector.getScoreboardController(),
                Injector.getStatsController(),
                Injector.getTradeController(),
                Injector.getTransactionController(),
                Injector.getBattleController());
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
