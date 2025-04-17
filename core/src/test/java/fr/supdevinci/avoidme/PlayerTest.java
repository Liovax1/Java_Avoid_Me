package fr.supdevinci.avoidme;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Tests unitaires pour la classe Player.
 */
public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        OrthographicCamera camera = new OrthographicCamera();
        StretchViewport viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        player = new Player(viewport);
    }

    @Test
    public void testInitialPosition() {
        assertEquals(140, (int) player.getBounds().x);
        assertEquals(210, (int) player.getBounds().y);
    }

    @Test
    public void testReset() {
        player.reset();
        assertEquals(140, (int) player.getBounds().x);
        assertEquals(210, (int) player.getBounds().y);
    }

    @Test
    public void testStayInsideLeftBorder() {
        player.reset();
        player.update(100); // simulate long left movement
        assertTrue(player.getBounds().x >= 0);
    }

    @Test
    public void testStayInsideBottomBorder() {
        player.reset();
        player.update(100); // simulate long down movement
        assertTrue(player.getBounds().y >= 0);
    }
}
