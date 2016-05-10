package koi;

import java.util.EnumSet;

import koi.math.Point2D;
import koi.math.Vector3D;

public abstract class Bsdf {
	protected EnumSet<Type> type;
	
	public enum Type
	{
		REFLECTIVE,
		REFRACTIVE,
		DIFFUSE,
		GLOSSY,
		EMISSIVE
	}

	public abstract double Pdf(Vector3D wo, Vector3D wi);
	public abstract RGB sample(Vector3D wi, BsdfSample bsdfSample, Intersection intersection);
	
	public boolean isBsdfType(EnumSet<Bsdf.Type> type)
	{
		return this.type.contains(type);
	}
	
	public EnumSet<Bsdf.Type> getType()
	{
		return type;
	}
	
	public abstract RGB F(Vector3D wo, Vector3D wi, Intersection intersection);
}
