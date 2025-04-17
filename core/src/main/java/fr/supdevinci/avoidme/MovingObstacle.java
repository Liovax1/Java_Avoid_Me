package fr.supdevinci.avoidme;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.MathUtils;

public class MovingObstacle {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private Rectangle bounds;
    private Vector2 velocity;

    public MovingObstacle(float width, float height, float speed) {
        String[] creatureFiles = {
                "creature.png", "creature-2.png", "creature-3.png", "creature-4.png",
                "creature-5.png", "creature-6.png", "creature-7.png", "creature-8.png",
                "creature-9.png", "creature-10.png"
        };
        String selectedCreature = creatureFiles[MathUtils.random(creatureFiles.length - 1)];
        Texture spriteSheet = new Texture(selectedCreature);

        int frameWidth = spriteSheet.getWidth() / 4;
        int frameHeight = spriteSheet.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[4];
        System.arraycopy(tmp[0], 0, frames, 0, 4);

        animation = new Animation<>(0.1f, frames);
        stateTime = 0f;

        float x = 0, y = 0;
        int edge = MathUtils.random(3);

        switch (edge) {
            case 0:
                x = MathUtils.random(Constants.WORLD_WIDTH);
                y = Constants.WORLD_HEIGHT;
                break;
            case 1:
                x = MathUtils.random(Constants.WORLD_WIDTH);
                y = -height;
                break;
            case 2:
                x = -width;
                y = MathUtils.random(Constants.WORLD_HEIGHT);
                break;
            case 3:
                x = Constants.WORLD_WIDTH;
                y = MathUtils.random(Constants.WORLD_HEIGHT);
                break;
        }

        bounds = new Rectangle(x, y, width, height);
        Vector2 direction = new Vector2(Constants.WORLD_WIDTH / 2f - x, Constants.WORLD_HEIGHT / 2f - y).nor();
        velocity = direction.scl(speed);
    }

    public void update(float delta) {
        bounds.x += velocity.x * delta;
        bounds.y += velocity.y * delta;
    }

    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        animation.getKeyFrames()[0].getTexture().dispose();
    }
}
