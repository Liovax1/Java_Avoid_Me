package fr.supdevinci.avoidme;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.Input;
import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen extends ScreenAdapter {

    private final AvoidMeGame game;
    private OrthographicCamera camera;
    private Viewport viewport;

    private Player player;
    private ArrayList<MovingObstacle> obstacles;
    private float spawnTimer;
    private boolean gameOver = false;

    private BitmapFont font;
    private int score;

    public GameScreen(AvoidMeGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        viewport.apply();

        font = new BitmapFont();
        obstacles = new ArrayList<>();
        player = new Player(viewport);
        score = 0; // Initialisation du score
    }

    @Override
    public void render(float delta) {
        if (!gameOver) {
            player.update(delta);

            // Génération d’obstacles
            spawnTimer += delta;
            if (spawnTimer >= Constants.OBSTACLE_SPAWN_INTERVAL) {
                spawnTimer = 0;
                obstacles.add(new MovingObstacle(Constants.OBSTACLE_WIDTH, Constants.OBSTACLE_HEIGHT,
                        Constants.OBSTACLE_SPEED));
            }

            // Update + collision
            Iterator<MovingObstacle> it = obstacles.iterator();
            while (it.hasNext()) {
                MovingObstacle obstacle = it.next();
                obstacle.update(delta);

                if (obstacle.getBounds().overlaps(player.getBounds())) {
                    if (obstacle.isMonster()) {
                        player.loseLife();
                        it.remove(); // Supprimer le monstre après collision
                        if (player.getLives() <= 0) {
                            gameOver = true; // Fin de la partie si plus de vies
                        }
                    } else {
                        score++; // Incrémenter le score si c'est une créature
                        it.remove(); // Supprimer l'obstacle touché
                    }
                }
            }
        }

        if (gameOver && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            restart();
        }

        // Affichage
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        for (MovingObstacle obstacle : obstacles) {
            obstacle.render(game.batch);
        }
        player.render(game.batch);
        font.draw(game.batch, "Score: " + score, 10, 580); // Afficher le score
        font.draw(game.batch, "Lives: " + player.getLives(), 10, 550); // Afficher les vies
        if (gameOver) {
            font.draw(game.batch, "GAME OVER", 300, 400);
            font.draw(game.batch, "Appuyez sur R pour recommencer", 240, 370);
        }
        game.batch.end();
    }

    private void restart() {
        player.reset();
        obstacles.clear();
        gameOver = false;
        spawnTimer = 0;
        score = 0; // Réinitialiser le score
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        font.dispose();
        player.dispose();
        for (MovingObstacle obstacle : obstacles) {
            obstacle.dispose();
        }
    }
}