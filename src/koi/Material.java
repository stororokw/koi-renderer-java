package koi;

import java.util.EnumSet;

import koi.Bsdf.Type;
import koi.math.Vector3D;

public abstract class Material {
	
	public abstract RGB F(Vector3D wo, Vector3D wi, Intersection intersection);
	public abstract RGB SampleF(Vector3D wo, BsdfSample bsdfSample, Intersection intersection);
	public abstract double Pdf(Vector3D wo, Vector3D wi);
	public abstract EnumSet<Type> getType();
}
