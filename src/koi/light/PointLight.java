package koi.light;

import koi.Intersection;
import koi.Light;
import koi.LightSample;
import koi.RGB;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Transform;

public class PointLight extends Light {

	private double multiplier;
	private RGB power = null;
	private Point3D position = null;
	
	public PointLight(Transform localToWorld, RGB power) {
		this(localToWorld, power, 1.0);
	}

	public PointLight(Transform localToWorld, RGB power, double multiplier) {
		super(localToWorld);
		this.power = power;
		this.position = localToWorld.transform(new Point3D(0.0, 0.0, 0.0));
		this.multiplier = multiplier;
	}

	@Override
	public void sample(Point2D sample, LightSample lightSample,
			Intersection intersection) {
		lightSample.point = position;
		lightSample.wi = (position.minus(intersection.point)).hat();
		double attenuation = position.DistanceToSquared(intersection.point);
		double pdf = 4.0 * Math.PI;
		lightSample.li = power.times(multiplier).divide(attenuation * pdf);
	}

}
