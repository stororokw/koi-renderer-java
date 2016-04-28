package koi;

import java.util.ArrayList;

import koi.util.Bitmap;

public class Scene {

	protected Camera camera = null;
	protected Integrator integrator = null;
	protected ArrayList<Geometry> geometries = new ArrayList<Geometry>();
	protected int samples = 16;
	protected Bitmap bitmap = null;
	
	public Scene()
	{
		
	}

	public boolean intersect(Ray ray, Intersection intersection)
	{
		boolean hit = false;
		for(Geometry geometry : geometries)
		{
			if(geometry.intersectRay(ray, intersection))
			{
				hit = true;
			}
		}
		return hit;
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public int getSamples() {
		return samples;
	}
	
	public Integrator getIntegrator() {
		return integrator;
	}
	
	public void addGeometry(Geometry geometry)
	{
		geometries.add(geometry);
	}
	
	public void setSamples(int samples) {
		this.samples = samples;
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	
	public void setIntegrator(Integrator integrator) {
		this.integrator = integrator;
	}
}
