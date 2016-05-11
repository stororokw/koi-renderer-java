package koi;

import koi.math.Point2D;
import koi.math.Transform;

public interface Light {

	default public boolean isArea()
	{
		return false;
	}
	
	default public boolean isDelta()
	{
		return false;
	}
	
	default public boolean isInfinite()
	{
		return false;
	}
	
	default public RGB Le(Ray ray, Intersection intersection)
	{
		return RGB.black;
	}
	
	public abstract Transform getWorldTransform();
	public abstract Transform getLocalTransform();
	public abstract void sample(Point2D sample, LightSample lightSample, Intersection intersection);
}
