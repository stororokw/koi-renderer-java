package koi;

import koi.math.Point3D;
import koi.math.Vector3D;

public class Ray {
	public final Point3D origin;
	public final Vector3D direction;
	public double minT;
	public double maxT;
	public int depth;
	
	public Ray()
	{
		this.origin = new Point3D(0,0,0);
		this.direction = new Vector3D(0, 0, -1);
	}
	
	public Ray(Point3D origin, Vector3D direction)
	{
		this.origin = origin;
		this.direction = direction;
		this.minT = 1e-5f;
		this.maxT = Double.MAX_VALUE;
	}
	
	public Ray(Point3D origin, Vector3D direction, double minT, double maxT)
	{
		this.origin = origin;
		this.direction = direction;
		this.minT = minT;
		this.maxT = maxT;
	}
	
	public Point3D on(double t)
	{
		return origin.plus(direction.times(t));
	}
	
}
