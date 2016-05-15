package koi.util;



import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import koi.RGB;

public class Bitmap {
	
	private int width;
	private int height;
	private double gamma;
	private RGB[][] data;

	public Bitmap(int width, int height, double gamma) {
		this.width = width;
		this.height = height;
		data = new RGB[height][width];
		for(RGB[] row : data)
		{
			Arrays.fill(row, new RGB());
		}
	}
	
	public Bitmap(int width, int height)
	{
		this(width, height, 2.2);
	}
	
	public Bitmap(BufferedImage image) {
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.data = new RGB[this.height][this.width];
		int rgb;
		double r, g, b;
		for(int row = 0; row < height; ++row)
		{
			for(int column = 0; column < width; ++column)
			{
				rgb = image.getRGB(column, row);
				r = ((rgb & 0x00ff0000) >> 16) / 255.0;
				g = ((rgb & 0x0000ff00) >> 8) / 255.0;
				b = (rgb & 0x000000ff) / 255.0;
				data[row][column] = new RGB(r, g, b);
			}
		}
	
	}

	public synchronized void setPixel(int x, int y, RGB c)
	{
		data[y][x] = c;
	}
	
	public RGB getPixel(int column, int row)
	{
		return data[row][column];
	}
	
	public int rgb(int column, int row)
	{
		return getPixel(column, row).getRGB();
	}
	
	synchronized
	public void copy(Bitmap b, int x0, int y0, int x1, int y1)
	{
		for(int col = 0; col < x1; ++col)
		{
			for(int row = 0; row < y1; ++row)
			{
				data[y0 + row][x0 + col] = b.getPixel(col, row);
			}
		}
	}
	
	public void clear(Color c)
	{
		for(int col = 0; col < getWidth(); ++col)
		{
			for(int row = 0; row < getHeight(); ++row)
			{
				data[row][col].setRed(c.getRed() / 255.0);
				data[row][col].setGreen(c.getGreen() / 255.0);
				data[row][col].setBlue(c.getBlue() / 255.0);
			}
		}
	}
	
	public void save(String filepath) throws IOException
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int row = 0 ; row < height; ++row)
		{
			for(int column = 0; column < width; ++column)
			{
				image.setRGB(column, row, rgb(column, row));
			}
		}
		File file = new File(filepath);
		ImageIO.write(image, "png", file);
	}
	
	public static Bitmap loadPFM(String filename)
	{
		File file = new File(filename);
		int width, height;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			Stream<String> lines = reader.lines();
			StringBuilder sb = new StringBuilder();
			Iterator<String> iterator = lines.iterator();

			// format
			iterator.next();
			width = Integer.parseInt(iterator.next());
			height = Integer.parseInt(iterator.next());
			//scale factor and endianess(sign)
			float scaleEndianess = Float.parseFloat(iterator.next());
			
			while(iterator.hasNext())
			{
				sb.append(iterator.next()).append("\n");
			}

			Bitmap bitmap = new Bitmap(width, height);
			byte data[] = sb.toString().getBytes();
			FloatBuffer buffer;
			if(scaleEndianess < 0.0f)
				buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
			else
				buffer = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).asFloatBuffer();
			
			scaleEndianess = Math.abs(scaleEndianess);
			
			for (int i = 0; i < height; ++i)
			{
				for (int j = 0; j < width; ++j)
				{
					float r = buffer.get() * scaleEndianess;
					float g = buffer.get() * scaleEndianess;
					float b = buffer.get() * scaleEndianess;
					bitmap.setPixel(j, i, new RGB(r,g,b));
				}
			}
			reader.close();
			return bitmap;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	
		
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
}
