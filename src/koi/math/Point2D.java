package koi.math;

public class Point2D {
	
	public double X, Y;
	
	public Point2D()
	{
		this(0.0f, 0.0f);
	}
	
	public Point2D(Point2D v)
	{
		this.X = v.X;
		this.Y = v.Y;
	}
	
	public Point2D(double x, double y)
	{
		this.X = x;
		this.Y = y;
	}
	
	public Point2D plus(Point2D v)
	{
		return new Point2D(X + v.X, Y + v.Y);
	}
	
	public Point2D plusEquals(Point2D v)
	{
		X += v.X;
		Y += v.Y;
		return this;
	}
	
	public Point2D minus(Point2D v)
	{
		return new Point2D(X - v.X, Y - v.Y);
	}
	
	public Point2D minusEquals(Point2D point)
	{
		X -= point.X;
		Y -= point.Y;
		return this;
	}
	
	public Point2D times(double s)
	{
		return new Point2D(X * s , Y * s);
	}

	public Point2D timesEquals(float s)
	{
		X *= s;
		Y *= s;
		return this;
	}
	
	public Point2D divide(float s)
	{
		Point2D result = new Point2D();
		s = 1 / s;
		result.X *= s;
		result.Y *= s;
		return result;
	}
	
	public Point2D divideEquals(float s)
	{
		s = 1 / s;
		X *= s;
		Y *= s;
		return this;
	}
	
	public Point2D negate()
	{
		return new Point2D(-X, -Y);
	}
	
	public double dot(Point2D v)
	{
		return X * v.X + Y * v.Y;
	}
	
	public Point2D hat()
	{
		double Lengthsq = X * X + Y * Y;
		double Invlength = 1.0 / Math.sqrt(Lengthsq);
		
		X *= Invlength;
		Y *= Invlength;

		return this;
	}

	public double distanceTo(Point2D p)
	{
		double dx = p.X - X;
		double dy = p.Y - Y;
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public double DistanceToSquared(Point2D p)
	{
		double dx = p.X - X;
		double dy = p.Y - Y;
		return (dx * dx + dy * dy);
	}
	
	public double at(int index)
	{
		if(index == 0)
			return X;
		else if(index == 1)
			return Y;
		else
			throw new IndexOutOfBoundsException();
	}
	
	@Override
	public boolean equals(Object p)
	{
		Point2D point = (Point2D)p;
		return X == point.X && Y == point.Y;
	}
}
