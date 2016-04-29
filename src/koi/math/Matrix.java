package koi.math;

import koi.Koi;
import koi.Ray;

public class Matrix {
	double[] a;
	
	public Matrix()
	{
		a = new double[16];
		a[0]  = 1.0;
		a[5]  = 1.0;
		a[10] = 1.0;
		a[15] = 1.0;
	}
	
	public Matrix(
			double z, double b, double c, double d, // row 1
			double e, double f, double g, double h, // row 2
			double i, double j, double k, double l, // row 3
			double m, double n, double o, double p) // row 4
	{
		this();
		a[0]  = z;	a[1]  = e; a[2]  = i; a[3]  = m;
		a[4]  = b;	a[5]  = f; a[6]  = j; a[7]  = n;
		a[8]  = c;	a[9]  = g; a[10] = k; a[11] = o;
		a[12] = d;	a[13] = h; a[14] = l; a[15] = p;
	}
	
	public Matrix(Matrix other)
	{
		other.a = this.a.clone();
	}
	
	public Matrix(Vector3D x, Vector3D y, Vector3D z) {
		this();
		a[0]  = x.X;	a[1]  = y.X; 	a[2]  = z.X;	a[3]  = 0;
		a[4]  = x.Y;	a[5]  = y.Y; 	a[6]  = z.Y;	a[7]  = 0;
		a[8]  = x.Z;	a[9]  = y.Z; 	a[10] = z.Z;	a[11] = 0;
		a[12] = 0;		a[13] = 0;		a[14] = 0;		a[15] = 1;
	}

	public static Matrix identity()
	{
		return new Matrix();
	}
	
	public Matrix transpose()
	{
		Matrix m = new Matrix(this);
		for (int y = 0; y < 4; ++y)
		{
			for (int x = 0; x < 4; ++x)
			{
				m.a[x * 4 + y] = this.a[y * 4 + x];
			}
		}
		return m;
	}
	
	public Matrix plus(Matrix other)
	{
		return new Matrix(
				a[0]  + other.a[0],  a[1]  + other.a[1],  a[2]  + other.a[2],  a[3]  + other.a[3],
				a[4]  + other.a[4],  a[5]  + other.a[5],  a[6]  + other.a[6],  a[7]  + other.a[7],
				a[8]  + other.a[8],  a[9]  + other.a[9],  a[10] + other.a[10], a[11] + other.a[11],
				a[12] + other.a[12], a[13] + other.a[13], a[14] + other.a[14], a[15] + other.a[15]);
	}
	
	public Matrix minus(Matrix other)
	{
		return new Matrix(
				a[0]  - other.a[0],  a[1]  - other.a[1],  a[2]  - other.a[2],  a[3]  - other.a[3],
				a[4]  - other.a[4],  a[5]  - other.a[5],  a[6]  - other.a[6],  a[7]  - other.a[7],
				a[8]  - other.a[8],  a[9]  - other.a[9],  a[10] - other.a[10], a[11] - other.a[11],
				a[12] - other.a[12], a[13] - other.a[13], a[14] - other.a[14], a[15] - other.a[15]);
	}
	
	public void plusEquals(Matrix other)
	{
		a[0]  += other.a[0];  a[1]  += other.a[1];  a[2]  += other.a[2];  a[3]  += other.a[3];
		a[4]  += other.a[4];  a[5]  += other.a[5];  a[6]  += other.a[6];  a[7]  += other.a[7];
		a[8]  += other.a[8];  a[9]  += other.a[9];  a[10] += other.a[10]; a[11] += other.a[11];
		a[12] += other.a[12]; a[13] += other.a[13]; a[14] += other.a[14]; a[15] += other.a[15];
	}
	
	public void minusEquals(Matrix other)
	{
		a[0]  -= other.a[0];  a[1]  -= other.a[1];  a[2]  -= other.a[2];  a[3]  -= other.a[3];
		a[4]  -= other.a[4];  a[5]  -= other.a[5];  a[6]  -= other.a[6];  a[7]  -= other.a[7];
		a[8]  -= other.a[8];  a[9]  -= other.a[9];  a[10] -= other.a[10]; a[11] -= other.a[11];
		a[12] -= other.a[12]; a[13] -= other.a[13]; a[14] -= other.a[14]; a[15] -= other.a[15];
	}
	
	public Matrix times(Matrix other)
	{
		Matrix result = new Matrix();
		for (int i = 0; i < 4; ++i)
		{
			for (int y = 0; y < 4; ++y)
			{
				double value = 0;
				for (int x = 0; x < 4; ++x)
				{
					value += a[x * 4 + i] * other.a[y * 4 + x];
				}
				result.a[y * 4 + i] = value;
			}
		}
		return result;
	}
		
	public Matrix times(double scalar)
	{
		Matrix result = new Matrix();
		for (int row = 0; row < 4; ++row) {
			for (int col = 0; col < 4; ++col) {
				result.a[row * 4 + col] = a[row * 4 + col] * scalar;
			}
		}
		return result;
	}
	
	public Vector3D times(Vector3D v)
	{
		double x = v.X * a[0] + v.Y * a[4] + v.Z * a[8];
		double y = v.X * a[1] + v.Y * a[5] + v.Z * a[9];
		double z = v.X * a[2] + v.Y * a[6] + v.Z * a[10];

		return new Vector3D(x, y, z);
	}
	
	public Ray times(Ray ray)
	{
		Vector3D dir = this.times(ray.direction);
		Point3D point = this.times(ray.origin);
		Ray result = new Ray(point, dir);
		result.minT = ray.minT;
		result.maxT = ray.maxT;
		return result;
	}
	
	public double get(int row, int column)
	{
		return a[row * 4 + column];
	}
	
	public void set(int row, int column, double value)
	{
		a[row * 4 + column] = value;
	}
	
	public Matrix inverse()
	{
		double S0 = a[0] * a[5] -
			a[1] * a[4];
		double S1 = a[0] * a[9] -
			a[1] * a[8];
		double S2 = a[0] * a[13] -
			a[1] * a[12];
	
		double S3 = a[4] * a[9] -
			a[5] * a[8];
		double S4 = a[4] * a[13] -
			a[5] * a[12];
		double S5 = a[8] * a[13] -
			a[9] * a[12];
	
		double C0 = a[2] * a[7] -
			a[3] * a[6];
		double C1 = a[2] * a[11] -
			a[3] * a[10];
		double C2 = a[2] * a[15] -
			a[3] * a[14];
	
		double C3 = a[6] * a[11] -
			a[7] * a[10];
		double C4 = a[6] * a[15] -
			a[7] * a[14];
		double C5 = a[10] * a[15] -
			a[11] * a[14];
	
		double det = S0 * C5 - S1 * C4 + S2 * C3 + S3 * C2 - S4 * C1 + S5 * C0;
		if (det == 0.0f)
			System.err.println("Determinant is zero cannot find inverse");
		
		double invDet = 1 / det;
		Matrix adj = new Matrix();
		adj.a[0] = (a[5] * C5 - a[9] * C4 + a[13] * C3) * invDet;
		adj.a[1] = (-a[1] * C5 + a[9] * C2 - a[13] * C1) * invDet;
		adj.a[2] = (a[1] * C4 - a[5] * C2 + a[13] * C0) * invDet;
		adj.a[3] = (-a[1] * C3 + a[5] * C1 - a[9] * C0) * invDet;
	
		adj.a[4] = (-a[4] * C5 + a[8] * C4 - a[12] * C3) * invDet;
		adj.a[5] = (a[0] * C5 - a[8] * C2 + a[12] * C1) * invDet;
		adj.a[6] = (-a[0] * C4 + a[4] * C2 - a[12] * C0) * invDet;
		adj.a[7] = (a[0] * C3 - a[4] * C1 + a[8] * C0) * invDet;
	
		adj.a[8] = (a[7] * S5 - a[11] * S4 + a[15] * S3) * invDet;
		adj.a[9] = (-a[3] * S5 + a[11] * S2 - a[15] * S1) * invDet;
		adj.a[10] = (+a[3] * S4 - a[7] * S2 + a[15] * S0) * invDet;
		adj.a[11] = (-a[3] * S3 + a[7] * S1 - a[11] * S0) * invDet;
	
		adj.a[12] = (-a[6] * S5 + a[10] * S4 - a[14] * S3) * invDet;
		adj.a[13] = (a[2] * S5 - a[10] * S2 + a[14] * S1) * invDet;
		adj.a[14] = (-a[2] * S4 + a[6] * S2 - a[14] * S0) * invDet;
		adj.a[15] = (a[2] * S3 - a[6] * S1 + a[10] * S0) * invDet;
	
		return adj;
	}
	

	public static Matrix rotateX(double rad)
	{
		Matrix result = new Matrix();
		result.a[5]  =  Math.cos(rad);
		result.a[9]  = -Math.sin(rad);
		result.a[6]  =  Math.sin(rad);
		result.a[10] =  Math.cos(rad);
		return result;
	}
	
	public static Matrix rotateY(double rad)
	{
		Matrix result = new Matrix();
		result.a[0]  =  Math.cos(rad);
		result.a[8]  =  Math.sin(rad);
		result.a[2]  = -Math.sin(rad);
		result.a[10] =  Math.cos(rad);
		return result;
	}
	
	public static Matrix rotateZ(double rad)
	{
		Matrix result = new Matrix();
		result.a[0] =  Math.cos(rad);
		result.a[4] = -Math.sin(rad);
		result.a[1] =  Math.sin(rad);
		result.a[5] =  Math.cos(rad);
		return result;
	}
	
	public static Matrix translate(double x, double y, double z)
	{
		Matrix result = new Matrix();
		result.a[12] = x;
		result.a[13] = y;
		result.a[14] = z;
		return result;
	}
	
	public static Matrix scale(double x, double y, double z)
	{
		Matrix result = new Matrix();
		result.a[0]  = x;
		result.a[5]  = y;
		result.a[10] = z;
		return result;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; ++i)
		{
			for (int j = 0; j < 4; ++j)
			{
				sb.append(String.format("%8.2f", a[j * 4 + i]));
			}

			sb.append('\n');
		}

		return sb.toString();
	}

	public static Matrix perspective(double fov, double aspectRatio,
			double zNear, double zFar) {
		Matrix res = new Matrix();
		double t = Math.tan(Koi.DegToRad(fov * 0.5f));
		double sx = 1 / ( t * aspectRatio);
		double sy = 1 / t;
		double sz = (zNear + zFar) / (zNear - zFar);
		double pz = (2 * zFar * zNear) / (zNear - zFar);

		res.a[0] = sx;
		res.a[5] = sy;
		res.a[10] = sz;
		// Vertical FOV
		res.a[11] = -1;
		// Horizontal FOV
	    //res[11] = -1/ar;
		res.a[14] = pz;
		res.a[15] = 0;

		return res;
	}
	
	public static Matrix lookAt(Point3D pos, Point3D target, Vector3D u)
	{
		Vector3D dir = pos.minus(target).hat();
		Vector3D right = dir.cross(u).hat();
		Vector3D up = right.cross(dir).hat();

		Matrix res = new Matrix(right, up, dir);
		Matrix translate;
		translate = Matrix.translate(-pos.X, -pos.Y, -pos.Z);
		return res.times(translate);
	}

	public Point3D times(Point3D v)
	{
		double x = v.X * a[0] + v.Y * a[4] + v.Z * a[8] + a[12];
		double y = v.X * a[1] + v.Y * a[5] + v.Z * a[9] + a[13];
		double z = v.X * a[2] + v.Y * a[6] + v.Z * a[10] + a[14];
		double w = 1 / (v.X * a[3] + v.Y * a[7] + v.Z * a[11] + a[15]);

		return new Point3D(x*w, y*w, z*w);
	}
}

