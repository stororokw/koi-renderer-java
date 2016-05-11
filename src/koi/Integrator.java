package koi;


public abstract class Integrator {
	public abstract RGB li(Scene scene, Ray ray);
	
	public boolean isInShadow(Scene scene, Ray ray, LightSample lightSample)
	{
		Intersection intersection = new Intersection();
		if(scene.intersect(ray, intersection))
		{
			double distanceToLight = (lightSample.point.minus(ray.origin)).lengthSquared();
			double distanceToPoint = (intersection.point.minus(ray.origin)).lengthSquared();
			return distanceToPoint < distanceToLight;
		}
		return false;
	}
}
