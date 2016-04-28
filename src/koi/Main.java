package koi;

import gui.RenderWindow;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import koi.geometry.Sphere;
import koi.integrator.RayCasterIntegrator;
import koi.math.Matrix;
import koi.math.Transform;
import koi.math.Vector3D;
import koi.renderer.TileRenderer;
import koi.util.Bitmap;



public class Main {
	public static void main(String args[]) throws IOException {
		int width = 450;
		int height = 500;
		
		Bitmap bitmap = new Bitmap(width, height);
		Matrix m = Koi.XYZMax(new Vector3D(104.331, 104.331, 86.614), new Vector3D(62, 0, 135));

		Camera camera = new koi.camera.PerspectiveCamera(m, 15.805, width / (float)height, 0.1, 1000.0f);
		Integrator integrator = new RayCasterIntegrator();
		Geometry sphere = new Sphere(new Transform(),new Transform(), 5.0);
		Scene scene = new Scene();

		scene.addGeometry(sphere);
		scene.setCamera(camera);
		scene.setSamples(16);
		scene.setIntegrator(integrator);
		scene.setBitmap(bitmap);
	  	TileRenderer renderer = new TileRenderer(scene, bitmap, 32);
		SwingUtilities.invokeLater(new Runnable() {
		      @Override
		      public void run() {
		            try 
		            {
		            	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		            	
		            }
		            catch(Exception e)
		            {
		            	e.printStackTrace();
		            }
					RenderWindow window = new RenderWindow(scene, renderer);
					window.setLocationRelativeTo(null);
					window.setLocationByPlatform(true);
					window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					window.setVisible(true);
		      }
		 });

	}
}
