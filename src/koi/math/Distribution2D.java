package koi.math;

import java.util.ArrayList;
import java.util.List;

import koi.Koi;
import koi.RGB;
import koi.util.Bitmap;

public class Distribution2D {
	private Distribution1D marginalDensities;
	private List<Distribution1D> rowDistributions;
	
	public Distribution2D(double[][] samples)
	{
		this.rowDistributions = new ArrayList<Distribution1D>();
		double columnSum[] = new double[samples.length];
		double sum;
		int index = 0;
		for(double[] row : samples)
		{
			sum = 0.0;
			Distribution1D distribution = new Distribution1D(row);
			rowDistributions.add(distribution);
			for(double column : row)
			{
				sum += column;
			}
			columnSum[index++] = sum;
		}
		marginalDensities = new Distribution1D(columnSum);
	}
	
	public Distribution2D(Bitmap bitmap)
	{
		this.rowDistributions = new ArrayList<Distribution1D>();
		double[][] samples = new double[bitmap.getHeight()][bitmap.getWidth()];
		for (int i = 0; i < bitmap.getHeight(); ++i)
		{
			double[] row = new double[bitmap.getWidth()];
			for (int j = 0; j < bitmap.getWidth(); ++j)
			{
				RGB c = (bitmap.getPixel(j, i));
				row[j] = c.getGreen() + c.getBlue() + c.getRed();
			}
			samples[i] = row;
		}

		double columnSum[] = new double[samples.length];
		double sum;
		int index = 0;
		for(double[] row : samples)
		{
			sum = 0.0;
			Distribution1D distribution = new Distribution1D(row);
			rowDistributions.add(distribution);
			for(double column : row)
			{
				sum += column;
			}
			columnSum[index++] = sum;
		}
		marginalDensities = new Distribution1D(columnSum);
	}
	
	public void sample2D(Point2D sample, Distribution2DSample sample2D)
	{
		Distribution1DSample distributionU = new Distribution1DSample();
		Distribution1DSample distributionV = new Distribution1DSample();
		
		marginalDensities.Sample(sample.Y, distributionV);
		int i = (int) (Koi.clamp(distributionV.probability, 0.0, 1.0) * (rowDistributions.size() - 1)) ;
		rowDistributions.get(i).Sample(sample.X, distributionU);
		sample2D.pdf  = distributionU.pdf * distributionV.pdf;
		sample2D.u = distributionU.probability;
		sample2D.v = distributionV.probability;
	}
}
