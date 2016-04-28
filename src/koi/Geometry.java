package koi;

import koi.math.BBox;
import koi.math.Transform;

public abstract class Geometry {
	
	protected Transform worldToObject = null;
	protected Transform objectToWorld = null;
	
	public Geometry(Transform worldToObject, Transform objectToWorld) {
		this.worldToObject = worldToObject;
		this.objectToWorld = objectToWorld;
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
	
	public abstract BBox getBounds();
	public abstract boolean intersectRay(Ray ray, Intersection intersection);

	
}
