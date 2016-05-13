package koi;

import koi.math.OrthonormalBasis;
import koi.math.Point2D;
import koi.math.Vector3D;

public class Sampler {
	
	public static Vector3D cosineHemisphere(Point2D sample)
	{
		double z = Math.sqrt(sample.X);
		double r = Math.sqrt(Math.max(0.0, 1.0 - z * z));
		double phi = 2.0 * Math.PI * sample.Y;
		double x = r * Math.cos(phi);
		double y = r * Math.sin(phi);
		return new Vector3D(x, y, z);
	}
	
	public static double cosineHemispherePDF(double cosTheta)
	{
		return cosTheta / Math.PI ;
	}
	
	public static Vector3D uniformHemisphere(Point2D sample)
	{
		double z = sample.X;
		double r = Math.sqrt(Math.max(0.0, 1.0 - z * z));
		double phi = 2.0 * Math.PI * sample.Y;
		double x = r * Math.cos(phi);
		double y = r * Math.sin(phi);
		return new Vector3D(x, y, z);
	}
	
	public static double uniformHemispherePDF()
	{
		return 0.5 / Math.PI;
	}
	
	public static Vector3D uniformSphere(Point2D sample)
	{
		double z = sample.X * 2.0 - 1;
		double r = Math.sqrt(Math.max(0.0, 1.0 - z * z));
		double phi = 2.0 * Math.PI * sample.Y;
		double x = r * Math.cos(phi);
		double y = r * Math.sin(phi);
		return new Vector3D(x, y, z);
	}
	
	public static double uniformSpherePDF()
	{
		return 0.25 / Math.PI;
	}
	
	public static Point2D uniformTriangle(Point2D sample)
	{
		double s = Math.sqrt(sample.X);
		double x = 1.0 - s;
		double y = sample.Y * s;
		return new Point2D(x, y);
	}
	
	public static Vector3D beckmann(Point2D sample, double alpha) {
		double theta = Math.atan(Math.sqrt((-alpha*alpha) * Math.log(1 - sample.X)));
		double z = Math.cos(theta);
		double r = Math.sqrt(Math.max(0.0, 1.0 - z * z));
		double phi = 2.0f * Math.PI * sample.Y;
		double x = r * Math.cos(phi);
		double y = r * Math.sin(phi);

		return new Vector3D(x, y, z);
	}
	
	public static double BeckmannPDF(Vector3D m, double alpha) {
		double costheta = OrthonormalBasis.cosTheta(m);
		if (costheta > 0.0)
		{
			double theta = Math.acos(m.Y);
			double t = -Math.pow(Math.tan(theta), 2.0) / (alpha * alpha);
			double e = Math.exp(t);
			double d = Math.PI * alpha * alpha * Math.pow(costheta, 4.0);
			return (e / d) * costheta;
		}
		
		return 0.0;
	}

	public static Vector3D phong(Point2D sample, double alpha) {
		double theta = Math.acos(Math.pow(sample.X, 1.0 / (alpha + 2.0)));
		double z = Math.cos(theta);
		double r = Math.sqrt(Math.max(0.0, 1.0 - z * z));
		double phi = 2.0 * Math.PI * sample.Y;
		double x = r * Math.cos(phi);
		double y = r * Math.sin(phi);

		return new Vector3D(x, y, z);
	}
	
	static double PhongPDF(Vector3D m, double alpha)
	{
		double costheta = OrthonormalBasis.cosTheta(m);
		if (costheta > 0.0f)
		{
			double a = (alpha + 2.0) / (2.0 * Math.PI);
			return a * costheta * Math.pow(costheta, alpha);
		}
		return 0.0;
	}
	
	public static Vector3D GGX(Point2D sample, double alpha)
	{
		double theta = Math.atan(alpha * Math.sqrt(sample.X) / Math.sqrt(1.0 - sample.X));
		double phi = 2.0 * Math.PI * sample.X;
		double z = Math.cos(theta);
		double sintheta = Math.sqrt(Math.max(0.0, 1.0 - z * z));
		double x = sintheta * Math.cos(phi);
		double y = sintheta * Math.sin(phi);
		return new Vector3D(x, y, z);
	}

	static double GGXPDF(Vector3D m, double alpha)
	{
		if (m.Y > 0.0)
		{
			double costheta = m.Z;
			double alpha2 = alpha * alpha;
			double e = alpha2 + Math.pow(Math.tan(Math.acos(costheta)), 2.0);
			double d = Math.PI * Math.pow(costheta, 3.0) * e * e;
			return alpha2 / d;
		}
		return 0.0;
	}
}
