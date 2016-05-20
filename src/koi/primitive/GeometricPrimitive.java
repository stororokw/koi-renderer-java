package koi.primitive;

import java.util.ArrayList;
import java.util.List;

import koi.Geometry;
import koi.Intersection;
import koi.Material;
import koi.Primitive;
import koi.Ray;
import koi.math.BBox;

public class GeometricPrimitive extends Primitive {

	protected Geometry geometry = null;
	protected Material material = null;
	
	public GeometricPrimitive(Geometry geometry, Material material) {
		this.geometry = geometry;
		this.material = material;
	}
	
	@Override
	public boolean intersectRay(Ray ray, Intersection intersection) {
		if(geometry.intersectRay(ray, intersection))
		{
			intersection.material = material;
			intersection.primitive = this;
			return true;
		}
		return false;
	}

	@Override
	public BBox getBounds() {
		return geometry.getBounds();
	}

	@Override
	public List<Primitive> getPrimitives() {
		ArrayList<Primitive> primitives = new ArrayList<Primitive>();
		if (geometry.isCompound())
		{
			for (int i = 0; i < geometry.getNumberOfGeometries(); ++i)
			{
				primitives.add(new GeometricPrimitive(geometry.getSubGeometry(i), material));
			}
		}

		return primitives;
	}
	
	@Override
	public boolean isCompound() {
		return geometry.isCompound();
	}

	@Override
	public boolean isEmissive() {
		//TODO: implement when materials have been added.
		return false;
	}

	@Override
	public Material getMaterial() {
		return material;
	}
}
