package koi.renderer;

import gui.TileListener;

import java.util.Random;

import koi.RGB;
import koi.Ray;
import koi.Scene;
import koi.math.Point2D;
import koi.util.Bitmap;

public class RenderTile implements Runnable {
	private int x0;
	private int y0;
	private int x1;
	private int y1;
	private int samples;
	
	private Bitmap bitmap = null;
	private Scene scene = null;
	private Renderer renderer = null;
	private Random random = new Random();
	
	public RenderTile(Renderer renderer, Scene scene, int x0, int y0, int x1, int y1, int samples) {
		this.scene = scene;
		this.renderer = renderer;
		this.bitmap = new Bitmap(x1, y1);
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.samples = samples;
	}

	@Override
	public void run() {
		for(TileListener listener : renderer.updateListeners)
		{
			listener.onStart(x0, y0, x1, y1, Thread.currentThread().getId() % Runtime.getRuntime().availableProcessors());
		}
		for (int row = 0; row < y1; ++row)
		{
			for (int col = 0; col < x1; ++col)
			{
				RGB colour = new RGB();
				for (int i = 0; i < samples; ++i)
				{
					Point2D ap = new Point2D(random.nextDouble(), random.nextDouble());
					double SampleX = (x0 + col + ap.X);
					double SampleY = (y0 + row + ap.Y);
					Ray ray = scene.getCamera().calculateRay(SampleX, SampleY, 0, 0, scene.getBitmap().getWidth(), scene.getBitmap().getHeight());
					colour.plusEquals(scene.getIntegrator().li(scene, ray));	
				}
				colour.divideEquals(scene.getSamples());
				colour = colour.pow(1 / 2.2).clamp();
				bitmap.setPixel(col, row, colour);
			}
		}
		scene.getBitmap().copy(bitmap, x0, y0, x1, y1);
		for(TileListener listener : renderer.updateListeners)
		{
			listener.onUpdate(x0, y0, x1, y1, Thread.currentThread().getId());
		}
	}
	

}
