package koi.accelerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import koi.Intersection;
import koi.Material;
import koi.Primitive;
import koi.Ray;
import koi.math.BBox;
import koi.math.Range;

public class BVH extends Primitive{
	
	private BVHNode root = null;
	private ArrayList<Primitive> objects = null;
	private BVHComparator comparator = null;
	
	public BVH(Primitive primitive)
	{
		this.objects = new ArrayList<Primitive>();
		this.objects.add(primitive);
		this.comparator = new BVHComparator(0);

		if (primitive.isCompound())
		{
			objects.addAll(primitive.getPrimitives());
		} 
		else
		{
			objects.add(primitive);
		}
		
		this.root = build();
	}
	
	public BVH(ArrayList<Primitive> primitives) {
		this.objects = new ArrayList<Primitive>();
		this.comparator = new BVHComparator(0);
		for (Primitive primitive : primitives)
		{
			if (primitive.isCompound())
			{
				objects.addAll(primitive.getPrimitives());
			} 
			else
			{
				objects.add(primitive);
			}
		}
		this.root = build();
	}

	public BVHNode build()
	{
		root = new BVHNode();
		return recurse(root, objects, 0);
	}
	
	public BVHNode recurse(BVHNode node, List<Primitive> primitiveList, int depth)
	{
		if(primitiveList.size() <= 5)
		{
			BVHNode leaf = new BVHNode();
			leaf.objects = primitiveList;
			return leaf;
		}
		
		BBox b = new BBox(); 
		for(Primitive primitive : primitiveList)
		{
			b.plusEquals(primitive.getBounds());
		}
		node.bounds = b;

		this.comparator.axis = b.LongestAxis();
		Collections.sort(primitiveList, this.comparator);
		List<Primitive> leftPrimitives = primitiveList.subList(0, primitiveList.size() - primitiveList.size() / 2);
		List<Primitive> rightPrimitives = primitiveList.subList(primitiveList.size() - primitiveList.size() / 2, primitiveList.size());
		node.left = recurse(new BVHNode(), leftPrimitives, depth + 1);
		node.right = recurse(new BVHNode(), rightPrimitives, depth + 1);
		
		return node;
	}

	public boolean intersectRay(BVHNode node, Ray ray, Intersection intersection)
	{
		Range range = new Range();
		if(node.bounds.intersectRay(ray, range) && range.t0 < intersection.tHit)
		{
			if(node.isLeaf())
			{
				boolean hit = false;
				for(Primitive primitive : node.objects)
				{
					if(primitive.intersectRay(ray, intersection))
					{
						hit = true;
					}
				}
				
				return hit;
			}
			

			boolean hitLeft = false;
			boolean hitRight = false;
			
			if(intersectRay(node.left, ray, intersection))
			{
				hitLeft = true;
			}
			if(intersectRay(node.right, ray, intersection))
			{
				hitRight = true;
			}
			
			return hitLeft || hitRight;
		}
		return false;
		
	}
	
	@Override
	public boolean intersectRay(Ray ray, Intersection intersection)
	{
		return intersectRay(root, ray, intersection);
	}
	
	@Override
	public BBox getBounds() {
		return root.bounds;
	}
	
	@Override
	public List<Primitive> getPrimitives() {
		return objects;
	}
	
	private class BVHNode
	{
		BBox bounds = null;
		BVHNode left = null;
		BVHNode right = null;
		List<Primitive> objects = null;
		
		public BVHNode()
		{
			bounds = new BBox();
		}
		
		public boolean isLeaf()
		{
			return left == null && right == null;
		}
	}
	
	private class BVHComparator implements Comparator<Primitive>
	{
		int axis;
		
		public BVHComparator(int axis)
		{
			this.axis = axis;
		}

		@Override
		public int compare(Primitive a, Primitive b) {
			double centerA = a.getBounds().getCenter().at(axis);
			double centerB = b.getBounds().getCenter().at(axis);
			if(centerA < centerB)
			{
				return -1;
			}
			else if( centerA > centerB)
			{
				return 1;
			}
			return 0;
		}

	}

	@Override
	public Material getMaterial() {
		return null;
	}
}
