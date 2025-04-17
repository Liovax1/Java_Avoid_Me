package fr.supdevinci.avoidme;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float x;
    private float y;
    private Rectangle bounds;
    private Viewport viewport;

    public Player(Viewport viewport) {
        this.viewport = viewport;

        Texture spriteSheet = new Texture("player.png");
        int frameWidth = spriteSheet.getWidth() / 4;
        int frameHeight = spriteSheet.getHeight();

        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[4];
        System.arraycopy(tmp[0], 0, frames, 0, 4);

        animation = new Animation<>(0.1f, frames);
        stateTime = 0f;

        x = 140;
        y = 210;
        bounds = new Rectangle(x, y, frameWidth, frameHeight);
    }

    public void reset() {
        x = 140;
        y = 210;
        bounds.setPosition(x, y);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= Constants.PLAYER_SPEED * delta;
            if (x < 0)
                x = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += Constants.PLAYER_SPEED * delta;
            if (x + bounds.width > Constants.WORLD_WIDTH)
                x = Constants.WORLD_WIDTH - bounds.width;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += Constants.PLAYER_SPEED * delta;
            if (y + bounds.height > Constants.WORLD_HEIGHT)
                y = Constants.WORLD_HEIGHT - bounds.height;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= Constants.PLAYER_SPEED * delta;
            if (y < 0)
                y = 0;
        }

        bounds.setPosition(x, y);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, x, y);
    }

    public void dispose() {
        animation.getKeyFrames()[0].getTexture().dispose();
    }
}
