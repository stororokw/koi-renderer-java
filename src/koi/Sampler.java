package koi;

import koi.math.Point2D;
import koi.math.Vector3D;

public class Sampler {
	
	public static Vector3D cosineHemisphere(Point2D sample)
	{
		double z = Math.sqrt(sample.X);
		double r = Math.sqrt(Math.max(0.0, 1.0 - z * z));
		double phi = 2.0f * Math.PI * sample.Y;
		double x = r * Math.cos(phi);
		double y = r * Math.sin(phi);
		return new Vector3D(x, y, z);
	}
	
	public static double cosineHemispherePDF(double cosTheta)
	{
		return cosTheta / Math.PI ;
	}
}
