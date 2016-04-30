package koi.geometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import koi.math.Normal;
import koi.math.Point3D;

public class OBJLoader {
	public ArrayList<Point3D> positions = new ArrayList<Point3D>();
	public ArrayList<Point3D> uvws = new ArrayList<Point3D>();
	public ArrayList<Normal> normals = new ArrayList<Normal>();
	public ArrayList<OBJVertex> vertices = new ArrayList<OBJVertex>();

	public OBJLoader(String filepath) {
		
		File file = new File(filepath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			Stream<String> lines = reader.lines();
			Iterator<String> iter = lines.iterator();

			double x;
			double y;
			double z;
			int positionIndex;
			int normalIndex;
			int uvwIndex;
			
			while(iter.hasNext())
			{
				String line = iter.next();
				StringTokenizer tokenizer = new StringTokenizer(line);
				if(line.startsWith("#"))
				{
					continue;
				}
				else if(line.startsWith("vn"))
				{
					tokenizer.nextToken();
					x = Float.parseFloat(tokenizer.nextToken());
					y = Float.parseFloat(tokenizer.nextToken());
					z = Float.parseFloat(tokenizer.nextToken());
					Normal normal = new Normal(x, y, z);
					normals.add(normal);
				}
				else if(line.startsWith("vt"))
				{
					tokenizer.nextToken();
					x = Float.parseFloat(tokenizer.nextToken());
					y = Float.parseFloat(tokenizer.nextToken());
					z = Float.parseFloat(tokenizer.nextToken());
					Point3D uvw = new Point3D(x, y, z);
					uvws.add(uvw);
				}
				else if(line.startsWith("v"))
				{
					tokenizer.nextToken();
					x = Float.parseFloat(tokenizer.nextToken());
					y = Float.parseFloat(tokenizer.nextToken());
					z = Float.parseFloat(tokenizer.nextToken());
					Point3D point = new Point3D(x, y, z);
					positions.add(point);
				} 
				else if(line.startsWith("f"))
				{
					if(line.contains("//"))
					{
						
					}
					else
					{
						tokenizer.nextToken();
						for(int i = 0 ; i < 3; ++i)
						{
							positionIndex = Integer.valueOf(tokenizer.nextToken("/ ").trim()) - 1;
							uvwIndex = Integer.valueOf(tokenizer.nextToken("/ ").trim()) - 1;
							normalIndex = Integer.valueOf(tokenizer.nextToken("/ ").trim()) - 1;
							vertices.add(new OBJVertex(
							positions.get(positionIndex),
							normals.get(normalIndex),
							uvws.get(uvwIndex)));
						}
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

