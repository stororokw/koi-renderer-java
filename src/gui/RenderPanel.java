package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import koi.Scene;
import koi.renderer.Renderer;

@SuppressWarnings("serial")
public class RenderPanel extends JPanel implements TileListener{
	
	private Scene scene = null;
	private Map<Long, Rectangle> rectangles = Collections.synchronizedMap(new HashMap<Long, Rectangle>());
	
	public RenderPanel(Scene scene) {
		this.scene = scene;
		this.setPreferredSize(new Dimension(scene.getBitmap().getWidth(), scene.getBitmap().getHeight()));
		this.setDoubleBuffered(true);
		setBackground(Color.darkGray);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	  
		int imageWidth = scene.getBitmap().getWidth() / 2;
		int imageHeight = scene.getBitmap().getHeight() / 2;
		int centerX = getWidth()/2 - imageWidth;
		int centerY = getHeight()/2 - imageHeight;
		int margin = 1;
		
		g2.drawImage(scene.getBitmap().getImage(), centerX , centerY, this);
		g2.setColor(Color.white);
		
		synchronized (rectangles) {
			for(Long key : rectangles.keySet())
			{
				Rectangle rectangle = rectangles.get(key);
				g2.drawRect(centerX + rectangle.x + margin, centerY + rectangle.y + margin, rectangle.width - margin * 2, rectangle.height - margin * 2);
				g2.drawString("" + key, centerX + rectangle.x + margin * 2, centerY + rectangle.y + rectangle.height - margin * 5);
			}
		}
	}
	
	@Override
	public void onUpdate(long thread) {
		synchronized (rectangles) {
			rectangles.remove(thread);
		};
		this.repaint();
	}

	@Override
	public void onStart(int x0, int y0, int x1, int y1, long thread) {
		rectangles.put(thread, new Rectangle(x0, y0, x1, y1));
		repaint();
	}

	public void clear() {
		scene.getBitmap().clear(Color.black);	
	}

	@Override
	public void onComplete() {
		rectangles.clear();
	}
}
