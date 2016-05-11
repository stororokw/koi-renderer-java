package koi;

import koi.math.BBox;
import koi.math.Point2D;
import koi.math.Transform;

public abstract class Geometry {
	
	protected Transform objectToWorld = null;
	protected Transform worldToObject = null;
	
	public Geometry(Transform objectToWorld, Transform worldToObject) {
		this.objectToWorld = objectToWorld;
		this.worldToObject = worldToObject;
	}
	
	public boolean isCompound() 
	{ 
		return false; 
	}
	
	public int getNumberOfGeometries() 
	{ 
		return 1; 
	}
	
	public Geometry getSubGeometry(int i) 
	{ 
		return this;
	}
	
	public Transform getWorldTransform()
	{
		return objectToWorld;
	}
	
	public Transform getObjectTransform()
	{
		return worldToObject;
	}
	
	public abstract BBox getBounds();
	public abstract boolean intersectRay(Ray ray, Intersection intersection);
	public abstract void sample(Point2D sample, GeometrySample geometrySample);
	public abstract double getSurfaceArea();

	
}
