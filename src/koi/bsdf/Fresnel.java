package koi.bsdf;

import koi.RGB;

public abstract class Fresnel {
	public abstract RGB calculate(double cosTheta);
}
