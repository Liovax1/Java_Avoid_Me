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

import java.util.ArrayList;
import java.util.Iterator;
import com.badlogic.gdx.math.MathUtils;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Player player;
    private ShapeRenderer shapeRenderer;
    private boolean gameOver = false;
    private BitmapFont font;
    private ArrayList<MovingObstacle> movingObstacles;
    private float spawnTimer = 0;
    private float spawnInterval = 1f;

    @Override
    public void create() {
        batch = new SpriteBatch();
        player = new Player();
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
                System.out.println("🔁 Partie relancée !");
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

        // Dessiner les obstacles et le joueur
        batch.begin();
        for (MovingObstacle obstacle : movingObstacles) {
            obstacle.render(batch);
        }
        player.render(batch);

        // Afficher le texte "GAME OVER" si le jeu est terminé
        if (gameOver) {
            System.out.println("💀 GAME OVER");
            font.draw(batch, "GAME OVER", 200, 400);
            font.draw(batch, "Appuyez sur R pour recommencer", 150, 370);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}