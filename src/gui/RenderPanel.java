package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import koi.Scene;

@SuppressWarnings("serial")
public class RenderPanel extends JPanel implements TileListener, MouseInputListener, ActionListener {
	
	private Scene scene = null;
	private Map<Long, Rectangle> rectangles = Collections.synchronizedMap(new HashMap<Long, Rectangle>());
	private Rectangle region = null;
	private Point currentPos = null;
	private Tool activeTool = Tool.NONE;
	
	public RenderPanel(Scene scene) {
		this.scene = scene;
		this.setPreferredSize(new Dimension(scene.getBitmap().getWidth() + 100, scene.getBitmap().getHeight() + 100));
		this.setDoubleBuffered(true);
		this.region = new Rectangle(0, 0, scene.getBitmap().getWidth(), scene.getBitmap().getHeight());
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.currentPos = new Point();
		
		setBackground(Color.darkGray);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int imageWidth = scene.getBitmap().getWidth() / 2;
		int imageHeight = scene.getBitmap().getHeight() / 2;
		int left = getWidth()/2 - imageWidth;
		int top = getHeight()/2 - imageHeight;
		int margin = 1;
		
		g2.drawImage(scene.getBitmap().getImage(), left , top, this);
		g2.setColor(Color.white);
		if(activeTool == Tool.REGION)
		{
			g2.drawRect(left + region.x, top + region.y, region.width, region.height);
		}
		synchronized (rectangles) {
			for(Long key : rectangles.keySet())
			{
				Rectangle rectangle = rectangles.get(key);
				g2.drawRect(left + rectangle.x + margin, top + rectangle.y + margin, rectangle.width - margin * 2, rectangle.height - margin * 2);
				g2.drawString("" + key, left + rectangle.x + margin * 2, top + rectangle.y + rectangle.height - margin * 5);
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
	
	public void onStop() {
		synchronized (rectangles) {
			rectangles.clear();
		}
		this.repaint();
	}

	public void regionSelect(int x, int y, int w, int h)
	{
		region.x = x;
		region.y = y;
		region.width = w;
		region.height = h;
		repaint();
	}
	
	public Point topLeft()
	{
		int imageWidth = scene.getBitmap().getWidth() / 2;
		int imageHeight = scene.getBitmap().getHeight() / 2;
		int centerX = getWidth()/2 - imageWidth;
		int centerY = getHeight()/2 - imageHeight;
		
		return new Point(centerX , centerY);
	}
	
	public Rectangle getRegion()
	{
		if(activeTool == Tool.REGION)
			return region;
		return new Rectangle(0, 0, scene.getBitmap().getWidth(), scene.getBitmap().getHeight());
	}
	
	@Override
	public void onComplete() {
		rectangles.clear();
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
		{	
			int imageWidth = scene.getBitmap().getWidth();
			int imageHeight = scene.getBitmap().getHeight();
			int left = getWidth()/2 - imageWidth / 2;
			int top = getHeight()/2 - imageHeight / 2;
			if(activeTool == Tool.REGION)
			{
				if(e.getX() < left)
				{
					currentPos.x = left;
				} else if(e.getX() > left + imageWidth)
				{
					currentPos.x = left + imageWidth;
				} else
				{
					currentPos.x = e.getX();
				}
				
				if(e.getY() < top)
				{
					currentPos.y = top;
				} else if(e.getY() > top + imageHeight)
				{
					currentPos.y = top + imageHeight;
				} else
				{
					currentPos.y = e.getY();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
		if(activeTool == Tool.REGION)
		{
			int imageWidth = scene.getBitmap().getWidth();
			int imageHeight = scene.getBitmap().getHeight();
			int left = getWidth()/2 - imageWidth / 2;
			int top = getHeight()/2 - imageHeight / 2;
			
			int x = e.getX();
			int y = e.getY();

			if(e.getY() > top + imageHeight)
			{
				y = top + imageHeight;
			}
			if(e.getY() < top)
			{
				y = top;
			}
			if(e.getX() < left)
			{
				x = left;
			}
			if(e.getX() > left + imageWidth)
			{
				x = left + imageWidth;
			}

			int x0 = Math.min(currentPos.x, x);
			int y0 = Math.min(currentPos.y, y);
			int w = Math.abs(x - currentPos.x);
			int h = Math.abs(y - currentPos.y);
			regionSelect(x0 - left, y0 - top, w, h);
			RenderPanel.this.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	protected enum Tool
	{
		NONE,
		REGION
	}

	public void toggleRegionTool() {
		if(activeTool == Tool.REGION)
		{
			activeTool = Tool.NONE;
		}
		else
		{
			activeTool = Tool.REGION;
		}
		repaint();
	}
}
