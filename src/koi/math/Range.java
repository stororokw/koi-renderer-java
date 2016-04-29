package koi.math;

public class Range {
	
	public double t0;
	public double t1;

	public Range()
	{
		this.t0 = Double.NEGATIVE_INFINITY;
		this.t1 = Double.POSITIVE_INFINITY;
	}
	
	public Range(double t0, double t1) {
		this.t0 = t0;
		this.t1 = t1;
	}
}
