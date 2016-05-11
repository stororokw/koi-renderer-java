package koi.bsdf;

import koi.RGB;

public class SchlickFresnel extends Fresnel {
	
	private double ior;
	
	public SchlickFresnel(double ior) {
		this.ior = ior;
	}
	
	@Override
	public RGB calculate(double cosTheta) {
		RGB r_0 = new RGB((1.0 - ior) / (1.0 + ior));
		r_0.timesEquals(r_0);
		return r_0.plus(RGB.white.minus(r_0).times(Math.pow(1.0 - cosTheta, 5.0)));
	}

}
