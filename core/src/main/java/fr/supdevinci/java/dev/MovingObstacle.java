package fr.supdevinci.java.dev;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class MovingObstacle {
    private Rectangle bounds;
    private Vector2 velocity;

    public MovingObstacle(float width, float height, float speed) {
        // Position de départ aléatoire sur un bord
        float x = 0, y = 0;
        int edge = MathUtils.random(3); // 0: haut, 1: bas, 2: gauche, 3: droite

        switch (edge) {
            case 0: // top
                x = MathUtils.random(800); y = 600;
                break;
            case 1: // bottom
                x = MathUtils.random(800); y = 0 - height;
                break;
            case 2: // left
                x = 0 - width; y = MathUtils.random(600);
                break;
            case 3: // right
                x = 800; y = MathUtils.random(600);
                break;
        }

        bounds = new Rectangle(x, y, width, height);

        // Vecteur direction vers centre + vitesse
        Vector2 direction = new Vector2(400 - x, 300 - y).nor(); // vers le centre
        velocity = direction.scl(speed);
    }

    public void update(float delta) {
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
