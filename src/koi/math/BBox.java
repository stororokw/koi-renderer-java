package koi.math;

import koi.Ray;

public class BBox {
	private Point3D min;
	private Point3D max;
	
	public BBox()
	{
		this.min = new Point3D(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);
		this.max = new Point3D(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
	}
	
	public BBox(Point3D point) 
	{
		this.min = point;
		this.max = point;
	}

	public BBox(Point3D min, Point3D max)
	{
		this.min = min;
		this.max = max;
	}
	
	public boolean isOverlap(BBox box) {
		return min.X <= box.max.X && box.max.X <= max.X &&
			min.Y <= box.max.Y && box.max.Y <= max.Y &&
			min.Z <= box.max.Z && box.max.Z <= max.Z;
	}
	
	public boolean isInsideOrAt(Point3D point) {
		return (point.X >= min.X && point.X <= max.X) &&
				(point.Y >= min.Y && point.Y <= max.Y) &&
				(point.Z >= min.Z && point.Z <= max.Z);
	}
	
	public boolean isInside(Point3D point) {
		return (point.X > min.X && point.X < max.X) &&
				(point.Y > min.Y && point.Y < max.Y) &&
				(point.Z > min.Z && point.Z < max.Z);
	}
	public BBox expandBy(double x, double y, double z)
	{
		Vector3D v = new Vector3D(x, y, z);
		Point3D _min = min.plus(v);
		Point3D _max = max.plus(v);
		return new BBox(_min, _max);
	}
	
	public BBox expandBy(double w)
	{
		return expandBy(w, w, w);
	}
	
	public double getVolume()
	{
		return (max.X - min.X) * (max.Y - min.Y) * (max.Z * min.Z);
	}
	
	public double getSurfaceArea()
	{
		double x = max.X - min.X;
		double y = max.Y - min.Y;
		double z = max.Z - min.Z;
		return 2 * (x * y + x * z + z * y);
	}
	
	public Vector3D getExtent()
	{
		return max.minus(min);
	}

	public BBox plus(Point3D point)
	{
		BBox Result = new BBox();
		Result.min.X = Math.min(point.X, min.X);
		Result.min.Y = Math.min(point.Y, min.Y);
		Result.min.Z = Math.min(point.Z, min.Z);

		Result.max.X = Math.max(point.X, max.X);
		Result.max.Y = Math.max(point.Y, max.Y);
		Result.max.Z = Math.max(point.Z, max.Z);

		return Result;
	}
	
	public BBox plusEquals(Point3D point)
	{
		min.X = Math.min(point.X, min.X);
		min.Y = Math.min(point.Y, min.Y);
		min.Z = Math.min(point.Z, min.Z);

		max.X = Math.max(point.X, max.X);
		max.Y = Math.max(point.Y, max.Y);
		max.Z = Math.max(point.Z, max.Z);

		return this;
	}
	
	public BBox plus(BBox box)
	{
		BBox Result = new BBox();
		Result.min.X = Math.min(box.min.X, min.X);
		Result.min.Y = Math.min(box.min.Y, min.Y);
		Result.min.Z = Math.min(box.min.Z, min.Z);

		Result.max.X = Math.max(box.max.X, max.X);
		Result.max.Y = Math.max(box.max.Y, max.Y);
		Result.max.Z = Math.max(box.max.Z, max.Z);

		return Result;
	}
	
    public int LongestAxis()
	{
		Vector3D v = max.minus(min);

		if (v.X  > v.Y && v.X > v.Z)
		{
			return 0;
		}
		
		if(v.Y > v.X && v.Y > v.Z)
		{
			return 1;
		}
		
		return 2;
	}

	public Point3D getCenter()
	{
		return min.plus(max).divide(2.0);
	}
	
	public boolean intersectRay(Ray ray, Range range)
	{

		double tNear = ray.minT;
		double tFar = ray.maxT;

		// calculate intersection with each slab and check intervals
		for (int dimension = 0; dimension < 3; ++dimension)
		{
			double inverseDirection = 1.0 / ray.direction.at(dimension);
			double t0 = (min.at(dimension) - ray.origin.at(dimension)) * inverseDirection;
			double t1 = (max.at(dimension) - ray.origin.at(dimension)) * inverseDirection;
			if (t0 > t1)
			{
				double temp = t0;
				t0 = t1;
				t1 = temp;
			}
			tNear = Math.max(tNear, t0);
			tFar = Math.min(tFar, t1);
			if (tNear > tFar)
			{
				return false;
			}
			if (tFar < 0.0f)
			{
				return false;
			}

		}
		range.t0 = tNear;
		range.t1 = tFar;
		return true;
	}

}
