package koi.math;

import java.lang.reflect.Field;
import java.util.Iterator;

public class Vector3D implements Iterable<Vector3D>{
	
	public double X, Y, Z;
	
	public Vector3D()
	{
		this(0.0f, 0.0f, 0.0f);
	}
	
	public Vector3D(Vector3D v)
	{
		this.X = v.X;
		this.Y = v.Y;
		this.Z = v.Z;
	}
	
	public Vector3D(Normal n)
	{
		this.X = n.X;
		this.Y = n.Y;
		this.Z = n.Z;
	}
	
	public Vector3D(double x, double y, double z)
	{
		this.X = x;
		this.Y = y;
		this.Z = z;
	}
	
	public Vector3D(Point3D u) {
		this.X = u.X;
		this.Y = u.Y;
		this.Z = u.Z;
	}

	public Vector3D plus(Vector3D v)
	{
		return new Vector3D(X + v.X, Y + v.Y, Z + v.Z);
	}
	
	public Vector3D plusEquals(Vector3D v)
	{
		X += v.X;
		Y += v.Y;
		Z += v.Z;
		return this;
	}
	
	public Vector3D minus(Vector3D v)
	{
		return new Vector3D(X - v.X, Y - v.Y, Z - v.Z);
	}
	
	public Vector3D minusEquals(Vector3D v)
	{
		X -= v.X;
		Y -= v.Y;
		Z -= v.Z;
		return this;
	}
	
	public Vector3D times(double s)
	{
		return new Vector3D(X * s , Y * s, Z * s);
	}

	public Vector3D timesEquals(float s)
	{
		X *= s;
		Y *= s;
		Z *= s;
		return this;
	}
	
	public Vector3D divide(float s)
	{
		Vector3D result = new Vector3D();
		s = 1 / s;
		result.X *= s;
		result.Y *= s;
		result.Z *= s;
		return result;
	}
	
	public Vector3D divideEquals(float s)
	{
		s = 1 / s;
		X *= s;
		Y *= s;
		Z *= s;
		return this;
	}
	
	public Vector3D negate()
	{
		return new Vector3D(-X, -Y, -Z);
	}
	
	public double dot(Vector3D v)
	{
		return X * v.X + Y * v.Y + Z * v.Z;
	}
	
	public double dot(Normal n)
	{
		return X * n.X + Y * n.Y + Z * n.Z;
	}
	
	public Vector3D cross(Vector3D b)
	{

		return new Vector3D(Y * b.Z - b.Y * Z, b.X * Z - X * b.Z, X * b.Y - Y * b.X);
	}
	
	public Vector3D hat()
	{
		double Lengthsq = X * X + Y * Y + Z * Z;
		double Invlength = 1.0 / Math.sqrt(Lengthsq);
		
		X *= Invlength;
		Y *= Invlength;
		Z *= Invlength;

		return this;
	}

	public Vector3D normalized()
	{
		Vector3D result = new Vector3D(this);
		double Invlength = 1.0 / result.length();
		
		result.X *= Invlength;
		result.Y *= Invlength;
		result.Z *= Invlength;
		return result;
	}
	public double length()
	{
		return Math.sqrt(X * X + Y * Y + Z * Z);
	}
	
	public double lengthSquared()
	{
		return X * X + Y * Y + Z * Z;
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
		sb.append(String.format("Vector3D(%3.2f, %3.2f %3.2f)", X, Y, Z));
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object p)
	{
		Point3D point = (Point3D)p;
		return X == point.X && Y == point.Y && Z == point.Z;
	}

	@Override
	public Iterator<Vector3D> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}
