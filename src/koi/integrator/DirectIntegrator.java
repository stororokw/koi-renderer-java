package koi.integrator;

import koi.BsdfSample;
import koi.Integrator;
import koi.Intersection;
import koi.Light;
import koi.LightSample;
import koi.Material;
import koi.RGB;
import koi.Ray;
import koi.Scene;
import koi.math.Normal;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Vector3D;

public class DirectIntegrator extends Integrator {

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
	
	@Override
	public RGB li(Scene scene, Ray ray)
	{
		Intersection intersection = new Intersection();
		if(scene.intersect(ray, intersection))
		{
			RGB color = new RGB();
			Point3D point = intersection.point;
			Material material = intersection.material;
			Normal normal = new Normal(intersection.basis.W);
			Vector3D wo = ray.direction.negate();
			BsdfSample bsdfSample = new BsdfSample();
			for(Light light : scene.getLights())
			{
				LightSample lightSample = new LightSample();
				Point2D sample = new Point2D(Math.random(), Math.random());
				light.sample(sample, lightSample, intersection);
				Ray shadowRay = new Ray(point, lightSample.wi.hat());
				RGB f = material.SampleF(lightSample.wi, bsdfSample, intersection);
				if(isInShadow(scene, shadowRay, lightSample) || f.equals(RGB.black))
				{
					continue;
				}
				color.plusEquals(lightSample.li.times(f)).timesEquals(Math.abs(lightSample.wi.dot(normal)));
			}
			return color;
			
		}
		return new RGB(0.2, 0.2, 0.2);
	}
}
