package koi.texture;

import koi.Intersection;
import koi.RGB;
import koi.Texture;

public class SolidColourTexture extends Texture {
	private RGB colour;
	private double gamma;
	
	public SolidColourTexture(double r, double g, double b) {
		this(r, g, b, 2.2);
	}
	
	public SolidColourTexture(double r, double g, double b, double gamma) {
		this.colour = new RGB(r, g, b).pow(gamma);
	}
	
	@Override
	public RGB getValue(Intersection intersection) {
		return colour;
	}
	
}
