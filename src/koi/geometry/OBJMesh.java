package koi.geometry;

import java.util.ArrayList;

import koi.Geometry;
import koi.GeometrySample;
import koi.Intersection;
import koi.Koi;
import koi.Ray;
import koi.math.BBox;
import koi.math.Distribution1D;
import koi.math.Distribution1DSample;
import koi.math.Normal;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Transform;

public class OBJMesh extends Geometry {

	private BBox bounds = null;
	private OBJLoader loader = null;
	private double area;
	private Distribution1D areaDistribution;
	ArrayList<Triangle> worldTriangles = new ArrayList<Triangle>();
	
	public OBJMesh(Transform objectToWorld, Transform worldToObject, String filepath) {
		super(objectToWorld, objectToWorld);
		bounds = new BBox();
		loader = new OBJLoader(filepath);
		
		ArrayList<Point3D> worldPoints = new ArrayList<Point3D>();
		for(Point3D position : loader.positions)
		{
			Point3D worldPoint = objectToWorld.transform(position);
			bounds.plusEquals(worldPoint);
			worldPoints.add(worldPoint);
		}
		ArrayList<OBJVertex> vertices = loader.vertices;
		for(int i = 0; i < vertices.size(); i += 3)
		{
			OBJVertex v1 = vertices.get(i);
			OBJVertex v2 = vertices.get(i + 1);
			OBJVertex v3 = vertices.get(i + 2);
			
			Triangle triangle = new Triangle(
					worldToObject,
					objectToWorld,
					v1, v2, v3, this);
			worldTriangles.add(triangle);
		}
		calculateAreaDistribution();
	}
	
	@Override
	public int getNumberOfGeometries() {
		return worldTriangles.size();
	}

	@Override
	public Geometry getSubGeometry(int i) {
		return worldTriangles.get(i);
	}

	@Override
	public BBox getBounds() {
		return bounds;
	}
	
	@Override
	public boolean isCompound() {
		return true;
	}
	
	@Override
	public boolean intersectRay(Ray ray, Intersection intersection) {
		for (int i = 0; i < worldTriangles.size(); ++i)
		{
			if (worldTriangles.get(i).intersectRay(ray, intersection))
			{
				return true;
			}
		}
		return false;
	}
	
	private void calculateAreaDistribution()
	{
		area = 0.0;
		double[] areas = new double[worldTriangles.size()];
		for(int i = 0; i < worldTriangles.size(); ++i)
		{
			double temp = worldTriangles.get(i).getSurfaceArea();
			area += temp;
			areas[i] = temp;
		}
		areaDistribution = new Distribution1D(areas);
	}

	@Override
	public void sample(Point2D sample, GeometrySample geometrySample) {
		Distribution1DSample distSample = new Distribution1DSample();
		areaDistribution.Sample(sample.X, distSample);
		int index = (int) Koi.clamp(distSample.probability * worldTriangles.size(),
				0.0, worldTriangles.size() - 1);
		worldTriangles.get(index).sample(sample, geometrySample);
	}

	@Override
	public double getSurfaceArea() {
		return area;
	}
	
}
