package koi.geometry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
			Scanner scanner = new Scanner(file);
			String line;
			double x;
			double y;
			double z;
			while(scanner.hasNext())
			{
				line = scanner.next();
				if(line.startsWith("#"))
				{
					scanner.nextLine();
					continue;
				}
				else if(line.startsWith("vn"))
				{
					x = scanner.nextFloat();
					y = scanner.nextFloat();
					z = scanner.nextFloat();
					Normal normal = new Normal(x, y, z);
					normals.add(normal);
				}
				else if(line.startsWith("vt"))
				{
					x = scanner.nextFloat();
					y = scanner.nextFloat();
					z = scanner.nextFloat();
					Point3D uvw = new Point3D(x, y, z);
					uvws.add(uvw);
				}
				else if(line.startsWith("v"))
				{
					x = scanner.nextDouble();
					y = scanner.nextDouble();
					z = scanner.nextDouble();
					Point3D point = new Point3D(x, y, z);
					positions.add(point);
				} 
				else if(line.startsWith("f"))
				{
					String faceString = scanner.nextLine();
					if(faceString.contains("//"))
					{
						String[] tokens = faceString.split("//");
						for(int i = 0; i < faceString.length(); i +=3)
						{
//							int positionIndex = Integer.valueOf(tokens[i]);
//							int normalIndex = Integer.valueOf(tokens[i + 1]);
//							vertices.add(new OBJVertex(positionIndex, normalIndex, -1));
						}
					}
					else
					{
						Scanner tokenize = new Scanner(faceString);
						while(tokenize.hasNext())
						{
							String[] tokens = tokenize.next().split("/");
							for(int i = 0; i < tokens.length; i +=3)
							{
								int positionIndex = Integer.valueOf(tokens[i].trim()) - 1;
								int uvwIndex = Integer.valueOf(tokens[i + 1].trim()) - 1;
								int normalIndex = Integer.valueOf(tokens[i + 2].trim()) - 1;
//								vertices.add(new OBJVertex(positionIndex, normalIndex, uvwIndex));
								vertices.add(new OBJVertex(
										positions.get(positionIndex),
										normals.get(normalIndex),
										uvws.get(uvwIndex)));
							}
						}
						tokenize.close();
					}
				}
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		long start = System.currentTimeMillis();
		
		OBJLoader loader = new OBJLoader("I:\\[DEMOS]\\scenes\\material\\macbeth.obj");
		double end = (System.currentTimeMillis() - start) / 1000.0;
		System.out.println(end + " s");
	}


}

