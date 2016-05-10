package koi.math;

import koi.Koi;

public class OrthonormalBasis {
	public static double ORTHONORMAL_EPSILON = 1e-6;
	public Vector3D U, V, W;
	
	public OrthonormalBasis()
	{
		U = new Vector3D(1.0, 0.0, 0.0);
		V = new Vector3D(0.0, 1.0, 0.0);
		W = new Vector3D(0.0, 0.0, 1.0);
	}
	
	public OrthonormalBasis(Vector3D u, Vector3D v, Vector3D w)
	{
		U = u;
		V = v;
		W = w;
	}
	
	public static OrthonormalBasis fromW(Vector3D w)
	{
		// Assuming that w is already normalized
		OrthonormalBasis basis = new OrthonormalBasis();
		basis.W = w.hat();
		Vector3D V;
		if (Math.abs(w.X) > Math.abs(w.Z))
		{
			V = new Vector3D(-w.Y, w.X, 0.0);
		}
		else
		{
			V = new Vector3D(0.0, -w.Z, w.Y);
		}
		basis.V = V.hat();
		basis.U = w.cross(V);
		return basis;
	}
	
	public static Vector3D toLocal(OrthonormalBasis basis, Vector3D v)
	{
		return new Vector3D(basis.U.dot(v), basis.V.dot(v), basis.W.dot(v));
	}
	
	public static Vector3D toWorld(OrthonormalBasis basis, Vector3D v)
	{
		return new Vector3D(basis.U.dot(v), basis.V.dot(v), basis.W.dot(v));
	}
	
	public static double cosTheta(Vector3D w)
	{
		return w.Z;
	}
	
	public static double absCosTheta(Vector3D w)
	{
		return Math.abs(w.Z);
	}
	
	public static double sinTheta(Vector3D w)
	{
		return Math.sqrt(sinThetaSquared(w));
	}
	
	public static double sinThetaSquared(Vector3D w)
	{
		double cost = cosTheta(w);
		return (1.0 - cost * cost);
	}
	
	public static double cosPhi(Vector3D w)
	{
		double sinTheta = sinTheta(w);
		if (sinTheta == 0.0)
		{
			return 1.0f;
		}
		double cosPhi = w.X / sinTheta;
		return Koi.clamp(cosPhi, -1.0f, 1.0f);
	}
	
	public static double sinPhi(Vector3D w)
	{
		double sinTheta = sinTheta(w);
		if (sinTheta == 0.0)
		{
			return 0.0;
		}
		double SinPhi = w.Y / sinTheta;
		return Koi.clamp(SinPhi, -1.0f, 1.0f);
	}
}
