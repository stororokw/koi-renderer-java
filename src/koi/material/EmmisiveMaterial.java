package koi.material;

import java.util.EnumSet;

import koi.Bsdf.Type;
import koi.BsdfSample;
import koi.Intersection;
import koi.Material;
import koi.RGB;
import koi.math.Vector3D;

public class EmmisiveMaterial extends Material {
	
	private double multiplier;
	private RGB kd;
	
	public EmmisiveMaterial(RGB kd, double multiplier) {
		this.kd = kd;
		this.multiplier = multiplier;
	}
	
	@Override
	public RGB F(Vector3D wo, Vector3D wi, Intersection intersection) {
		return kd.times(multiplier);
	}

	@Override
	public RGB SampleF(Vector3D wo, BsdfSample bsdfSample,
			Intersection intersection) {
		bsdfSample.pdf = 0.0;
		return kd.times(multiplier);
	}

	@Override
	public double Pdf(Vector3D wo, Vector3D wi) {
		return 0.0f;
	}

	@Override
	public EnumSet<Type> getType() {
		return EnumSet.of(Type.EMISSIVE);
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
