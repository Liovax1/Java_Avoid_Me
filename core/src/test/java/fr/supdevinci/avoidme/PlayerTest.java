package fr.supdevinci.avoidme;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.mockito.Mockito;

public class PlayerTest {

    private Player player;

    @Before
    public void setUp() {
        Gdx.input = Mockito.mock(Input.class); // simule les touches clavier

        Viewport viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, new OrthographicCamera());
        player = new Player(viewport, true); // Mode test
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
        player.update(100); // simulate movement
        assertTrue(player.getBounds().x >= 0);
    }

    @Test
    public void testStayInsideBottomBorder() {
        player.update(100); // simulate movement
        assertTrue(player.getBounds().y >= 0);
    }
}
