package koi;

import koi.math.Normal;
import koi.math.OrthonormalBasis;
import koi.math.Point3D;
import koi.primitive.GeometricPrimitive;

public class Intersection {
	
	public Point3D point = null;
	public Normal normal = null;
	public double U;
	public double V;
	public Geometry geometry;
	public Material material;
	public double tHit = Double.MAX_VALUE;
	public OrthonormalBasis basis = null;
	public GeometricPrimitive primitive = null;
}
