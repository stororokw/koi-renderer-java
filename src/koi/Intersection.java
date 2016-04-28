package koi;

import koi.math.Normal;
import koi.math.Point3D;

public class Intersection {
	
	public Point3D point = null;
	public Normal normal = null;
	public double U;
	public double V;
	public Geometry geometry;
	public double tHit = Double.MAX_VALUE;
}
