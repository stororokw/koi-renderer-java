package koi;

import java.util.List;

import koi.math.BBox;

public abstract class Primitive {
	
	public abstract boolean intersectRay(Ray ray, Intersection intersection);
	public abstract BBox getBounds();
	public abstract List<Primitive> getPrimitives();
	
	public boolean isCompound()
	{
		return false;
	}
	
	public boolean isEmissive()
	{
		return false;
	}
}
