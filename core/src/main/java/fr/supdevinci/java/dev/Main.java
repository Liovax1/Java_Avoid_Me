package fr.supdevinci.java.dev;

import fr.supdevinci.java.dev.Player;
import fr.supdevinci.java.dev.MovingObstacle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.math.MathUtils;

import java.util.logging.Logger;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private ShapeRenderer shapeRenderer;
    private boolean gameOver = false;
    private BitmapFont font;
    private ArrayList<MovingObstacle> movingObstacles;
    private float spawnTimer = 0;
    private float spawnInterval = 1f;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private Viewport viewport;
    private OrthographicCamera camera;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(800, 600, camera);
        viewport.apply();

        batch = new SpriteBatch();
        player = new Player(viewport); // Passez le viewport au joueur
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(); // police par défaut
        movingObstacles = new ArrayList<>();
    }

    @Override
    public void render() {
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        if (!gameOver) {
            player.update(delta);

            // Relancer le jeu si game over et touche R pressée
            if (gameOver && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
                gameOver = false;
                player.reset();
                movingObstacles.clear(); // Réinitialiser les obstacles
                logger.info("🔁 Partie relancée !");
            }

            // Générer un nouvel obstacle toutes les secondes
            spawnTimer += delta;
            if (spawnTimer >= spawnInterval) {
                spawnTimer = 0;
                movingObstacles.add(new MovingObstacle(64, 64, 150)); // Taille + vitesse
            }

            // Mise à jour des obstacles et vérification des collisions
            Iterator<MovingObstacle> iterator = movingObstacles.iterator();
            while (iterator.hasNext()) {
                MovingObstacle obstacle = iterator.next();
                obstacle.update(delta);

                if (obstacle.getBounds().overlaps(player.getBounds())) {
                    gameOver = true;
                }
            }
        }

        // Effacer l'écran
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Mettez à jour la caméra
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Dessiner les obstacles et le joueur
        batch.begin();
        for (MovingObstacle obstacle : movingObstacles) {
            obstacle.render(batch);
        }
        player.render(batch);

        // Afficher le texte "GAME OVER" si le jeu est terminé
        if (gameOver) {
            logger.info("💀 GAME OVER");
            font.draw(batch, "GAME OVER", 200, 400);
            font.draw(batch, "Appuyez sur R pour recommencer", 150, 370);
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true); // Adaptez le viewport à la nouvelle taille
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}