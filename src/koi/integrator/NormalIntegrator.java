package koi.integrator;


import koi.Integrator;
import koi.Intersection;
import koi.RGB;
import koi.Ray;
import koi.Scene;
import koi.math.Normal;

public class NormalIntegrator extends Integrator{

	@Override
	public RGB li(Scene scene, Ray ray)
	{
		Intersection intersection = new Intersection();
		if(scene.intersect(ray, intersection))
		{
			Normal normal = intersection.normal;
			return new RGB(normal.X, normal.Y, normal.Z).abs();
		}
		return new RGB(0.2, 0.2, 0.2);
	}
}
