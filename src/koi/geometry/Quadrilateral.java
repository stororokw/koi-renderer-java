package koi.geometry;

import koi.Geometry;
import koi.GeometrySample;
import koi.Intersection;
import koi.Ray;
import koi.math.BBox;
import koi.math.Normal;
import koi.math.OrthonormalBasis;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Transform;
import koi.math.Vector3D;

public class Quadrilateral extends Geometry {

	private double width;
	private double length;
	
	public Quadrilateral(Transform objectToWorld, Transform worldToObject, double length, double width) {
		super(objectToWorld, worldToObject);
		this.width = width;
		this.length = length;
	}

	@Override
	public BBox getBounds() {
		return objectToWorld.transform(new BBox(new Point3D(-length, 0.0, -width), new Point3D(length, 0.0, width)));
	}

	@Override
	public boolean intersectRay(Ray ray, Intersection intersection) {
		Ray localRay = worldToObject.transform(ray);
		double height = 0.0;
		double epsilon = 1e-6d;
		if(Math.abs(localRay.direction.Y) < epsilon)
		{
			return false;
		}
		
		double t = (height - localRay.origin.Y) / localRay.direction.Y;
		if(t < epsilon)
		{
			return false;
		}
		
		Point3D hitpoint = localRay.on(t);
		double distanceX = Math.abs(hitpoint.X);
		double distanceZ = Math.abs(hitpoint.Z);
		if(distanceX > length || distanceZ > width)
		{
			return false;
		}
		
		if(t > ray.minT && t < ray.maxT && t < intersection.tHit)
		{
			intersection.tHit = t;
			double u = (hitpoint.X / length + 1.0) / 2.0;
			double v = 1.0 - (hitpoint.Z / width + 1.0) / 2.0;
			
			intersection.point = hitpoint;
			intersection.geometry = this;
			intersection.normal = objectToWorld.transform(new Normal(0.0, -1.0, 0.0)).hat();
			intersection.basis = OrthonormalBasis.fromW(new Vector3D(intersection.normal));
			intersection.U = u;
			intersection.V = v;
			return true;
		}
		
		return false;
	}

	@Override
	public void sample(Point2D sample, GeometrySample geometrySample) {
		geometrySample.normal = objectToWorld.transform(new Normal(0.0, -1.0, 0.0)).hat();
		geometrySample.point = objectToWorld.transform(new Point3D((2.0 * sample.X - 1.0) * length,
				0, (2.0 * sample.Y - 1.0) * width));
	}

	@Override
	public double getSurfaceArea() {
		return (width + width) * (length + length);
	}

}
