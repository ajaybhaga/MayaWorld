package com.monkeymaya.mayaworld;

import com.badlogic.gdx.ApplicationAdapter;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.monkeymaya.mayaworld.utils.OrthoCamController;
import com.badlogic.gdx.utils.TimeUtils;

public class MayaWorldGame extends ApplicationAdapter {
	static final int LAYERS = 1;
	static final int WIDTH = 30;
	static final int HEIGHT = 30;
	static final int TILES_PER_LAYER = WIDTH * HEIGHT;
	static final int TILE_WIDTH = 64;
	static final int TILE_HEIGHT = 64;
	static final int TILE_HEIGHT_DIAMOND = 32;
	static final int BOUND_X = HEIGHT * TILE_WIDTH / 2 + WIDTH * TILE_WIDTH / 2;
	static final int BOUND_Y = HEIGHT * TILE_HEIGHT_DIAMOND / 2 + WIDTH * TILE_HEIGHT_DIAMOND / 2;

	Texture texture;
	SpriteBatch[] batches = new SpriteBatch[LAYERS];
	int[] tilemap = new int[WIDTH*HEIGHT];
    int[] heightmap = new int[WIDTH*HEIGHT];
    int[] anglemap = new int[WIDTH*HEIGHT];
    int globalAngle = 0;

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

        for (int i = 0; i < LAYERS; i++) {
            batches[i] = new SpriteBatch();
        }

        initMap();
    }

    public void initMap() {

        Random rand = new Random();

        // Create flat land
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                //map[y*WIDTH+x] = rand.nextInt(4);
                tilemap[y*WIDTH+x] = 0;
                heightmap[y*WIDTH+x] = 0;
                anglemap[y*WIDTH+x] = 0;
            }
        }

        // Add large mountains
        for (int m = 0; m < 5; m++) {

            int rx = rand.nextInt(WIDTH-1) + 1;
            int ry = rand.nextInt(HEIGHT-1) + 1;
            int dir = rand.nextInt(4);
            //int height = rand.nextInt(10);
            int height = 0;

            int tileHeight = 14;
            int mSize = 2;

            // Middle tile
            heightmap[ry*WIDTH+rx] = tileHeight;
            tilemap[ry*WIDTH+rx] = (0*10)+0;

            // 4 flat sides
            tilemap[ry * WIDTH + (rx + 1)] = (2 * 10) + height;
            tilemap[ry * WIDTH + (rx - 1)] = (0 * 10) + height;
            tilemap[(ry + 1) * WIDTH + rx] = (1 * 10) + height;
            tilemap[(ry - 1) * WIDTH + rx] = (3 * 10) + height;

            // 4 diagonals
            tilemap[(ry-1)*WIDTH+(rx-1)] = (4*10)+height;
            tilemap[(ry+1)*WIDTH+(rx-1)] = (5*10)+height;
            tilemap[(ry-1)*WIDTH+(rx+1)] = (7*10)+height;
            tilemap[(ry+1)*WIDTH+(rx+1)] = (6*10)+height;
        }

        // Add large mountains
        for (int m = 0; m < 5; m++) {

            int rx = rand.nextInt(WIDTH-1) + 1;
            int ry = rand.nextInt(HEIGHT-1) + 1;
            int dir = rand.nextInt(4);
            //int height = rand.nextInt(10);
            int height = 0;


            //if (rx > 2 && ry > 2) {
            //map[ry*WIDTH+rx] = (dir*10)+height;

            // Middle tile
            heightmap[ry * WIDTH + rx] = 14;
            tilemap[ry * WIDTH + rx] = (0 * 10) + 0;

            /*
            // 4 flat sides
                tilemap[ry * WIDTH + (rx + 1)] = (2 * 10) + height;
                tilemap[ry * WIDTH + (rx - 1)] = (0 * 10) + height;
                tilemap[(ry + 1) * WIDTH + rx] = (1 * 10) + height;
                tilemap[(ry - 1) * WIDTH + rx] = (3 * 10) + height;


            // 4 diagonals
            tilemap[(ry-1)*WIDTH+(rx-1)] = (4*10)+height;
            tilemap[(ry+1)*WIDTH+(rx-1)] = (5*10)+height;
            tilemap[(ry-1)*WIDTH+(rx+1)] = (7*10)+height;
            tilemap[(ry+1)*WIDTH+(rx+1)] = (6*10)+height;
*/
            //}


            int tileHeight = 14;
            int mSize = 2;

            // Middle tile
            heightmap[ry*WIDTH+rx] = (tileHeight*2);
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



            //        map[ry*WIDTH+rx] = (dir*10)+height;
            //          map[ry*WIDTH+rx] = (dir*10)+height;
//            map[ry*WIDTH+rx] = (dir*10)+height;

        }

    }

	public void genMap() {

        Random rand = new Random();
        int r = rand.nextInt(8);
        int s = rand.nextInt(2);

        int w = Math.round(r/8.0f);
        if (s == 0) {
            w *= -1;
        }
        globalAngle += w;

        // Adjust weights
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                //map[y*WIDTH+x] = rand.nextInt(4);

                //heightmap[y*WIDTH+x] += w;
                //tilemap[y*WIDTH+x] += w;
//                anglemap[y*WIDTH+x] += w;
            }
        }

        if (globalAngle < 1) {
            globalAngle = 1;
        }

        if (globalAngle > 8) {
            globalAngle = 8;
        }

        //int w = anglemap[y*WIDTH+x];



        for (int i = 0; i < LAYERS; i++) {
            SpriteBatch batch = batches[i];
            batch.setProjectionMatrix(cam.combined);
            batch.begin();

            int colX = HEIGHT * TILE_WIDTH / 2 - TILE_WIDTH / 2;
            int colY = BOUND_Y - TILE_HEIGHT_DIAMOND;
            for (int x = 0; x < WIDTH; x++) {
                for (int y = 0; y < HEIGHT; y++) {
                    int tileX = colX - y * TILE_WIDTH / 2;
                    int tileY = (colY - y * TILE_HEIGHT_DIAMOND / 2) + heightmap[y*WIDTH+x];
                    int tileId = tilemap[y*WIDTH+x]+globalAngle;

                    batch.draw(texture, tileX, tileY, tileId * TILE_WIDTH, 0, TILE_WIDTH, TILE_HEIGHT);
                }
                colX += (TILE_WIDTH / 2)-1;
                colY -= (TILE_HEIGHT_DIAMOND / 2);
            }

            batch.end();
        }
    }

	@Override
	public void dispose () {
		renderer.dispose();
		texture.dispose();
		for (SpriteBatch batch : batches)
			batch.dispose();
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        genMap();

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
