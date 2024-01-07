package at.technikum.apps.mtcg;

import at.technikum.apps.mtcg.controller.Controller;
import at.technikum.apps.mtcg.controller.UserController;
import at.technikum.apps.mtcg.repository.UserRepositoryImpl;
import at.technikum.apps.mtcg.service.UserService;
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
       controllers.add(new UserController(new UserService(new UserRepositoryImpl())));
    }

    @Override
    public Response handle(Request request) {

        for (Controller controller: controllers) {
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
