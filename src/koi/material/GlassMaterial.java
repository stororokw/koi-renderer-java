package koi.material;

import java.util.EnumSet;






import java.util.concurrent.ThreadLocalRandom;

import koi.BsdfSample;
import koi.Intersection;
import koi.Material;
import koi.RGB;
import koi.Texture;
import koi.Bsdf.Type;
import koi.bsdf.DielectricBsdf;
import koi.bsdf.Fresnel;
import koi.bsdf.MirrorBsdf;
import koi.bsdf.SchlickFresnel;
import koi.math.OrthonormalBasis;
import koi.math.Vector3D;

public class GlassMaterial extends Material {
	
	DielectricBsdf bsdfDielectric;
	MirrorBsdf bsdfMirror;
	Fresnel fresnel;
	
	public GlassMaterial(Texture diffuse, double ior) {
		fresnel = new SchlickFresnel(ior);
		bsdfDielectric = new DielectricBsdf(diffuse, ior);
		bsdfMirror = new MirrorBsdf(new RGB(1), new SchlickFresnel(ior));
	}

	@Override
	public RGB F(Vector3D wo, Vector3D wi, Intersection intersection) {
		return bsdfDielectric.F(wo, wi, intersection);
	}

	@Override
	public EnumSet<Type> getType() {
		return bsdfDielectric.getType();
	}

	@Override
	public double Pdf(Vector3D wo, Vector3D wi) {
		return 1.0;
	}

	@Override
	public RGB SampleF(Vector3D wi, BsdfSample bsdfSample,
			Intersection intersection) {
		wi = OrthonormalBasis.toLocal(intersection.basis, wi).hat();
		RGB fr = fresnel.calculate(OrthonormalBasis.absCosTheta(wi));
		double prReflection = fr.average();
		double u = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
		RGB f = new RGB();
		if(u < 1 - prReflection)
		{
			f = bsdfDielectric.sample(wi, bsdfSample, intersection);
		} else
		{
			f = bsdfMirror.sample(wi, bsdfSample, intersection);
		}
		bsdfSample.wo = OrthonormalBasis.toWorld(intersection.basis, bsdfSample.wo).hat();
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
