package koi.math;

public class Point3D {
	
	public double X, Y, Z;
	
	public Point3D()
	{
		this(0.0f, 0.0f, 0.0f);
	}
	
	public Point3D(Point3D v)
	{
		this.X = v.X;
		this.Y = v.Y;
		this.Z = v.Z;
	}
	
	public Point3D(double x, double y, double z)
	{
		this.X = x;
		this.Y = y;
		this.Z = z;
	}
	
	public Point3D plus(Point3D v)
	{
		return new Point3D(X + v.X, Y + v.Y, Z + v.Z);
	}
	
	public Point3D plus(Vector3D v)
	{
		return new Point3D(X + v.X, Y + v.Y, Z + v.Z);
	}
	
	public Point3D plusEquals(Point3D v)
	{
		X += v.X;
		Y += v.Y;
		Z += v.Z;
		return this;
	}
	
	public Vector3D minus(Point3D v)
	{
		return new Vector3D(X - v.X, Y - v.Y, Z - v.Z);
	}
	
	public Point3D minusEquals(Point3D v)
	{
		X -= v.X;
		Y -= v.Y;
		Z -= v.Z;
		return this;
	}
	
	public Point3D times(double s)
	{
		return new Point3D(X * s , Y * s, Z * s);
	}

	public Point3D timesEquals(double s)
	{
		X *= s;
		Y *= s;
		Z *= s;
		return this;
	}
	
	public Point3D divide(double s)
	{
		Point3D result = new Point3D(this);
		s = 1 / s;
		result.X *= s;
		result.Y *= s;
		result.Z *= s;
		return result;
	}
	
	public Point3D divideEquals(double s)
	{
		s = 1 / s;
		X *= s;
		Y *= s;
		Z *= s;
		return this;
	}
	
	public Point3D negate()
	{
		return new Point3D(-X, -Y, -Z);
	}
	
	public double dot(Point3D v)
	{
		return X * v.X + Y * v.Y + Z * v.Z;
	}
	
	public Point3D hat()
	{
		double Lengthsq = X * X + Y * Y + Z * Z;
		double Invlength = 1.0 / Math.sqrt(Lengthsq);
		
		X *= Invlength;
		Y *= Invlength;
		Z *= Invlength;

		return this;
	}

	public double distanceTo(Point3D p)
	{
		double dx = p.X - X;
		double dy = p.Y - Y;
		double dz = p.Z - Z;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	
	public double DistanceToSquared(Point3D p)
	{
		double dx = p.X - X;
		double dy = p.Y - Y;
		double dz = p.Z - Z;
		return (dx * dx + dy * dy + dz * dz);
	}
	
	public double at(int index)
	{
		if(index == 0)
			return X;
		else if(index == 1)
			return Y;
		else if(index == 2)
			return Z;
		else
			throw new IndexOutOfBoundsException();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Point3D(%3.2f, %3.2f %3.2f)", X, Y, Z));
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object p)
	{
		Point3D point = (Point3D)p;
		return X == point.X && Y == point.Y && Z == point.Z;
	}
}
