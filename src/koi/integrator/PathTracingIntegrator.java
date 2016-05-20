package koi.integrator;

import java.util.concurrent.ThreadLocalRandom;

import koi.Bsdf.Type;
import koi.BsdfSample;
import koi.Integrator;
import koi.Intersection;
import koi.Light;
import koi.LightSample;
import koi.Material;
import koi.RGB;
import koi.Ray;
import koi.Sampler;
import koi.Scene;
import koi.math.Normal;
import koi.math.OrthonormalBasis;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Vector3D;

public class PathTracingIntegrator extends Integrator {
	
	private int maxDepth;
	
	public PathTracingIntegrator(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	@Override
	public RGB li(Scene scene, Ray ray) {
		if(ray.depth >= maxDepth)
		{
			return new RGB(0.0);
		}
		
		RGB color = new RGB();
		Intersection intersection = new Intersection();
		if(scene.intersect(ray, intersection))
		{
			Point3D hitpoint = intersection.point;
			Vector3D wo = ray.direction.negate();
			Material material = intersection.material;
			Normal normal = new Normal(intersection.normal);
			
			for(Light light : scene.getLights())
			{
				LightSample lightSample = new LightSample();
				light.sample(new Point2D(ThreadLocalRandom.current().nextDouble(), ThreadLocalRandom.current().nextDouble()), lightSample, intersection);
				RGB f = material.F(wo, lightSample.wi, intersection);
				Ray shadowRay = new Ray(hitpoint, lightSample.wi.normalized(), 1e-6d, (lightSample.point.minus(hitpoint)).length());
				Intersection shadowIntersection = new Intersection();

				if(!f.equals(RGB.black) && !material.isType(Type.EMISSIVE) && !isInShadow(scene, shadowRay, lightSample, shadowIntersection))
				{
					color.plusEquals(lightSample.li.times(f).times(Math.max(0.0, lightSample.wi.dot(normal))));
				}
			}
			ray.depth += 1;
			BsdfSample bsdfSample = new BsdfSample();
			RGB f = material.SampleF(wo, bsdfSample, intersection);
			
			if(material.isType(Type.EMISSIVE))
			{
				color.plusEquals(f);
			}

			if(ray.depth < maxDepth)
			{
				if(material.isType(Type.REFLECTIVE))
				{
					color.plusEquals(liSpecular(scene, ray, intersection));
				}
				
				if(material.isType(Type.REFRACTIVE))
				{
					color.plusEquals(liSpecular(scene, ray, intersection));
				}
				
				if(material.isType(Type.GLOSSY, Type.DIFFUSE))
				{
					if(f.equals(RGB.black) || bsdfSample.pdf <= 0.0)
					{
						return new RGB(0.0);
					}
					Ray pathRay = new Ray(intersection.point, new Vector3D(bsdfSample.wo));
					pathRay.depth = ray.depth;
					color.plusEquals(f.times(li(scene, pathRay)).times(Math.abs(bsdfSample.wo.dot(intersection.normal)) / bsdfSample.pdf));
				}
			}
		}
		
		return color;
	}
	
	private RGB reflection(Scene scene, Ray ray, Intersection intersection) {
		RGB color = new RGB();
		if(!scene.intersect(ray, intersection))
		{
			for(Light light : scene.getLights())
			{
				if(light.isInfinite())
				{
					color.plusEquals(light.Le(ray, intersection));
				}
			}
		}
		
		return color;
	}

	private RGB liSpecular(Scene scene, Ray ray, Intersection intersection) {
		Normal normal = intersection.normal;
		Vector3D wi = ray.direction.negate();
		BsdfSample bsdfSample = new BsdfSample();
		RGB f = intersection.material.SampleF(wi, bsdfSample, intersection);
		RGB l = new RGB();
		if(!f.equals(RGB.black) && bsdfSample.pdf != 0.0 && Math.abs(bsdfSample.wo.dot(normal)) != 0.0)
		{
			Ray reflectedRay = new Ray(intersection.point, bsdfSample.wo);
			reflectedRay.depth = ray.depth;
			RGB refl = reflection(scene, reflectedRay, new Intersection());
			l = li(scene, reflectedRay).plus(refl).times(f).times(Math.abs(bsdfSample.wo.normalized().dot(normal)));
			
		}
		return l;
	}
}
