package koi.geometry;

import koi.Geometry;
import koi.GeometrySample;
import koi.Intersection;
import koi.Ray;
import koi.Sampler;
import koi.math.BBox;
import koi.math.Normal;
import koi.math.OrthonormalBasis;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Transform;
import koi.math.Vector3D;

public class Sphere extends Geometry{

	private double radius;
	private BBox bounds;
	private Point3D center;
	
	public Sphere(Transform objectToWorld, Transform worldToObject, double radius) {
		super(objectToWorld, worldToObject);
		this.radius = radius;
		bounds = new BBox();
		center = objectToWorld.transform(new Point3D(0,0,0));
		System.out.println(center);
		Point3D point = new Point3D(radius, radius, radius);
		bounds = bounds.plus(center.plus(point));
		bounds = bounds.plus(center.plus(point.negate()));
	}

	@Override
	public boolean intersectRay(Ray ray,
			Intersection intersection) {

		Ray localRay = worldToObject.transform(ray);
		double T;
		Vector3D OC = new Vector3D(localRay.origin);
		double A = localRay.direction.dot(localRay.direction);
		double B = localRay.direction.times(2.0).dot(OC);
		double C = OC.dot(OC) - (radius * radius);
		double Discriminant = B * B - 4 * A * C;

		if (Discriminant < 0.0)
			return false;

		Discriminant = Math.sqrt(Discriminant);

		T = (-B - Discriminant) / (A + A);
		
		if (T > ray.minT && T < ray.maxT && T < intersection.tHit)
		{
			intersection.tHit = T;
			Point3D p = localRay.on(T);
			double theta = Math.acos(p.Y / radius);
			double phi = Math.atan2(p.Z, p.X);
			if (phi < 0.0)
			{
				phi += 2 * Math.PI;
			}

			double u = phi / (2 * Math.PI);
			double v = (theta / (2 * Math.PI));
			intersection.point = p;
			intersection.normal = new Normal(p.hat());
			intersection.geometry = this;
			intersection.basis = OrthonormalBasis.fromW(new Vector3D(intersection.normal));
			intersection.U = u;
			intersection.V = v;
			return true;
		}
		T = (-B + Discriminant) / (A + A);
		
		if (T > ray.minT && T < ray.maxT && T < intersection.tHit)
		{
			intersection.tHit = T;
			Point3D p = localRay.on(T);
			double theta = Math.acos(p.Y / radius);
			double phi = Math.atan2(p.Z, p.X);
			if (phi < 0.0)
			{
				phi += 2 * Math.PI;
			}

			double u = phi / (2 * Math.PI);
			double v = (theta / (2 * Math.PI));
			intersection.point = p;
			intersection.normal = new Normal(p.hat());
			intersection.geometry = this;
			intersection.basis = OrthonormalBasis.fromW(new Vector3D(intersection.normal));
			intersection.U = u;
			intersection.V = v;
			return true;
		}

		return false;
	}

	@Override
	public BBox getBounds() {
		return bounds;
	}

	@Override
	public void sample(Point2D sample, GeometrySample geometrySample) {
		Vector3D r = Sampler.uniformSphere(sample);
		geometrySample.point = objectToWorld.transform(new Point3D(r.X * radius, r.Y * radius, r.Z * radius));
		geometrySample.normal = objectToWorld.transform(new Normal(r)).hat();
	}

	@Override
	public double getSurfaceArea() {
		return 4.0 * Math.PI * radius * radius;
	}

	
}
