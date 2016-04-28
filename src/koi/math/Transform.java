package koi.math;

import koi.Koi;
import koi.Ray;

public class Transform {
	private Matrix m_Transform;
	private Matrix m_InverseTransform;
	
	public Transform()
	{
		m_Transform = new Matrix();
		m_InverseTransform = new Matrix();
	}
	
	public Transform(Matrix matrix)
	{
		m_Transform = new Matrix(matrix);
		m_InverseTransform = matrix.inverse();
	}
	
	public Transform(Matrix matrix, Matrix inverse)
	{
		m_Transform = new Matrix(matrix);
		m_InverseTransform = new Matrix(inverse);
	}
	
	public Transform(Transform t)
	{
		m_Transform = new Matrix(t.m_Transform);
		m_InverseTransform = new Matrix(t.m_InverseTransform);
	}
	
	public Transform inverse()
	{
		return null;
	}
	
	public static Transform Translate(Vector3D v)
	{
		return null;
	}
	
	public static Transform Scale(float x, float y, float z)
	{
		return new Transform(Matrix.scale(x, y, z), Matrix.scale(1/x, 1/y, 1/z));
	}
	
	public static Transform RotateX(float deegrees)
	{
		Matrix m = Matrix.rotateX(Koi.DegToRad(deegrees));
		return new Transform(m, m.transpose());
	}
	
	public static Transform RotateY(float deegrees)
	{
		Matrix m = Matrix.rotateY(Koi.DegToRad(deegrees));
		return new Transform(m, m.transpose());
	}
	
	public static Transform RotateZ(float deegrees)
	{
		Matrix m = Matrix.rotateZ(Koi.DegToRad(deegrees));
		return new Transform(m, m.transpose());
	}
	
	public Matrix GetTransform()
	{
		return m_Transform;
	}
	
	public Matrix GetInverse()
	{
		return m_InverseTransform;
	}
	
	public Transform transform(Transform xform)
	{
		return null;
	}
	
	public Point3D transform(Point3D point)
	{
		return m_Transform.times(point);
	}
	
	public Vector3D transform(Vector3D vector)
	{
		return m_Transform.times(vector);
	}
	
	public Normal transform(Normal normal)
	{
		return new Normal(
				m_InverseTransform.a[0] * normal.X + m_InverseTransform.a[1] * normal.Y + m_InverseTransform.a[2]  * normal.Z,
				m_InverseTransform.a[4] * normal.X + m_InverseTransform.a[5] * normal.Y + m_InverseTransform.a[6]  * normal.Z,
				m_InverseTransform.a[8] * normal.X + m_InverseTransform.a[9] * normal.Y + m_InverseTransform.a[10] * normal.Z);
	}
	
	public Ray transform(Ray ray)
	{
		return m_Transform.times(ray);
	}
	
}