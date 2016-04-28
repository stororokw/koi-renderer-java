package koi.camera;

import koi.Camera;
import koi.Ray;
import koi.math.Matrix;
import koi.math.Point3D;
import koi.math.Vector3D;

public class PerspectiveCamera extends Camera{
	
	private double fov;
	private double aspectRatio;
	private Matrix projection;
	
	public PerspectiveCamera(Matrix cameraToWorld, double fov, double aspectRatio, double zNear, double zFar)
	{
		this.cameraToWorld = cameraToWorld;
		this.worldToCamera = cameraToWorld.inverse();
		this.fov = fov;
		this.aspectRatio = aspectRatio;
		this.projection = Matrix.perspective(fov, aspectRatio, 0.1, 1000.0).inverse();
	}
	
	@Override
	public Ray calculateRay(double x, double y, double lensX, double lensY,
			double width, double height) {
		double NDCX = x / width;
		double NDCY = y / height;
		// NDC -> Screen transformation
		double ScreenSpaceX = (2 * NDCX - 1);
		double ScreenSpaceY = (2 * NDCY - 1);
		Point3D u = projection.times(new Point3D(ScreenSpaceX, -ScreenSpaceY, 0));
		return cameraToWorld.times(new Ray(new Point3D(0, 0, 0), new Vector3D(u).hat()));
	}

}
