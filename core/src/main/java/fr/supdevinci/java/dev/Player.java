package fr.supdevinci.java.dev;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float x;
    private float y;
    private float speed = 200f;
    private Rectangle bounds;

    public Player() {
        // Chargez le sprite sheet
        Texture spriteSheet = new Texture("player.png");

        // Découpez les frames (par exemple, 4 frames de largeur égale)
        int frameWidth = spriteSheet.getWidth() / 4; // 4 frames horizontales
        int frameHeight = spriteSheet.getHeight(); // 1 seule ligne
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[4]; // 4 frames
        for (int i = 0; i < 4; i++) {
            frames[i] = tmp[0][i]; // Supposons que les frames sont sur la première ligne
        }

        // Créez l'animation (0.1s par frame)
        animation = new Animation<>(0.1f, frames);
        stateTime = 0f;

        // Position initiale
        x = 140;
        y = 210;
        bounds = new Rectangle(x, y, frameWidth, frameHeight); // Taille du joueur
    }

    public void reset() {
        x = 140;
        y = 210;
        bounds.setPosition(x, y);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            x -= speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            x += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            y += speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            y -= speed * delta;
        bounds.setPosition(x, y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true); // Animation en boucle
        batch.draw(currentFrame, x, y);
    }

    public void dispose() {
        // Pas besoin de disposer des frames individuellement, juste le sprite sheet
        animation.getKeyFrames()[0].getTexture().dispose();
    }
}