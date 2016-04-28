package koi.integrator;


import koi.Integrator;
import koi.Intersection;
import koi.RGB;
import koi.Ray;
import koi.Scene;

public class RayCasterIntegrator extends Integrator{

	@Override
	public RGB li(Scene scene, Ray ray)
	{
		Intersection intersection = new Intersection();
		if(scene.intersect(ray, intersection))
		{
			return new RGB(0, 1, 0);
		}
		return new RGB(1, 0, 0);
	}
}
