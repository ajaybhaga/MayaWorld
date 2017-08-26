package com.monkeymaya.mayaworld;

import com.badlogic.gdx.ApplicationAdapter;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.monkeymaya.mayaworld.utils.OrthoCamController;
import com.badlogic.gdx.utils.TimeUtils;

public class MayaWorldGame extends ApplicationAdapter {
	static final int LAYERS = 1;
	static final int WIDTH = 48;
	static final int HEIGHT = 48;
	static final int TILES_PER_LAYER = WIDTH * HEIGHT;
	static final int TILE_WIDTH = 64;
	static final int TILE_HEIGHT = 64;
	static final int TILE_HEIGHT_DIAMOND = 32;
	static final int BOUND_X = HEIGHT * TILE_WIDTH / 2 + WIDTH * TILE_WIDTH / 2;
	static final int BOUND_Y = HEIGHT * TILE_HEIGHT_DIAMOND / 2 + WIDTH * TILE_HEIGHT_DIAMOND / 2;

	Texture texture;
	SpriteCache[] caches = new SpriteCache[LAYERS];
	int[] layers = new int[LAYERS];
	int[] tilemap = new int[WIDTH*HEIGHT];
    int[] heightmap = new int[WIDTH*HEIGHT];

	OrthographicCamera cam;
	OrthoCamController camController;
	ShapeRenderer renderer;
	long startTime = TimeUtils.nanoTime();

	@Override
	public void create () {
		cam = new OrthographicCamera(860, 480);
		camController = new OrthoCamController(cam);
		Gdx.input.setInputProcessor(camController);

		renderer = new ShapeRenderer();
		texture = new Texture(Gdx.files.internal("tiles/tiles.png"));

		Random rand = new Random();

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				//map[y*WIDTH+x] = rand.nextInt(4);
                tilemap[y*WIDTH+x] = 0;
                heightmap[y*WIDTH+x] = 0;
			}
		}


        for (int m = 0; m < 3; m++) {

            int rx = rand.nextInt(WIDTH-1) + 1;
            int ry = rand.nextInt(HEIGHT-1) + 1;
            int dir = rand.nextInt(4);
            //int height = rand.nextInt(10);
            int height = 4;


            //map[ry*WIDTH+rx] = (dir*10)+height;
/*
            // Middle tile
            heightmap[ry*WIDTH+rx] = 14;
            tilemap[ry*WIDTH+rx] = (0*10)+0;

            // 4 flat sides
            tilemap[ry*WIDTH+(rx+1)] = (2*10)+height;
            tilemap[ry*WIDTH+(rx-1)] = (0*10)+height;
            tilemap[(ry+1)*WIDTH+rx] = (1*10)+height;
            tilemap[(ry-1)*WIDTH+rx] = (3*10)+height;

            // 4 diagonals
            tilemap[(ry-1)*WIDTH+(rx-1)] = (4*10)+height;
            tilemap[(ry+1)*WIDTH+(rx-1)] = (5*10)+height;
            tilemap[(ry-1)*WIDTH+(rx+1)] = (7*10)+height;
            tilemap[(ry+1)*WIDTH+(rx+1)] = (6*10)+height;
*/

            int tileHeight = 14;
            int mSize = 2;

            // Middle tile
            heightmap[ry*WIDTH+rx] += (tileHeight*2);
            tilemap[ry*WIDTH+rx] = (0*10)+0;

            // 4 flat sides
            tilemap[ry*WIDTH+(rx+1)] = (2*10)+height;
            heightmap[ry*WIDTH+(rx+1)] += (tileHeight);

            tilemap[ry*WIDTH+(rx-1)] = (0*10)+height;
            heightmap[ry*WIDTH+(rx-1)] += (tileHeight);

            tilemap[(ry+1)*WIDTH+rx] = (1*10)+height;
            heightmap[(ry+1)*WIDTH+rx] += (tileHeight);

            tilemap[(ry-1)*WIDTH+rx] = (3*10)+height;
            heightmap[(ry-1)*WIDTH+rx] += (tileHeight);

            // 4 diagonals
            tilemap[(ry-1)*WIDTH+(rx-1)] = (4*10)+height;
            heightmap[(ry-1)*WIDTH+(rx-1)] += (tileHeight);

            tilemap[(ry+1)*WIDTH+(rx-1)] = (5*10)+height;
            heightmap[(ry+1)*WIDTH+(rx-1)] += (tileHeight);

            tilemap[(ry-1)*WIDTH+(rx+1)] = (7*10)+height;
            heightmap[(ry-1)*WIDTH+(rx+1)] += (tileHeight);

            tilemap[(ry+1)*WIDTH+(rx+1)] = (6*10)+height;
            heightmap[(ry+1)*WIDTH+(rx+1)] += (tileHeight);

            // 12 flat sides
            for (int i = -1; i < 2; i++) {
                tilemap[(ry+i) * WIDTH + (rx + 2)] = (2 * 10) + height;
                tilemap[(ry+i)*WIDTH+(rx-2)] = (0*10)+height;
                tilemap[(ry+2)*WIDTH+(rx+i)] = (1*10)+height;
                tilemap[(ry-2)*WIDTH+(rx+i)] = (3*10)+height;
            }

            // 4 diagonals
            tilemap[(ry-2)*WIDTH+(rx-2)] = (4*10)+height;
            //heightmap[(ry-2)*WIDTH+(rx-2)] += (tileHeight);

            tilemap[(ry+2)*WIDTH+(rx-2)] = (5*10)+height;
            //heightmap[(ry+2)*WIDTH+(rx-2)] += (tileHeight);

            tilemap[(ry-2)*WIDTH+(rx+2)] = (7*10)+height;
            //heightmap[(ry-2)*WIDTH+(rx+2)] += (tileHeight);

            tilemap[(ry+2)*WIDTH+(rx+2)] = (6*10)+height;
            //heightmap[(ry+2)*WIDTH+(rx+2)] += (tileHeight);

/*
            tilemap[ry*WIDTH+(rx-1)] = (0*10)+height;
            heightmap[ry*WIDTH+(rx-1)] += (tileHeight);

            tilemap[(ry+1)*WIDTH+rx] = (1*10)+height;
            heightmap[(ry+1)*WIDTH+rx] += (tileHeight);

            tilemap[(ry-1)*WIDTH+rx] = (3*10)+height;
            heightmap[(ry-1)*WIDTH+rx] += (tileHeight);
*/

            //        map[ry*WIDTH+rx] = (dir*10)+height;
  //          map[ry*WIDTH+rx] = (dir*10)+height;
//            map[ry*WIDTH+rx] = (dir*10)+height;




        }


		for (int i = 0; i < LAYERS; i++) {
			caches[i] = new SpriteCache();
			SpriteCache cache = caches[i];
			cache.beginCache();

			int colX = HEIGHT * TILE_WIDTH / 2 - TILE_WIDTH / 2;
			int colY = BOUND_Y - TILE_HEIGHT_DIAMOND;
			for (int x = 0; x < WIDTH; x++) {
				for (int y = 0; y < HEIGHT; y++) {
					int tileX = colX - y * TILE_WIDTH / 2;
					int tileY = (colY - y * TILE_HEIGHT_DIAMOND / 2) + heightmap[y*WIDTH+x];

					cache.add(texture, tileX, tileY, tilemap[y*WIDTH+x] * TILE_WIDTH, 0, TILE_WIDTH, TILE_HEIGHT);
				}
				colX += (TILE_WIDTH / 2)-1;
				colY -= (TILE_HEIGHT_DIAMOND / 2);
			}

			layers[i] = cache.endCache();
		}
	}

	@Override
	public void dispose () {
		renderer.dispose();
		texture.dispose();
		for (SpriteCache cache : caches)
			cache.dispose();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		for (int i = 0; i < LAYERS; i++) {
			SpriteCache cache = caches[i];
			cache.setProjectionMatrix(cam.combined);
			cache.begin();
			cache.draw(layers[i]);
			cache.end();
		}

		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeType.Line);
		renderer.setColor(1, 0, 0, 1);
		renderer.line(0, 0, 500, 0);
		renderer.line(0, 0, 0, 500);

		renderer.setColor(0, 0, 1, 1);
		renderer.line(0, BOUND_Y, BOUND_X, BOUND_Y);

		renderer.setColor(0, 0, 1, 1);
		renderer.line(BOUND_X, 0, BOUND_X, BOUND_Y);

		renderer.end();
	}
}
