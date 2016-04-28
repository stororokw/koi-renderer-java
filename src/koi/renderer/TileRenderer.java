package koi.renderer;

import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import koi.Scene;
import koi.util.Bitmap;

public class TileRenderer extends Renderer {
	
	private Scene scene = null;
	private Bitmap bitmap = null;
	
	private int tilesX;
	private int tilesY;
	private int tiles;
	
	private int tileSize;
	
	
	public TileRenderer(Scene scene, Bitmap bitmap, int tileSize) {
		this.scene = scene;
		this.bitmap = bitmap;
		this.tileSize = tileSize;
		this.tilesX = (bitmap.getWidth() + tileSize) / tileSize;
		this.tilesY = (bitmap.getHeight() + tileSize) / tileSize;
		this.tiles = tilesX * tilesY;
		
	}

	@Override
	public void render() {
		Stack<RenderTile> tasks = new Stack<RenderTile>();
		
		for (int currentTile = 0; currentTile < tiles; ++currentTile)
		{
			int row = currentTile / tilesX;
			int col = currentTile % tilesX;
	        int x0 = col * tileSize;
	        int y0 = row * tileSize;
	        int x1 = Math.min(tileSize, bitmap.getWidth() - x0);
	        int y1 = Math.min(tileSize, bitmap.getHeight() - y0);
			tasks.add(new RenderTile(this, scene, x0, y0, x1, y1, scene.getSamples()));
		}		
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService executioner = Executors.newFixedThreadPool(processors);
		while(!tasks.isEmpty())
		{
			executioner.execute(tasks.pop());
		}
		
		executioner.shutdown();
		try {
			executioner.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// User pressed stop rendering button
			executioner.shutdownNow();
		}

	}
}
