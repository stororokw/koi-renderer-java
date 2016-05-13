package koi.material;

import java.util.EnumSet;

import koi.Bsdf.Type;
import koi.BsdfSample;
import koi.Intersection;
import koi.Material;
import koi.RGB;
import koi.Sampler;
import koi.bsdf.LambertianBsdf;
import koi.math.OrthonormalBasis;
import koi.math.Vector3D;

public class DiffuseMaterial extends Material {
	
	private LambertianBsdf lambertianBsdf;
	
	public DiffuseMaterial(RGB kd)
	{
		lambertianBsdf = new LambertianBsdf(kd);
	}
	
	@Override
	public RGB F(Vector3D wo, Vector3D wi, Intersection intersection) {
		return lambertianBsdf.F(wo, wi, intersection);
	}

	@Override
	public EnumSet<Type> getType() {
		return lambertianBsdf.getType();
	}

	@Override
	public double Pdf(Vector3D wo, Vector3D wi) {
		return lambertianBsdf.Pdf(wo, wi);
	}

	@Override
	public RGB SampleF(Vector3D wi, BsdfSample bsdfSample,
			Intersection intersection) {
		wi = OrthonormalBasis.toLocal(intersection.basis, wi).hat();
		RGB f = lambertianBsdf.sample(wi, bsdfSample, intersection);
		bsdfSample.wo = OrthonormalBasis.toWorld(intersection.basis, bsdfSample.wo).hat();
		return f;
	}

	@Override
	public boolean isType(Type... types) {
		for(Type type : types)
		{
			if(getType().contains(type))
			{
				return true;
			}
		}
		return false;
	}

}
