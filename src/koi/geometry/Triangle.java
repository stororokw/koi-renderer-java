package koi.geometry;

import koi.Geometry;
import koi.GeometrySample;
import koi.Intersection;
import koi.Ray;
import koi.Sampler;
import koi.math.BBox;
import koi.math.Normal;
import koi.math.OrthonormalBasis;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Transform;
import koi.math.Vector3D;

public class Triangle extends Geometry{

	private OBJVertex v0;
	private OBJVertex v1;
	private OBJVertex v2;
	private BBox bounds;
	
	OBJMesh parent;
	
	public Triangle(Transform objectToWorld, Transform worldToObject, OBJVertex v0, OBJVertex v1, OBJVertex v2, OBJMesh mesh)
	{
		super(objectToWorld, worldToObject);
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		bounds = new BBox();
		bounds.plusEquals(v0.position).plusEquals(v1.position).plusEquals(v2.position);
	}
	
	@Override
	public BBox getBounds() {
		return bounds;
	}

	//Moller-Trumbore Triangle-Ray intersection
	//http://www.cs.virginia.edu/~gfx/Courses/2003/ImageSynthesis/papers/Acceleration/Fast%20MinimumStorage%20RayTriangle%20Intersection.pdf
	@Override
	public boolean intersectRay(Ray ray, Intersection intersection) {

		double T;
		Vector3D Edge1 = v1.position.minus(v0.position);
		Vector3D Edge2 = v2.position.minus(v0.position);

		Vector3D PVec = ray.direction.cross(Edge2);

		double Determinant = Edge1.dot(PVec);
		if (Determinant > -ray.minT && Determinant < ray.minT)
			return false;

		double InverseDeterminant = 1.0 / Determinant;
		
		// Distance from P0 to ray Origin
		Vector3D TVec = ray.origin.minus(v0.position);

		double U = TVec.dot(PVec) * InverseDeterminant;
		if (U < 0.0 || U > 1.0)
			return false;
		
		Vector3D QVec = TVec.cross(Edge1);
		double V = ray.direction.dot(QVec) * InverseDeterminant;
		if (V < 0.0 || U + V >  1.0)
			return false;

		T = Edge2.dot(QVec) * InverseDeterminant;
		
		if (T > ray.minT && T < ray.maxT && T < intersection.tHit)
		{
			intersection.tHit = T;
			
		
			Point3D uvw = v0.uvw.times(1 - U - V).plus(v1.uvw.times(U)).plus(v2.uvw.times(V));
			Normal normal = (v0.normal.times(1 - (U + V))).plus((v1.normal.times(U)).plus(v2.normal.times(V))).hat();
			intersection.normal = normal;
//			intersection.normal = new Normal(normal.X * 0.5 + 0.5, normal.Y * 0.5 + 0.5, normal.Z * 0.5 + 0.5);
			intersection.point = ray.on(T);
			intersection.U = uvw.X;
			intersection.V = uvw.Y;
			intersection.geometry = this;
			intersection.basis = OrthonormalBasis.fromW(new Vector3D(normal));
			return true;
		}
		return false;
	}

	@Override
	public void sample(Point2D sample, GeometrySample geometrySample) {
		Point2D uniformPoint = Sampler.uniformTriangle(sample);
		geometrySample.point = v2.position.times(uniformPoint.X)
								.plus(v1.position.times(uniformPoint.Y))
								.plus(v0.position.times(1.0 - uniformPoint.X - uniformPoint.Y));
		geometrySample.normal = new Normal((v1.position.minus(v0.position).cross(v2.position.minus(v0.position)))).hat();
	}

	@Override
	public double getSurfaceArea() {
		return 0.5 * (v1.position.minus(v0.position))
				.cross(v2.position.minus(v0.position))
				.length();
	}
	

}
