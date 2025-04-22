package fr.supdevinci.avoidme;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Rectangle;

public class MovingObstacleTest {

    private MovingObstacle movingObstacle;

    @Before
    public void setUp() {
        // Init de MovingObstacle en mode test
        movingObstacle = new MovingObstacle(Constants.OBSTACLE_WIDTH, Constants.OBSTACLE_HEIGHT, Constants.OBSTACLE_SPEED, true);
    }

    @Test
    public void testInitialBounds() {
        Rectangle bounds = movingObstacle.getBounds();
        assertNotNull(bounds);
        assertTrue(bounds.width == Constants.OBSTACLE_WIDTH);
        assertTrue(bounds.height == Constants.OBSTACLE_HEIGHT);
    }

    @Test
    public void testUpdatePosition() {
        Rectangle initialBounds = new Rectangle(movingObstacle.getBounds());
        movingObstacle.update(1f);
        Rectangle updatedBounds = movingObstacle.getBounds();

        assertNotEquals(initialBounds.x, updatedBounds.x);
        assertNotEquals(initialBounds.y, updatedBounds.y);
    }

    @Test
    public void testBoundsStayWithinWorld() {
        movingObstacle.update(100f); // Simule un déplacement important
        Rectangle bounds = movingObstacle.getBounds();
        //limites du jeu
        assertTrue(bounds.x >= -Constants.OBSTACLE_WIDTH);
        assertTrue(bounds.x <= Constants.WORLD_WIDTH + Constants.OBSTACLE_WIDTH);
        assertTrue(bounds.y >= -Constants.OBSTACLE_HEIGHT);
        assertTrue(bounds.y <= Constants.WORLD_HEIGHT + Constants.OBSTACLE_HEIGHT);
    }

    @Test
    public void testDispose() {
        try {
            movingObstacle.dispose();
        } catch (Exception e) {
            fail("Dispose method threw an exception: " + e.getMessage());
        }
    }
}