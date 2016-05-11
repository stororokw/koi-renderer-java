package koi.integrator;

import koi.Bsdf.Type;
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

public class WhittedIntegrator extends Integrator {
	
	private int maxDepth;
	
	public WhittedIntegrator(int maxDepth) {
		this.maxDepth = maxDepth;
	}
	
	@Override
	public RGB li(Scene scene, Ray ray) {
		RGB color = new RGB();
		Intersection intersection = new Intersection();
		if(scene.intersect(ray, intersection))
		{
			Point3D hitpoint = intersection.point;
			Vector3D wo = ray.direction.negate();
			Material material = intersection.material;
			Normal normal = new Normal(intersection.basis.W);
			
			for(Light light : scene.getLights())
			{
				LightSample lightSample = new LightSample();
				light.sample(new Point2D(Math.random(), Math.random()), lightSample, intersection);
				RGB f = material.F(wo, lightSample.wi, intersection);
				Ray shadowRay = new Ray(hitpoint, lightSample.wi.hat(), 1e-6d, (lightSample.point.minus(hitpoint)).length());
				if(material.isType(Type.EMISSIVE))
				{
					color.plusEquals(light.Le(ray, intersection));
				}
				Intersection shadowIntersection = new Intersection();
				if(!isInShadow(scene, shadowRay, lightSample, shadowIntersection))
				{
					color.plusEquals(lightSample.li.times(f)).timesEquals(Math.abs(lightSample.wi.dot(normal)));
				}
			}
			ray.depth += 1;
			
			if(ray.depth < maxDepth)
			{
				if(material.isType(Type.REFLECTIVE))
				{
					color.plusEquals(liSpecular(scene, ray, intersection));
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
		if(!f.equals(RGB.black) && Math.abs(bsdfSample.wo.dot(normal)) != 0.0)
		{
			Ray reflectedRay = new Ray(intersection.point, bsdfSample.wo.hat());
			reflectedRay.depth = ray.depth;
			l = li(scene, reflectedRay).timesEquals(f).timesEquals(Math.abs(bsdfSample.wo.dot(normal)));
		}
		return l;
	}
}
