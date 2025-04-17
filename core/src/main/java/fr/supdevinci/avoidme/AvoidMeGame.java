package fr.supdevinci.avoidme;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AvoidMeGame extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new GameScreen(this));
    }

    @Override
    public void render() {
        super.render(); // appelle le screen actif (GameScreen)
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
