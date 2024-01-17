package at.technikum.apps.mtcg.ControllerTests;

import at.technikum.apps.mtcg.controller.BattleController;
import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.mtcg.service.BattleService;
import at.technikum.server.http.HttpStatus;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BattleControllerTest {

    @Mock
    private BattleService battleService;

    private BattleController battleController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        battleController = new BattleController(battleService);
    }

    @Test

    public void testHandlePostRequestForNewBattle() {
        // Arrange
        Request mockRequest = createMockRequest("POST", "/battles", "Bearer player1-mtcgToken");
        when(battleService.initiateBattle(anyString(), anyString())).thenReturn(new Battle());
        // Act
        Response response = battleController.handle(mockRequest);

        // Assert
        assertEquals(HttpStatus.OK.getCode(), response.getStatusCode()); // Corrected from getStatus to getStatusCode
        assertNotNull(response.getBody());
    }

    @Test
    public void testHandleBadRequest() {
        // Arrange
        Request mockRequest = createMockRequest("GET", "/battles", "Bearer player1-mtcgToken");

        // Act
        Response response = battleController.handle(mockRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST.getCode(), response.getStatusCode()); // Corrected from getStatus to getStatusCode
    }


    private Request createMockRequest(String method, String route, String authorization) {
        Request request = mock(Request.class);
        when(request.getMethod()).thenReturn(method);
        when(request.getRoute()).thenReturn(route);
        when(request.getAuthorization()).thenReturn(authorization);
        return request;
    }
}