package koi;

import koi.Bsdf.Type;


public abstract class Integrator {
	public abstract RGB li(Scene scene, Ray ray);
	
	public boolean isInShadow(Scene scene, Ray ray, LightSample lightSample, Intersection shadowIntersection)
	{
		if(scene.intersect(ray, shadowIntersection) && !shadowIntersection.material.isType(Type.EMISSIVE))
		{
			double distanceToLight = (lightSample.point.minus(ray.origin)).lengthSquared();
			double distanceToPoint = (shadowIntersection.point.minus(ray.origin)).lengthSquared();
			return distanceToPoint < distanceToLight;
		}
		return false;
	}
}
