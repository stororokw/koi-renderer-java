package koi.bsdf;

import java.util.EnumSet;

import koi.Bsdf;
import koi.BsdfSample;
import koi.Intersection;
import koi.RGB;
import koi.math.OrthonormalBasis;
import koi.math.Vector3D;

public class MirrorBsdf extends Bsdf {

	private Fresnel fresnel;
	private RGB specular;
	
	public MirrorBsdf(RGB specular, Fresnel fresnel) {
		this.specular = specular;
		this.fresnel = fresnel;
		this.type = EnumSet.of(Type.REFLECTIVE);
	}
	
	@Override
	public double Pdf(Vector3D wo, Vector3D wi) {
		return 0.0;
	}

	@Override
	public RGB sample(Vector3D wi, BsdfSample bsdfSample,
			Intersection intersection) {
		bsdfSample.pdf = 1.0;
		if(OrthonormalBasis.cosTheta(wi) < 0.0)
		{
			bsdfSample.pdf = 0.0;
		}
		bsdfSample.wo = new Vector3D(-wi.X, -wi.Y, wi.Z).hat();
		return specular.times(fresnel.calculate(OrthonormalBasis.cosTheta(wi)).divide(OrthonormalBasis.absCosTheta(bsdfSample.wo)));
	}

	@Override
	public RGB F(Vector3D wo, Vector3D wi, Intersection intersection) {
		return new RGB(0.0, 0.0, 0.0);
	}

}
