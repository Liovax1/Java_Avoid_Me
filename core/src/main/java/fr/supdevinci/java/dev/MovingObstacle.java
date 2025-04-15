package fr.supdevinci.java.dev;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;


public class MovingObstacle {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private Rectangle bounds;
    private Vector2 velocity;

    public MovingObstacle(float width, float height, float speed) {
        // Sélectionner une image aléatoire parmi les 5 créatures
        String[] creatureFiles = { "creature.png", "creature-2.png", "creature-3.png", "creature-4.png",
                "creature-5.png" };
        String selectedCreature = creatureFiles[MathUtils.random(creatureFiles.length - 1)];

        // Charger le sprite sheet de la créature sélectionnée
        Texture spriteSheet = new Texture(selectedCreature);

        // Découper les frames (4 frames par sprite sheet)
        int frameWidth = spriteSheet.getWidth() / 4; // 4 frames horizontales
        int frameHeight = spriteSheet.getHeight(); // 1 seule ligne
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[4];
        for (int i = 0; i < 4; i++) {
            frames[i] = tmp[0][i];
        }

        // Créer l'animation
        animation = new Animation<>(0.1f, frames);
        stateTime = 0f;

        // Position de départ aléatoire sur un bord
        float x = 0, y = 0;
        int edge = MathUtils.random(3); // 0: haut, 1: bas, 2: gauche, 3: droite

        switch (edge) {
            case 0: // top
                x = MathUtils.random(800);
                y = 600;
                break;
            case 1: // bottom
                x = MathUtils.random(800);
                y = 0 - height;
                break;
            case 2: // left
                x = 0 - width;
                y = MathUtils.random(600);
                break;
            case 3: // right
                x = 800;
                y = MathUtils.random(600);
                break;
        }

        bounds = new Rectangle(x, y, width, height);

        // Vecteur direction vers le centre + vitesse
        Vector2 direction = new Vector2(400 - x, 300 - y).nor(); // vers le centre
        velocity = direction.scl(speed);
    }

    public void update(float delta) {
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true); // Animation en boucle
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        animation.getKeyFrames()[0].getTexture().dispose();
    }
}