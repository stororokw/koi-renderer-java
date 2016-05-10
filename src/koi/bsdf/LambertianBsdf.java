package koi.bsdf;

import java.util.EnumSet;

import koi.Bsdf;
import koi.BsdfSample;
import koi.Intersection;
import koi.RGB;
import koi.Sampler;
import koi.math.OrthonormalBasis;
import koi.math.Point2D;
import koi.math.Vector3D;

public class LambertianBsdf extends Bsdf {
	private RGB albedo;
	
	public LambertianBsdf(RGB kd)
	{
		type = EnumSet.of(Type.DIFFUSE);
		albedo = kd;
	}
	
	@Override
	public RGB F(Vector3D wo, Vector3D wi, Intersection intersection) {
		return albedo.divide(Math.PI);
	}

	@Override
	public double Pdf(Vector3D wo, Vector3D wi) {
		return Sampler.cosineHemispherePDF(OrthonormalBasis.cosTheta(wi));
	}

	@Override
	public RGB sample(Vector3D wi, BsdfSample bsdfSample, Intersection intersection) {
		bsdfSample.wo = Sampler.cosineHemisphere(new Point2D(Math.random(), Math.random()));
		return F(bsdfSample.wo, wi, intersection);
	}

}
