package koi.geometry;

import koi.Geometry;
import koi.Intersection;
import koi.Ray;
import koi.math.BBox;
import koi.math.Normal;
import koi.math.Point3D;
import koi.math.Transform;
import koi.math.Vector3D;

public class Sphere extends Geometry{

	private double radius;
	private BBox bounds;
	private Point3D center;
	
	public Sphere(Transform worldToObject, Transform objectToWorld, double radius) {
		super(worldToObject, objectToWorld);
		this.radius = radius;
		bounds = new BBox();
		center = objectToWorld.transform(new Point3D(0,0,0));
		Point3D point = new Point3D(radius, radius, radius);
		bounds = bounds.plus(center.plus(point));
		bounds = bounds.plus(center.plus(point.negate()));
	}

	@Override
	public boolean intersectRay(Ray ray,
			Intersection intersection) {

		Ray localRay = worldToObject.transform(ray);
		double T;
		Vector3D OC = localRay.origin.minus(center);
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
			intersection.normal = new Normal((p.minus(center).hat()));
			intersection.geometry = this;
//			intersection.basis = OrthonormalBasis::FromW(Vector(intersection.INormal));
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
			intersection.normal = new Normal((p.minus(center).hat()));
			intersection.geometry = this;
//			intersection.basis = OrthonormalBasis::FromW(Vector(intersection.INormal));
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

	
}
