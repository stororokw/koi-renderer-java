package koi.renderer;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import gui.TileListener;
import koi.Intersection;
import koi.RGB;
import koi.Ray;
import koi.Scene;
import koi.math.Point2D;
import koi.util.Bitmap;

public class SimpleRenderer extends Renderer{

	private Scene scene = null;
	private Bitmap bitmap = null;
	
	public SimpleRenderer(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	@Override
	public void render() 
	{
		int Width = bitmap.getWidth();
		int Height = bitmap.getHeight();
		
		int processors = Runtime.getRuntime().availableProcessors();
		ExecutorService executioner = Executors.newFixedThreadPool(processors);
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				for (int row = 0; row < Height && !executioner.isShutdown(); ++row)
				{
					for (int col = 0; col < Width; ++col)
					{
						RGB colour = new RGB();

						for (int i = 0; i < scene.getSamples(); ++i)
						{
							Point2D ap = new Point2D(Math.random(), Math.random());
							double SampleX = (col + ap.X);
							double SampleY = (row + ap.Y);
							Ray ray = scene.getCamera().calculateRay(SampleX, SampleY, 0, 0, Width, Height);
							colour.plusEquals(scene.getIntegrator().li(scene, ray));
						}
						colour.divideEquals((double)scene.getSamples());
						bitmap.setPixel(col, row, colour);
					}
					for(TileListener tile : updateListeners)
					{
						tile.onUpdate(Thread.currentThread().getId());
					}
				}
				
			}
		};
		
		executioner.execute(task);
		
		try {
			executioner.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			// User pressed stop rendering button
			executioner.shutdownNow();
		}
		

	}

	public RGB li(Ray ray, Intersection intersect)
	{
		RGB color = new RGB();
		if (scene.intersect(ray, intersect))
		{
			color.plusEquals(scene.getIntegrator().li(scene, ray));
		}
		else
		{
			return new RGB(0.5, 0.5, 0.5);
		}

		return color;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	
}
