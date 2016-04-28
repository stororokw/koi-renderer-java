package koi.util;



import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import koi.RGB;

public class Bitmap {
	private BufferedImage image;
	private WritableRaster raster;
	private int[] buffer;
	
	public Bitmap(int width, int height)
	{
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		raster = image.getRaster();
		buffer =((DataBufferInt)raster.getDataBuffer()).getData();
	}
	
	public synchronized void setPixel(int x, int y, RGB c)
	{
		image.setRGB(x, y, c.getRGB());
	}
	
	synchronized
	public void copy(Bitmap b, int x0, int y0, int x1, int y1)
	{
		for(int col = 0; col < x1; ++col)
		{
			for(int row = 0; row < y1; ++row)
			{
				image.setRGB(x0 + col, y0 + row, b.image.getRGB(col, row));
			}
		}
	}
	
	public void clear(Color c)
	{
		Arrays.fill(buffer, c.getRGB());
	}
	
	public void save(String filepath) throws IOException
	{
		File file = new File(filepath);
		ImageIO.write(image, "png", file);
	}
	
	public BufferedImage getImage()
	{
		return image;
	}

	public int getWidth()
	{
		return image.getWidth();
	}
	
	public int getHeight()
	{
		return image.getHeight();
	}
	
}
