package com.syllab.games;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Random;

public class Zeldalus extends ApplicationAdapter {
	// Game constants
	public final static int MAP_SIDE = 19;
	public final static int MAP_CENTER = MAP_SIDE/2;

	public final static int CHARACTER_WIDTH  =32;
	public final static int CHARACTER_HEIGHT =48;

	public final static int BACKGROUND_TILE_WIDTH  = 16;
	public final static int BACKGROUND_TILE_HEIGHT = 16;

	public final static int MAP_CELL_TILE_COUNT_X = 4;
	public final static int MAP_CELL_TILE_COUNT_Y = 3;

	public final static int MAP_CELL_WIDTH  = MAP_CELL_TILE_COUNT_X * BACKGROUND_TILE_WIDTH;
	public final static int MAP_CELL_HEIGHT = MAP_CELL_TILE_COUNT_Y * BACKGROUND_TILE_HEIGHT;

	public static int toMapX(float x) { return (int)x/(MAP_CELL_WIDTH /2); }
	public static int toMapY(float y) { return (int)y/(MAP_CELL_HEIGHT/2); }

	public static float fromMapX(int x) { return (float)x*(float)(MAP_CELL_WIDTH /2); }
	public static float fromMapY(int y) { return (float)y*(float)(MAP_CELL_HEIGHT /2); }

	public final static int SCREEN_WIDTH  = MAP_CENTER * MAP_CELL_WIDTH + BACKGROUND_TILE_WIDTH;
	public final static int SCREEN_HEIGHT = MAP_CENTER * MAP_CELL_HEIGHT + BACKGROUND_TILE_HEIGHT;

	private final int DIRECTIONS = 4;
	private final float CHARACTER_ANIM_SPEED = .1f;
	private final float CHARACTER_SPEED = 150f;

	// Game data
	private final Random rand = new Random();
	private final boolean [][] map = new boolean[1+MAP_SIDE+1][1+MAP_SIDE+1];
	private float gotoX, gotoY, characterX, characterY;
	private float time = .0f;
	private boolean resetting = true;
	private int stop = 0;

	// Graphical data
	private SpriteBatch batch;
	private Texture txBackground, txCharacter;
	private TextureRegion [][] txrBgTiles;
	private TextureRegion txrBgHole, txrBgWall;
	private TextureRegion [][] txrCharacterTiles;
	private Animation<TextureRegion>[] animCharacter;

	@Override
	public void create () {

		txBackground = new Texture("background.png");
		txrBgTiles = TextureRegion.split(txBackground, BACKGROUND_TILE_WIDTH, BACKGROUND_TILE_HEIGHT);
		txrBgHole = txrBgTiles[0][1];
		txrBgWall = txrBgTiles[0][3];

		txCharacter  = new Texture("character.png");
		txrCharacterTiles = TextureRegion.split(txCharacter, CHARACTER_WIDTH, CHARACTER_HEIGHT);

		animCharacter = new Animation[DIRECTIONS];
		for(int i = 0; i<animCharacter.length; i++) {
			animCharacter[i] = new Animation<TextureRegion>(
					CHARACTER_ANIM_SPEED,
					txrCharacterTiles[i]
			);
		}
		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		// Init/Reset
		if(resetting) {
			stop = 0;
			gotoX=characterX= fromMapX(MAP_CENTER);
			gotoY=characterY= fromMapY(MAP_CENTER);
			for(int y=0; y<=MAP_CENTER; y++)
				for(int x=0; x<=MAP_CENTER; x++) {
					int rx = MAP_SIDE-1-x;
					int ry = MAP_SIDE-1-y;

					map[x][y] = map[rx][y] = map[x][ry] = map[rx][ry] = isEven(Math.min(x,y));
				}
			for(int i=1; i<=MAP_CENTER; i++)
				if(isOdd(i)) {
					addRandomHole(i);
					addRandomHole(i);
				}
				else
					addRandomWall(i);
		}
		// Input
		if( gotoX==characterX && gotoY==characterY ) {
			int mapX = toMapX(gotoX);
			int mapY = toMapY(gotoY);
			int dx=0, dy=0;

			resetting =(mapX==0)||(mapY==0)||(mapX==MAP_SIDE-1)||(mapY==MAP_SIDE-1);
			if(!resetting)
			{
				if(Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.RIGHT))
					dx=Gdx.input.isKeyPressed(Input.Keys.LEFT) ? -1:1;
				else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)||Gdx.input.isKeyPressed(Input.Keys.UP))
					dy=Gdx.input.isKeyPressed(Input.Keys.DOWN) ? -1:1;

				assert(((dx==0)||(dy==0))&&(dx>=-1)&&(dx<=1)&&(dy>=-1)&&(dy<=1));
				if(!map[mapX+dx][mapY+dy])
				{
					gotoX=fromMapX(mapX+dx);
					gotoY=fromMapY(mapY+dy);
				}
			}
		}

		// Move
		int anim=-1;
		float dt = Gdx.graphics.getDeltaTime();

		time += dt;
		assert((gotoX==characterX)||(gotoY==characterY));
		if(gotoX<characterX) { anim=1; characterX=Math.max(gotoX, characterX-CHARACTER_SPEED*dt); }
		if(gotoX>characterX) { anim=2; characterX=Math.min(gotoX, characterX+CHARACTER_SPEED*dt); }
		if(gotoY<characterY) { anim=0; characterY=Math.max(gotoY, characterY-CHARACTER_SPEED*dt); }
		if(gotoY>characterY) { anim=3; characterY=Math.min(gotoY, characterY+CHARACTER_SPEED*dt); }

		// Render
		batch.begin();
		for(int y=0; y<MAP_SIDE; y+=2)
		{
			for(int x=0; x<MAP_SIDE; x+=2)
			{
				float offsetX=(float) fromMapX(x);
				float offsetY=(float) fromMapY(y);

				assert(map[x][y]&&!map[x+1][y+1]);
				batch.draw(txrBgWall, offsetX, offsetY);
				for(int i = 1; i< MAP_CELL_TILE_COUNT_X; i++)
					batch.draw(map[x+1][y] ? txrBgWall : txrBgTiles[0][i-1], offsetX+BACKGROUND_TILE_WIDTH*i, offsetY);
				for(int i = 1; i< MAP_CELL_TILE_COUNT_Y; i++) {
					batch.draw(map[x  ][y+1] ? txrBgWall : txrBgTiles[4 - i][1], offsetX, offsetY + BACKGROUND_TILE_HEIGHT * i);
					batch.draw(map[x+1][y+(i-1)*2] ? txrBgTiles[4 - i][1] : txrBgHole, offsetX+2*BACKGROUND_TILE_WIDTH, offsetY + BACKGROUND_TILE_HEIGHT * i);
				}
				for(int i=0; i<DIRECTIONS; i++) {
					int index = (map[x+1][y+2-(i&2)] ? 1:0)
							   |(map[x+(i&1)*2][y+1] ? 2:0);

					batch.draw(
						txrBgTiles[i+1][index],
						offsetX+(1+(i&1)*2)*BACKGROUND_TILE_WIDTH,
						offsetY+(2-(i&2)/2)*BACKGROUND_TILE_HEIGHT
					);
				}
			}
		}
		TextureRegion txrCharacter = anim<0
			? txrCharacterTiles[stop][0]
			: animCharacter[stop = anim].getKeyFrame(time, true);

		batch.draw(txrCharacter, characterX-BACKGROUND_TILE_WIDTH/2, characterY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		txCharacter.dispose();
		txBackground.dispose();
	}

	private static boolean isOdd (int integer) { return (integer&1) != 0; }
	private static boolean isEven(int integer) { return (integer&1) == 0; }
	private int opposite(int val, int bit) { return bit!=0 ? -val : val; }

	private void addRandomHole(int radius) { addRandom(radius, false); }
	private void addRandomWall(int radius) { addRandom(radius, true); }
	private void addRandom(int radius, boolean wall) {
		int direction=rand.nextInt(8);
		int pos=(radius+1)%2+2*(rand.nextInt(radius+1)/2);
		int [] offset = isOdd(direction)
				? new int[]{ pos, radius }
				: new int[]{ radius, pos };
		int x = MAP_CENTER + opposite(offset[0], direction&2);
		int y = MAP_CENTER + opposite(offset[1], direction&4);

		map[x][y] = wall;
	}
}