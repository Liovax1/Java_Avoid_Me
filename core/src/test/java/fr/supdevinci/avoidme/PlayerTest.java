package fr.supdevinci.avoidme;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.supdevinci.avoidme.Constants;
import fr.supdevinci.avoidme.Player;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import org.mockito.Mockito;

public class PlayerTest {

    private Player player;

    @Before // verifie pos init, deplac, et reset
    public void setUp() {
        Gdx.input = Mockito.mock(Input.class); // simule les touches clavier

        Viewport viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT,
                new OrthographicCamera());
        player = new Player(viewport, true); // Mode test
    }

    @Test
    public void testInitialPosition() {
        assertEquals(368, (int) player.getBounds().x);
        assertEquals(268, (int) player.getBounds().y);
    }

    @Test
    public void testReset() {
        player.reset();
        assertEquals(368, (int) player.getBounds().x);
        assertEquals(268, (int) player.getBounds().y);
    }

    @Test
    public void testStayInsideLeftBorder() {
        player.update(100); // simuler le mouvement
        assertTrue(player.getBounds().x >= 0);
    }

    @Test
    public void testStayInsideBottomBorder() {
        player.update(100);
        assertTrue(player.getBounds().y >= 0);
    }
}
