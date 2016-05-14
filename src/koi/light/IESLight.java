package koi.light;

import koi.Intersection;
import koi.Koi;
import koi.Light;
import koi.LightSample;
import koi.RGB;
import koi.math.OrthonormalBasis;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Transform;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import javafx.scene.shape.Line;

public class IESLight implements Light{
	
	private Transform localToWorld;
	private Transform worldToLocal;
	private Point3D position;
	private RGB intensity;
	private double multiplier;
	private IESData data;
	
	public IESLight(Transform localToWorld, RGB intensity, double multiplier, String filepath) {
		this.localToWorld = localToWorld;
		this.worldToLocal = localToWorld.inverse();
		this.position = localToWorld.transform(new Point3D(0, 0, 0));
		this.intensity = intensity;
		this.multiplier = multiplier;
		this.data = new IESData();
		loadIES(filepath, data);
	}
	
	@Override
	public Transform getWorldTransform() {
		return localToWorld;
	}

	@Override
	public Transform getLocalTransform() {
		return worldToLocal;
	}

	@Override
	public void sample(Point2D sample, LightSample lightSample,
			Intersection intersection) {
		Point3D intersectionPoint = intersection.point;
		lightSample.point = position;
		lightSample.wi = position.minus(intersectionPoint).hat();
		double attenuation = position.DistanceToSquared(intersection.point);
		
		if (attenuation == 0.0)
		{
			lightSample.li = RGB.black;
			return;
		}

		// calculate angle from ies data
		double costheta = OrthonormalBasis.cosTheta(worldToLocal.transform(lightSample.wi).hat());
		double theta = Math.acos(costheta);
		// Returns an angle between [0, PI]
		double angle = Koi.RadToDeg(theta);

		if (angle >= data.verticalAngles.get(data.verticalAngles.size() - 1))
		{
			lightSample.li = RGB.black;
			return;
		}
		
		// Calculate the angle->index factor into the vertical luminance array
		double increment = data.info.numberOfVerticalAngles / (data.verticalAngles.get(data.verticalAngles.size() - 1) + 1);
		double d1 = data.verticalLuminance.get((int) (angle * increment));
		double luminance = d1;

		lightSample.li = intensity.times(multiplier * luminance / attenuation);
		
	}
	
	@Override
	public boolean isDelta() {
		return true;
	}
	
	private void loadIES(String file, IESData data)
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			StringBuilder sb = new StringBuilder();
			Stream<String> lines = reader.lines();
			Iterator<String> iter = lines.iterator();
			
			while(iter.hasNext())
			{
				String line = iter.next();
				if(line.startsWith("["))
				{
					
				}
				else if(line.startsWith("TILT"))
				{
					
				}
				else if(line.startsWith("IESNA"))
				{
					
				} 
				else
				{
					sb.append(line).append("\n");
				}

			}

			StringTokenizer tokenizer = new StringTokenizer(sb.toString());
			// read information
			IESInformation info = new IESInformation();
			info.numberOfLamps = Integer.parseInt(tokenizer.nextToken());
			info.lumensPerLamp = Double.parseDouble(tokenizer.nextToken());
			info.candelaMultiplier = Double.parseDouble(tokenizer.nextToken());
			info.numberOfVerticalAngles = Integer.parseInt(tokenizer.nextToken());
			info.numberOfHorizontalAngles = Integer.parseInt(tokenizer.nextToken());
			info.photometricType = Integer.parseInt(tokenizer.nextToken());
			info.unitType = Integer.parseInt(tokenizer.nextToken());
			info.width = Double.parseDouble(tokenizer.nextToken());
			info.length = Double.parseDouble(tokenizer.nextToken());
			info.height = Double.parseDouble(tokenizer.nextToken());
			// parse ballast
			double ballastFactor = Double.parseDouble(tokenizer.nextToken());
			double ballastLampPhotometricFactor = Double.parseDouble(tokenizer.nextToken());
			double inputWatt = Double.parseDouble(tokenizer.nextToken());

			//parse angles and luminance
			ArrayList<Double> verticalAngles = new ArrayList<Double>();
			ArrayList<Double> horizontalAngles = new ArrayList<Double>();
			ArrayList<Double> verticalLuminanceValues= new ArrayList<Double>();
			ArrayList<Double> horizontalLuminanceValues = new ArrayList<Double>();

			double temp;
			for(int i = 0; i < info.numberOfVerticalAngles; ++i)
			{
				temp = Double.parseDouble(tokenizer.nextToken());
				verticalAngles.add(temp);
			}
			
			for(int i = 0; i < info.numberOfHorizontalAngles; ++i)
			{
				temp = Double.parseDouble(tokenizer.nextToken());
				horizontalAngles.add(temp);
			}
			
			for(int i = 0; i < info.numberOfVerticalAngles; ++i)
			{
				temp = Double.parseDouble(tokenizer.nextToken());
				verticalLuminanceValues.add(temp);
			}
			
			// Cubic interpolate data
			ArrayList<Double> interpolatedValues = new ArrayList<Double>();
			ArrayList<Double> interpolatedAngles = new ArrayList<Double>();
			int count = 25;
			double t = 0;
			for (int c = 0; c < (info.numberOfVerticalAngles + c) / 4; ++c)
			{
				for (int i = 0; i < count; ++i)
				{
					t = i / (double)count;
					double f = Koi.CubicInterp(t,
						verticalLuminanceValues.get(c * 3	 ),
						verticalLuminanceValues.get(c * 3 + 1),
						verticalLuminanceValues.get(c * 3 + 2),
						verticalLuminanceValues.get(c * 3 + 3));
					double a = Koi.CubicInterp(t,
						verticalAngles.get(c * 3	),
						verticalAngles.get(c * 3 + 1),
						verticalAngles.get(c * 3 + 2),
						verticalAngles.get(c * 3 + 3));
					interpolatedValues.add(f);
					interpolatedAngles.add(a);
				}
			}
			
			info.numberOfVerticalAngles = interpolatedAngles.size();
			data.info = info;
			data.verticalAngles = interpolatedAngles;
			data.verticalLuminance = interpolatedValues;
			data.horizontalAngles = horizontalAngles;
			data.horizontalLuminance = horizontalLuminanceValues;
			
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private class IESData
	{
		IESInformation info;
		List<Double> verticalAngles;
		List<Double> horizontalAngles;
		List<Double> verticalLuminance;
		List<Double> horizontalLuminance;
	}
	
	private class IESInformation
	{
		int numberOfLamps;
		double lumensPerLamp;
		double candelaMultiplier;
		int numberOfVerticalAngles;
		int numberOfHorizontalAngles;
		int photometricType;
		int unitType;
		double width;
		double length;
		double height;
	}
	
}
