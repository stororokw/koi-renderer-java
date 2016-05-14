package koi.math;

import java.util.Arrays;

import koi.Koi;

public class Distribution1D {
	public double[] PDF = null;
	public double[] CDF = null;
		
	public Distribution1D(double[] samples) {
		PDF = new double[samples.length];
		CDF = new double[samples.length];
		double integral = 0.0;
		for(int i = 0; i < samples.length; ++i)
		{
			integral += samples[i];
			PDF[i] = samples[i];
		}
		
		for(int i = 0; i < samples.length; ++i)
		{
			if(integral == 0.0)
			{
				PDF[i] = 0.0;
			} else {
				PDF[i] /= integral;
			}
		}
		
		CDF[0] = 0.0;
		for(int i = 1; i < samples.length; ++i)
		{
			CDF[i] = CDF[i - 1] + PDF[i - 1];
		}
		CDF[CDF.length - 1] = 1.0;
	}
	
	public void Sample(double uniform, Distribution1DSample distributionSample)
	{
		int index = Arrays.binarySearch(CDF, uniform);
		if(index < 0)
		{
			index = (int) Koi.clamp((-index - 1) - 1, 0, CDF.length - 1);
		}
		// find probability at x by linearly interpolating
		double t = (CDF[index + 1] - uniform) / (CDF[index + 1] - CDF[index]);
		distributionSample.probability = (1.0 - t) * index + t * (index + 1);
		distributionSample.probability /= PDF.length;
		distributionSample.pdf = PDF[index];
	}
}
