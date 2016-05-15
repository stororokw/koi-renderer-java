package koi.texture;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import koi.Intersection;
import koi.Koi;
import koi.RGB;
import koi.Texture;
import koi.util.Bitmap;

public class BitmapTexture extends Texture {
	
	float tilingU;
	float tilingV;
	float offsetU;
	float offsetV;
	Bitmap bitmap;
	double gamma;
	
	public BitmapTexture(String filename, double gamma) {
		try {
			bitmap = new Bitmap(ImageIO.read(new File(filename)));
			this.gamma = gamma;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BitmapTexture(Bitmap bitmap, double gamma) {
		this.bitmap = bitmap;
		this.gamma = gamma;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	@Override
	public RGB getValue(Intersection intersection) {
		double u = intersection.U;
		double v = intersection.V;
		
		u = Koi.clamp((u + offsetU) % 1.0, 0.0, 1.0);
		v = Koi.clamp((v + offsetV) % 1.0, 0.0, 1.0);
		
		u = u * (bitmap.getWidth() - 1);
		v = (1 - v) * (bitmap.getHeight() - 1);
		
		int U = (int) u;
		int V = (int) v;
	
		return bitmap.getPixel(U, V).pow(gamma);
	}

}
