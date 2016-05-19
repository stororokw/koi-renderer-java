package koi;


import koi.math.Matrix;
import koi.math.Normal;
import koi.math.Point2D;
import koi.math.Vector3D;

public final class Koi {
	
	private Koi()
	{
		
	}
	
	public static double clamp(double value, double min, double max)
	{
		if(value < min)
			return min;
		if(value > max)
			return max;
		return value;
	}
	
	public static double DegToRad(double degrees)
	{
		return degrees * Math.PI / 180.0;
	}

	public static double RadToDeg(double radians)
	{
		return (radians * 180.0) / Math.PI;
	}

	public static double CubicInterp(double t, double x0, double x1, double x2, double x3)
	{
		double a0 = (1 - t) * (1 - t) * (1 - t);
		double a1 = 3 * t * (1 - t) * (1 - t);
		double a2 = 3 * t * t * (1 - t);
		double a3 = t * t * t;
		return a0*x0 + a1*x1 + a2*x2 + a3*x3;
	}

	// Helper functions for converting from spherical to canonical coordinates
	// See page 291 of Physically based rendering
	public static Vector3D SphericalDirection(float theta, float phi)
	{
		double sintheta = Math.sin(theta);
		double costheta = Math.cos(theta);
		return new Vector3D(sintheta * Math.cos(phi),
					  sintheta * Math.sin(phi),
					  costheta);

	}
	
	public static Point2D Spherical(Vector3D v)
	{
		double phi = SphericalPhi(v);
		double theta = SphericalTheta(v);
		double U = phi / (2.0 * Math.PI);
		double V = theta / Math.PI;
		return new Point2D(U, V);
	}

	// Conversion from canonical coordinates to spherical coordinates theta, phi
	// See page 291 of Physically based rendering
	public static double SphericalTheta(Vector3D v)
	{
		return Math.acos(clamp(-v.Y, -1.0, 1.0));
	}

	public static double SphericalPhi(Vector3D v)
	{
		double p = Math.atan2(v.Z, v.X);
		return (p < 0.0) ? p + 2.0 * Math.PI : p; 
	}

	// Helper function to reflect a Vector v about a Normal n.
	public static Vector3D Reflect(Vector3D v, Normal n)
	{
		return new Vector3D(n.times(2 * v.dot(n))).minus(v);
	}

	// The default 3dsmax camera Axis order: XYZ RotationAxis: Z
	// The default 3dsmax camera points down the z-axis
	// From Max->System
	// 1 For both translation and rotation
//		1.1 Negate Y
//		1.2 Swap Y with Z
	// 3. Subtract 90 deg rotation from X axis rotation
	// 4. Swap Y and Z order in multiplication.
	public static Matrix XYZMax(Vector3D translation, Vector3D rotation, Vector3D scale)
	{
		Matrix CameraToWorld;
		Matrix z = Matrix.rotateZ(DegToRad(-rotation.Y));
		Matrix y = Matrix.rotateY(DegToRad(rotation.Z));
		Matrix x = Matrix.rotateX(DegToRad(rotation.X));
		Matrix t = Matrix.translate(translation.X, translation.Z, -translation.Y);
		Matrix s = Matrix.scale(scale.X, scale.Y, scale.Z);
		// note the order is applied in reverse first x.
		CameraToWorld = t.times(y).times(z).times(x).times(s);
		return CameraToWorld;
	}

	public static Matrix XYZMaxCamera(Vector3D translation, Vector3D rotation)
	{
		Matrix CameraToWorld;
		Matrix z = Matrix.rotateZ(DegToRad(-rotation.Y));
		Matrix y = Matrix.rotateY(DegToRad(rotation.Z));
		Matrix x = Matrix.rotateX(DegToRad(rotation.X - 90.0f));
		Matrix t = Matrix.translate(translation.X, translation.Z, -translation.Y);
		// note the order is applied in reverse first x.
		CameraToWorld = t.times(y).times(z).times(x);
		return CameraToWorld;
	}

	public static RGB BilinearInterp(RGB s, RGB t, RGB uv00, RGB uv10,
			RGB uv01, RGB uv11) {
		// interpolate across u to s
		RGB a = uv00.times(RGB.white.minus(s)).plus(uv10.times(s));
		RGB b = uv01.times(RGB.white.minus(s)).plus(uv11.times(s));
		// linearly interpolate across v between a and b
		return a.times(RGB.white.minus(t)).plus(b.times(t));
	}

}
