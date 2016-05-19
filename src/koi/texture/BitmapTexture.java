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
	
	double tilingU;
	double tilingV;
	double offsetU;
	double offsetV;
	Bitmap bitmap;
	double gamma;
	
	public BitmapTexture(String filename, double gamma) {
		this(filename, gamma, 1, 1, 0, 0);
	}
	
	public BitmapTexture(String filename, double gamma, double tilingU, double tilingV, double offsetU, double offsetV) {
		try {
			bitmap = new Bitmap(ImageIO.read(new File(filename)));
			this.gamma = gamma;
			this.tilingU = tilingU;
			this.tilingV = tilingV;
			this.offsetU = offsetU;
			this.offsetV = offsetV;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BitmapTexture(Bitmap bitmap, double gamma) {
		this.bitmap = bitmap;
		this.gamma = gamma;
		this.tilingU = 1.0;
		this.tilingV = 1.0;
		this.offsetU = 0.0;
		this.offsetV = 0.0;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	@Override
	public RGB getValue(Intersection intersection) {
		double u = intersection.U;
		double v = intersection.V;
		
		u = Koi.clamp(((u + offsetU) * tilingU) % 1.0, 0.0, 1.0);
		v = Koi.clamp(((v + offsetV) * tilingV) % 1.0, 0.0, 1.0);
		
		u = u * (bitmap.getWidth() - 1);
		v = (1 - v) * (bitmap.getHeight() - 1);
		
		int U = (int) u;
		int V = (int) v;
		RGB uv00 = new RGB(bitmap.getPixel(U    , V    ));
		RGB uv10 = new RGB(bitmap.getPixel(U + 1, V    ));
		RGB uv01 = new RGB(bitmap.getPixel(U    , V + 1));
		RGB uv11 = new RGB(bitmap.getPixel(U + 1, V + 1));
		return Koi.BilinearInterp(new RGB(u - U), new RGB(v - V), uv00, uv10, uv01, uv11).pow(gamma);
	}

}
