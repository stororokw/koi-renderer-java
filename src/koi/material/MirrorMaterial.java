package koi.material;

import java.util.Collections;
import java.util.EnumSet;

import koi.Bsdf.Type;
import koi.BsdfSample;
import koi.Intersection;
import koi.Material;
import koi.RGB;
import koi.bsdf.Fresnel;
import koi.bsdf.LambertianBsdf;
import koi.bsdf.MirrorBsdf;
import koi.math.OrthonormalBasis;
import koi.math.Vector3D;

public class MirrorMaterial extends Material {
	
	private MirrorBsdf mirrorBsdf;
	
	public MirrorMaterial(RGB ks, Fresnel fresnel)
	{
		mirrorBsdf = new MirrorBsdf(ks, fresnel);
	}
	
	@Override
	public RGB F(Vector3D wo, Vector3D wi, Intersection intersection) {
		return mirrorBsdf.F(wo, wi, intersection);
	}

	@Override
	public EnumSet<Type> getType() {
		return mirrorBsdf.getType();
	}

	@Override
	public double Pdf(Vector3D wo, Vector3D wi) {
		return 0;
	}

	@Override
	public RGB SampleF(Vector3D wi, BsdfSample bsdfSample,
			Intersection intersection) {
		wi = OrthonormalBasis.toLocal(intersection.basis, wi);
		RGB f = mirrorBsdf.sample(wi, bsdfSample, intersection);
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
