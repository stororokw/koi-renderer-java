package koi;

import koi.math.Matrix;

public abstract class Camera {
	protected Matrix cameraToWorld;
	protected Matrix worldToCamera;

	public abstract Ray calculateRay(double x, double y, double lensX, double lensY, double width, double height);
}
