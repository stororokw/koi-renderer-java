package koi.material;

import java.util.EnumSet;




import koi.BsdfSample;
import koi.Intersection;
import koi.Material;
import koi.RGB;
import koi.Texture;
import koi.Bsdf.Type;
import koi.bsdf.DielectricBsdf;
import koi.math.Normal;
import koi.math.OrthonormalBasis;
import koi.math.Vector3D;

public class DielectricMaterial extends Material {
	
	DielectricBsdf bsdf;
	
	public DielectricMaterial(Texture diffuse, double ior) {
		bsdf = new DielectricBsdf(diffuse, ior);
	}

	@Override
	public RGB F(Vector3D wo, Vector3D wi, Intersection intersection) {
		return bsdf.F(wo, wi, intersection);
	}

	@Override
	public EnumSet<Type> getType() {
		return bsdf.getType();
	}

	@Override
	public double Pdf(Vector3D wo, Vector3D wi) {
		return bsdf.Pdf(wo, wi);
	}

	@Override
	public RGB SampleF(Vector3D wi, BsdfSample bsdfSample,
			Intersection intersection) {

		wi = OrthonormalBasis.toLocal(intersection.basis, wi);
		RGB f = bsdf.sample(wi, bsdfSample, intersection);
		bsdfSample.wo = OrthonormalBasis.toWorld(intersection.basis, bsdfSample.wo);
		return f;
	}

	@Override
	public boolean isType(Type... types) {
		EnumSet<Type> set = getType();
		for(Type type : types)
		{
			if(set.contains(type))
			{
				return true;
			}
		}
		return false;
	}
}
