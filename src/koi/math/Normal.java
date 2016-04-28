package koi.math;

public class Normal {
	
	public double X, Y, Z;
	
	public Normal()
	{
		this(0.0f, 0.0f, 0.0f);
	}
	
	public Normal(Vector3D v)
	{		
		this.X = v.X;
		this.Y = v.Y;
		this.Z = v.Z;
	}
	
	public Normal(Normal v)
	{
		this.X = v.X;
		this.Y = v.Y;
		this.Z = v.Z;
	}
	
	public Normal(double x, double y, double z)
	{
		this.X = x;
		this.Y = y;
		this.Z = z;
	}
	
	public Normal plus(Vector3D v)
	{
		return new Normal(X + v.X, Y + v.Y, Z + v.Z);
	}
	
	public Normal plus(Normal v)
	{
		return new Normal(X + v.X, Y + v.Y, Z + v.Z);
	}
	
	public Normal plusEquals(Normal v)
	{
		X += v.X;
		Y += v.Y;
		Z += v.Z;
		return this;
	}
	
	public Normal minus(Normal v)
	{
		return new Normal(X - v.X, Y - v.Y, Z - v.Z);
	}
	
	public Normal minusEquals(Normal v)
	{
		X -= v.X;
		Y -= v.Y;
		Z -= v.Z;
		return this;
	}
	
	public Normal times(double s)
	{
		return new Normal(X * s , Y * s, Z * s);
	}

	public Normal timesEquals(float s)
	{
		X *= s;
		Y *= s;
		Z *= s;
		return this;
	}
	
	public Normal divide(float s)
	{
		Normal result = new Normal();
		s = 1 / s;
		result.X *= s;
		result.Y *= s;
		result.Z *= s;
		return result;
	}
	
	public Normal divideEquals(float s)
	{
		s = 1 / s;
		X *= s;
		Y *= s;
		Z *= s;
		return this;
	}
	
	public Normal negate()
	{
		return new Normal(-X, -Y, -Z);
	}
	
	public double dot(Normal v)
	{
		return X * v.X + Y * v.Y + Z * v.Z;
	}
	
	public Normal hat()
	{
		double Lengthsq = X * X + Y * Y + Z * Z;
		double Invlength = 1.0 / Math.sqrt(Lengthsq);
		
		X *= Invlength;
		Y *= Invlength;
		Z *= Invlength;

		return this;
	}

	public double length()
	{
		return Math.sqrt(X * X + Y * Y + Z * Z);
	}
	
	public double lengthSquared()
	{
		return X * X + Y * Y + Z * Z;
	}
	
	@Override
	public boolean equals(Object p)
	{
		Point3D point = (Point3D)p;
		return X == point.X && Y == point.Y && Z == point.Z;
	}
}
