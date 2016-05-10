package koi.renderer;

import gui.TileListener;

import java.util.Collections;
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

		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService executioner = Executors.newFixedThreadPool(processors);
		
		tilesX = (bitmap.getWidth() + tileSize) / tileSize;
		tilesY = (bitmap.getHeight() + tileSize) / tileSize;
		DIRECTION CurrentDirection = DIRECTION.DOWN;
		int StepSquareSize = 0;
		int StepsInCurrentSquare = -1;
		int StepsInCurrentDirection = -1;
		int Row = tilesY / 2;
		int Column = tilesX / 2;

		try {
			for (int i = 0; i < Math.max(tilesX, tilesY) * Math.max(tilesX, tilesY); ++i)
			{
				StepsInCurrentSquare++;
				StepsInCurrentDirection++;
				if (StepsInCurrentDirection == StepSquareSize)
				{
					StepsInCurrentDirection = 0;
					switch (CurrentDirection)
					{
					case RIGHT:
						CurrentDirection = DIRECTION.DOWN;
						break;
					case DOWN:
						CurrentDirection = DIRECTION.LEFT;
						break;
					case LEFT:
						CurrentDirection = DIRECTION.UP;
						break;
					case UP:
						CurrentDirection = DIRECTION.RIGHT;
						break;
					}
				}
				
				if (StepsInCurrentSquare == (StepSquareSize + 1) * (StepSquareSize + 1) - 1)
				{
					StepSquareSize++;
				}
	
				switch (CurrentDirection)
				{
					case RIGHT:
						Column += 1;
						break;
					case DOWN:
						Row += 1;
						break;
					case LEFT:
						Column -= 1;
						break;
					case UP:
						Row -= 1;
						break;
				}
				
				if (!(Column < 0 || Column > tilesX) && !(Row < 0 || Row > tilesY))
				{
					int x0 = Column * tileSize;
					int y0 = Row * tileSize;
					int x1 = Math.min(tileSize, bitmap.getWidth() - x0);
					int y1 = Math.min(tileSize, bitmap.getHeight() - y0);
					if(x1 > 0 && y1 > 0)
						tasks.add(new RenderTile(this, scene, x0, y0, x1, y1, scene.getSamples()));
				}
			}
			
			Collections.reverse(tasks);
			while(!tasks.isEmpty())
			{
				executioner.execute(tasks.pop());
			}
			
			executioner.shutdown();
			executioner.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			for(TileListener listener : updateListeners)
			{
				listener.onComplete();
			}
		} catch (InterruptedException e) {
			// User pressed stop rendering button
			executioner.shutdownNow();
		} catch( Exception e)
		{
			e.printStackTrace();
		}
	}

	enum DIRECTION
	{
		LEFT,
		RIGHT,
		DOWN,
		UP
	}
}
