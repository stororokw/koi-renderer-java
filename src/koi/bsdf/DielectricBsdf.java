package koi.bsdf;

import java.util.EnumSet;

import koi.Bsdf;
import koi.BsdfSample;
import koi.Intersection;
import koi.Koi;
import koi.RGB;
import koi.Texture;
import koi.math.OrthonormalBasis;
import koi.math.Vector3D;

public class DielectricBsdf extends Bsdf {
	
	double ior;
	Texture diffuse;
	Fresnel fresnel;
	
	public DielectricBsdf(Texture diffuse, double ior) {
		this.diffuse = diffuse;
		this.ior = ior;
		this.type = EnumSet.of(Type.REFRACTIVE);
		this.fresnel = new SchlickFresnel(ior);
	}

	@Override
	public double Pdf(Vector3D wo, Vector3D wi) {
		return 1.0;
	}

	@Override
	public RGB sample(Vector3D wi, BsdfSample bsdfSample,
			Intersection intersection) {
		boolean isEntering = OrthonormalBasis.cosTheta(wi) > 0.0;
		double eta = 1 / ior;
		if(!isEntering)
		{ 
			eta = 1 / eta;
			
		}
		
		double sinI2 = OrthonormalBasis.sinThetaSquared(wi);
		
		double sinT2 = eta * eta * sinI2;
		if(sinT2 >= 1.0)
		{
			bsdfSample.pdf = 1.0;
			bsdfSample.wo = new Vector3D(-wi.X, -wi.Y, wi.Z).hat();
			return new RGB(1.0 / OrthonormalBasis.absCosTheta(bsdfSample.wo));
		}
		
		double cosT = Math.sqrt(Math.max(0.0, 1.0 - sinT2));
		if(isEntering)
		{
			cosT = -cosT;
		}

		bsdfSample.pdf = 1.0;
		bsdfSample.wo = new Vector3D(eta * -wi.X, eta * -wi.Y, cosT);
		return new RGB(1.0).minus(fresnel.calculate(OrthonormalBasis.absCosTheta(wi))).divide(OrthonormalBasis.absCosTheta(bsdfSample.wo));
	}

	@Override
	public RGB F(Vector3D wo, Vector3D wi, Intersection intersection) {
		return new RGB(0.0);
	}

}
