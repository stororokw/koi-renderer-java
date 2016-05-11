package koi;

import java.util.ArrayList;
import java.util.List;

import koi.light.PointLight;
import koi.util.Bitmap;

public class Scene {

	protected Camera camera = null;
	protected Integrator integrator = null;
	protected ArrayList<Primitive> geometries = new ArrayList<Primitive>();
	protected ArrayList<Light> lights = new ArrayList<Light>();
	protected int samples = 16;
	protected Bitmap bitmap = null;
	
	public Scene()
	{
		
	}

	public boolean intersect(Ray ray, Intersection intersection)
	{
		boolean hit = false;
		for(Primitive geometry : geometries)
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
	
	public void addGeometry(Primitive geometry)
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

	public List<Light> getLights() {
		return lights;
	}

	public void addLight(Light pointLight) {
		lights.add(pointLight);
	}
}
