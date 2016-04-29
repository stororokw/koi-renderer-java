package koi;

import koi.math.BBox;
import koi.math.Transform;

public abstract class Geometry {
	
	protected Transform objectToWorld = null;
	protected Transform worldToObject = null;
	
	public Geometry(Transform objectToWorld, Transform worldToObject) {
		this.objectToWorld = objectToWorld;
		this.worldToObject = worldToObject;
	}
	
	boolean isCompound() 
	{ 
		return false; 
	}
	
	public int getNumberOfGeometries() 
	{ 
		return 1; 
	}
	
	public Geometry GetSubGeometry(int i) 
	{ 
		return null;
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

	
}
