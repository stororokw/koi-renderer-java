package koi;

import koi.math.Point2D;
import koi.math.Transform;

public abstract class Light {

	private Transform localToWorld = null;
	
	public Light(Transform localToWorld) {
		this.localToWorld = localToWorld;
	}
	
	public abstract void sample(Point2D sample, LightSample lightSample, Intersection intersection);
}
