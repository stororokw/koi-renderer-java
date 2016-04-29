package koi.geometry;

import koi.math.Normal;
import koi.math.Point3D;

public class OBJVertex
{
	Point3D position;
	Normal normal;
	Point3D uvw;
	
	public OBJVertex(Point3D position, Normal normal, Point3D uvw)
	{
		this.position = position;
		this.normal = normal;
		this.uvw = uvw;
	}
}