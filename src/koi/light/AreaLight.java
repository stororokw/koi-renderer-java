package koi.light;

import koi.Geometry;
import koi.GeometrySample;
import koi.Intersection;
import koi.Light;
import koi.LightSample;
import koi.Material;
import koi.RGB;
import koi.Ray;
import koi.math.Normal;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Transform;
import koi.math.Vector3D;
import koi.primitive.GeometricPrimitive;

public class AreaLight extends GeometricPrimitive implements Light {
	
	private Transform localToWorld = null;
	private Transform worldToLocal = null;
	private RGB radiance;
	
	public AreaLight(Transform localToWorld, Geometry geometry, Material material, RGB radiance) {
		super(geometry, material);
		this.localToWorld = localToWorld;
		this.worldToLocal = localToWorld.inverse();
		this.radiance = radiance;
	}
	
	public RGB L(Point3D point, Normal normal, Vector3D wo, Intersection intersection)
	{
		if(wo.dot(normal) > 0.0)
		{
			return radiance.times(material.F(wo, point.minus(intersection.point).hat(), intersection));
		}
		return RGB.black;
	}
	
	@Override
	public RGB Le(Ray ray, Intersection intersection) {
		Vector3D wi = intersection.point.minus(ray.origin).hat();
		return material.F(ray.direction.negate(), wi, intersection);
	}
	
	@Override
	public boolean isArea() {
		return true;
	}

	@Override
	public Transform getWorldTransform() {
		return localToWorld;
	}

	@Override
	public Transform getLocalTransform() {
		return worldToLocal;
	}

	@Override
	public void sample(Point2D sample, LightSample lightSample,
			Intersection intersection) {
		GeometrySample geometrySample = new GeometrySample();
		geometry.sample(sample, geometrySample);
		lightSample.point = geometrySample.point;
		lightSample.wi = geometrySample.point.minus(intersection.point).hat();
		double pdf = geometrySample.point.DistanceToSquared(intersection.point) /
				(Math.abs(lightSample.wi.negate().dot(geometrySample.normal)) * geometry.getSurfaceArea());
		lightSample.li = L(geometrySample.point, geometrySample.normal, lightSample.wi.negate(), intersection).divide(pdf);
	}
}
