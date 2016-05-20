package koi.light;

import koi.Intersection;
import koi.Koi;
import koi.Light;
import koi.LightSample;
import koi.RGB;
import koi.Ray;
import koi.Texture;
import koi.math.Distribution2D;
import koi.math.Distribution2DSample;
import koi.math.Point2D;
import koi.math.Point3D;
import koi.math.Transform;
import koi.math.Vector3D;
import koi.texture.BitmapTexture;
import koi.util.Bitmap;

public class DomeLight implements Light {

	private Transform localToWorld = null;
	private Transform worldToLocal = null;
	private BitmapTexture texture = null;
	private Distribution2D distribution = null;
	private double multiplier;
	
	public DomeLight(Transform localToWorld, BitmapTexture texture, double multiplier) {
		this.localToWorld = localToWorld;
		this.worldToLocal = localToWorld.inverse();
		this.texture = texture;
		this.distribution = new Distribution2D(texture.getBitmap());
		this.multiplier = multiplier;
	}
	
	@Override
	public boolean isInfinite() {
		return true;
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
	public RGB Le(Ray ray, Intersection intersection) {
		Vector3D localDirection = worldToLocal.transform(ray.direction).hat();
		Point2D uv = Koi.Spherical(localDirection);
		intersection.U = uv.X;
		intersection.V = uv.Y;
		return texture.getValue(intersection);
	}
	
	@Override
	public void sample(Point2D sample, LightSample lightSample,
			Intersection intersection) {
		
		Distribution2DSample distSample = new Distribution2DSample();
		distribution.sample2D(sample, distSample);
		
		double theta = distSample.v * Math.PI;
		double phi = distSample.u * 2.0 * Math.PI;
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		double cosPhi = Math.cos(phi);
		double sinPhi = Math.sin(phi);
		Bitmap bitmap = texture.getBitmap();
		lightSample.wi = localToWorld.transform(new Vector3D(sinTheta * cosPhi, cosTheta, sinTheta * sinPhi)).hat();
		lightSample.point = new Point3D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
		double pdf = distSample.pdf * (bitmap.getWidth() * bitmap.getHeight()) / (2.0 * Math.PI * Math.PI * sinTheta);
		
		if(pdf == 0.0)
		{
			lightSample.li = new RGB(0.0);
			return;
		}
		
		lightSample.li = bitmap.getPixel((int)(distSample.u * bitmap.getWidth()), (int)(distSample.v * bitmap.getHeight())).divide(pdf);
	}

}
