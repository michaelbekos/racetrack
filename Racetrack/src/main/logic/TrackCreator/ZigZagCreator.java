package src.main.logic.TrackCreator;

import java.util.ArrayList;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import src.main.java.logic.AIstar.LineSegment;
import src.main.java.logic.AIstar.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.System;

public class ZigZagCreator
{
	public static Random generator = new Random();
	public static void main(String[] args)
	{
		generator.setSeed(System.currentTimeMillis());
		if (args.length != 4)
		{
			System.out.println("Usage: ZigZagCreator width maxLength minLength maxNumberOfTurns");
		}
		else
		{
			int width = Integer.parseInt(args[0]);
			int maxLength = Integer.parseInt(args[1]);
			int minLength = Integer.parseInt(args[2]);
			int maxNumberOfTurns = Integer.parseInt(args[3]);
			if (width < 1 || maxLength < 1 || minLength < 1 || maxNumberOfTurns < 1)
			if (minLength < width)
			{
				minLength = width;
			}
			if (minLength > maxLength)
			{
				int swap = minLength;
				minLength = maxLength;
				maxLength = swap;
			}
			writeZigZagXML(width, maxLength, minLength, maxNumberOfTurns);
			System.out.println("Track generation done.");
		}
	}
	
	private enum Direction 
	{
		LEFT, RIGHT, UP, DOWN 
	}
	
	private static void writeZigZagXML(int width, int maxLength, int minLength, int maxNumberOfTurns)
	{
		ZigZagDimensions zigZagDim = new ZigZagDimensions(0, width+1, 0, width+1);
		Direction currentDirection = Direction.RIGHT;
		ArrayList<Point> outerBoundaryPoints = new ArrayList<Point>();
		ArrayList<Point> innerBoundaryPoints = new ArrayList<Point>();
		ArrayList<Point> startingPositions = new ArrayList<Point>();
		ArrayList<LineSegment> boundaries = new ArrayList<LineSegment>();
		outerBoundaryPoints.add(new Point(1,0));
		outerBoundaryPoints.add(new Point(0,0));
		outerBoundaryPoints.add(new Point(0,width+1));
		outerBoundaryPoints.add(new Point(width+1,width+1));
		innerBoundaryPoints.add(new Point(1,0));
		innerBoundaryPoints.add(new Point(width+1,0));
		for (int i = 1; i <= width; i++)
		{
			startingPositions.add(new Point(1,i));
		}
		boundaries.add(new LineSegment(outerBoundaryPoints.get(0),outerBoundaryPoints.get(1)));
		boundaries.add(new LineSegment(outerBoundaryPoints.get(1),outerBoundaryPoints.get(2)));
		boundaries.add(new LineSegment(outerBoundaryPoints.get(2),outerBoundaryPoints.get(3)));
		boundaries.add(new LineSegment(innerBoundaryPoints.get(0),innerBoundaryPoints.get(1)));
		for(int turns = 0; turns < maxNumberOfTurns; turns++)
		{
			if (!addStraightLine(maxLength, minLength, outerBoundaryPoints, innerBoundaryPoints,	boundaries, zigZagDim, currentDirection))
			{
				break;
			}
			Direction newDirection = Direction.UP;
			int random = generator.nextInt(2);
			switch (currentDirection)
			{
				case UP:
				{
					if (random == 0)
					{
						newDirection = Direction.LEFT;
					}
					else
					{
						newDirection = Direction.RIGHT;
					}
					break;
				}
				case DOWN:
				{
					if (random == 0)
					{
						newDirection = Direction.RIGHT;
					}
					else
					{
						newDirection = Direction.LEFT;
					}
					break;
				}
				case LEFT:
				{
					if (random == 0)
					{
						newDirection = Direction.UP;
					}
					else
					{
						newDirection = Direction.DOWN;
					}
					break;
				}
				case RIGHT:
				{
					if (random == 0)
					{
						newDirection = Direction.DOWN;
					}
					else
					{
						newDirection = Direction.UP;
					}
					break;
				}
			}
			if (!addTurn(width, outerBoundaryPoints, innerBoundaryPoints, boundaries, zigZagDim, currentDirection, newDirection))
			{
				break;
			}
			currentDirection = newDirection;
		}
		addStraightLine(maxLength, minLength, outerBoundaryPoints, innerBoundaryPoints,	boundaries, zigZagDim, currentDirection);
		int offsetX = 0 -zigZagDim.minX + 2;
		int offsetY = 0 -zigZagDim.minY + 2;
		int dimensionX = zigZagDim.maxX - zigZagDim.minX + 4;
		int dimensionY = zigZagDim.maxY - zigZagDim.minY + 4;
		int randomTrackCount = 0;
		boolean countOkay = false;
		while (!countOkay)
		{
			File file = new File(System.getProperty("user.dir") + String.format("\\Tracks\\RandomZigZag-%d.xml",randomTrackCount));
			if (file.exists()) 
			{
				randomTrackCount++;
			}
			else
			{
				countOkay = true;
			}
		}
		
		//Start XML structure
		Element trackElement = new Element("Track");
		Document trackFile = new Document(trackElement);
		trackElement.setAttribute("name", String.format("RandomZigZag-%d",randomTrackCount));
		trackElement.setAttribute("type", "Sprint");
		
		//Create outer boundaries
		Element outerBoundariesElement = new Element("OuterBoundaries");
		trackElement.addContent(outerBoundariesElement);
		for (int i = 0; i < outerBoundaryPoints.size(); i++)
		{
			Element outerBoundaryPointElement = new Element("Point");
			outerBoundaryPointElement.setAttribute("id", String.format("%d", i));
			outerBoundaryPointElement.setAttribute("x", String.format("%d", outerBoundaryPoints.get(i).X() + offsetX)); 
			outerBoundaryPointElement.setAttribute("y", String.format("%d", outerBoundaryPoints.get(i).Y() + offsetY)); 
			outerBoundariesElement.addContent(outerBoundaryPointElement);
		}
		
		//Create inner boundaries
		Element innerBoundariesElement = new Element("InnerBoundaries");
		trackElement.addContent(innerBoundariesElement);
		for (int i = 0; i < innerBoundaryPoints.size(); i++)
		{
			Element innerBoundaryPointElement = new Element("Point");
			innerBoundaryPointElement.setAttribute("id", String.format("%d", i));
			innerBoundaryPointElement.setAttribute("x", String.format("%d", innerBoundaryPoints.get(i).X() + offsetX)); 
			innerBoundaryPointElement.setAttribute("y", String.format("%d", innerBoundaryPoints.get(i).Y() + offsetY)); 
			innerBoundariesElement.addContent(innerBoundaryPointElement);
		}
		
		//Dimension
		Element dimensionElement = new Element("Dimension");
		trackElement.addContent(dimensionElement);
		dimensionElement.setAttribute("width", String.format("%d",dimensionX));
		dimensionElement.setAttribute("height", String.format("%d",dimensionY));
		
		//Starting positions
		Element startingPositionsElement = new Element("StartingPositions");
		trackElement.addContent(startingPositionsElement);
		for (int i = 0; i < startingPositions.size(); i++)
		{
			Element startingPositionPointElement = new Element("Point");
			startingPositionPointElement.setAttribute("x", String.format("%d", startingPositions.get(i).X() + offsetX)); 
			startingPositionPointElement.setAttribute("y", String.format("%d", startingPositions.get(i).Y() + offsetY)); 
			startingPositionsElement.addContent(startingPositionPointElement);
		}
		
		XMLOutputter out = new XMLOutputter();
		try
		{
			out.output( trackFile, new FileOutputStream(System.getProperty("user.dir") + String.format("\\Tracks\\RandomZigZag-%d.xml",randomTrackCount)));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static boolean addStraightLine(int maxLength, int minLength, ArrayList<Point> outerBoundaryPoints, ArrayList<Point> innerBoundaryPoints,
			ArrayList<LineSegment> boundaries, ZigZagDimensions zigZagDim, Direction currentDirection)
	{
		Point newOuterBoundaryPoint = null;
		Point newInnerBoundaryPoint = null;
		int distance = generator.nextInt(maxLength-minLength) + minLength;
		switch(currentDirection)
		{
			case UP:
			{
				newOuterBoundaryPoint = new Point(outerBoundaryPoints.get(outerBoundaryPoints.size()-1).X(),outerBoundaryPoints.get(outerBoundaryPoints.size()-1).Y() + distance);
				newInnerBoundaryPoint = new Point(innerBoundaryPoints.get(innerBoundaryPoints.size()-1).X(),innerBoundaryPoints.get(innerBoundaryPoints.size()-1).Y() + distance);
				break;
			}
			case DOWN:
			{
				newOuterBoundaryPoint = new Point(outerBoundaryPoints.get(outerBoundaryPoints.size()-1).X(),outerBoundaryPoints.get(outerBoundaryPoints.size()-1).Y() - distance);
				newInnerBoundaryPoint = new Point(innerBoundaryPoints.get(innerBoundaryPoints.size()-1).X(),innerBoundaryPoints.get(innerBoundaryPoints.size()-1).Y() - distance);
				break;
			}
			case RIGHT:
			{
				newOuterBoundaryPoint = new Point(outerBoundaryPoints.get(outerBoundaryPoints.size()-1).X() + distance,outerBoundaryPoints.get(outerBoundaryPoints.size()-1).Y());
				newInnerBoundaryPoint = new Point(innerBoundaryPoints.get(innerBoundaryPoints.size()-1).X() + distance,innerBoundaryPoints.get(innerBoundaryPoints.size()-1).Y());
				break;
			}
			case LEFT:
			{
				newOuterBoundaryPoint = new Point(outerBoundaryPoints.get(outerBoundaryPoints.size()-1).X() - distance,outerBoundaryPoints.get(outerBoundaryPoints.size()-1).Y());
				newInnerBoundaryPoint = new Point(innerBoundaryPoints.get(innerBoundaryPoints.size()-1).X() - distance,innerBoundaryPoints.get(innerBoundaryPoints.size()-1).Y());
				break;
			}
		}
		LineSegment newOuterLineSegment = new LineSegment(outerBoundaryPoints.get(outerBoundaryPoints.size()-1),newOuterBoundaryPoint);
		int newOuterLineSegmentCrossings = 0;
		LineSegment newInnerLineSegment = new LineSegment(innerBoundaryPoints.get(innerBoundaryPoints.size()-1),newInnerBoundaryPoint);
		int newInnerLineSegmentCrossings = 0;
		for (LineSegment ls : boundaries)
		{
			if (ls.IntersectWith(newOuterLineSegment))
			{
				newOuterLineSegmentCrossings++;
				if (newOuterLineSegmentCrossings > 1)
				{
					return false;
				}
			}
			if (ls.IntersectWith(newInnerLineSegment))
			{
				newInnerLineSegmentCrossings++;
				if (newInnerLineSegmentCrossings > 1)
				{
					return false;
				}
			}
		}
		if (newInnerBoundaryPoint.X() > zigZagDim.maxX)
		{
			zigZagDim.maxX = newInnerBoundaryPoint.X();
		}
		if (newOuterBoundaryPoint.X() > zigZagDim.maxX)
		{
			zigZagDim.maxX = newOuterBoundaryPoint.X();
		}
		if (newInnerBoundaryPoint.Y() > zigZagDim.maxY)
		{
			zigZagDim.maxY = newInnerBoundaryPoint.Y();
		}
		if (newOuterBoundaryPoint.Y() > zigZagDim.maxY)
		{
			zigZagDim.maxY = newOuterBoundaryPoint.Y();
		}
		if (newInnerBoundaryPoint.X() < zigZagDim.minX)
		{
			zigZagDim.minX = newInnerBoundaryPoint.X();
		}
		if (newOuterBoundaryPoint.X() < zigZagDim.minX)
		{
			zigZagDim.minX = newOuterBoundaryPoint.X();
		}
		if (newInnerBoundaryPoint.Y() < zigZagDim.minY)
		{
			zigZagDim.minY = newInnerBoundaryPoint.Y();
		}
		if (newOuterBoundaryPoint.Y() < zigZagDim.minY)
		{
			zigZagDim.minY = newOuterBoundaryPoint.Y();
		}
		outerBoundaryPoints.add(newOuterBoundaryPoint);
		innerBoundaryPoints.add(newInnerBoundaryPoint);
		boundaries.add(newOuterLineSegment);
		boundaries.add(newInnerLineSegment);
		return true;
	}
	
	private static boolean addTurn(int width, ArrayList<Point> outerBoundaryPoints, ArrayList<Point> innerBoundaryPoints, 
			ArrayList<LineSegment> boundaries, ZigZagDimensions zigZagDim, Direction currentDirection, Direction newDirection)
	{
		Point newBoundaryPoint1 = null;
		Point newBoundaryPoint2 = null;
		LineSegment newLineSegment1 = null;
		LineSegment newLineSegment2 = null;
		boolean isOuterBoundary = false;
		switch (currentDirection)
		{
			case UP:
			{
				newBoundaryPoint1 = new Point(outerBoundaryPoints.get(outerBoundaryPoints.size()-1).X(),outerBoundaryPoints.get(outerBoundaryPoints.size()-1).Y() + width + 1);
				newBoundaryPoint2 = new Point(innerBoundaryPoints.get(innerBoundaryPoints.size()-1).X(),innerBoundaryPoints.get(innerBoundaryPoints.size()-1).Y() + width + 1);
				newLineSegment1 = new LineSegment(newBoundaryPoint1,newBoundaryPoint2);
				switch (newDirection)
				{
					case LEFT:
					{
						newLineSegment2 = new LineSegment(innerBoundaryPoints.get(innerBoundaryPoints.size()-1),newBoundaryPoint2);
						isOuterBoundary = false;
						break;
					}
					case RIGHT:
					{
						newLineSegment2 = new LineSegment(outerBoundaryPoints.get(outerBoundaryPoints.size()-1),newBoundaryPoint1);
						isOuterBoundary = true;
						break;
					}
					default:
					{
						return false;
					}
				}
				break;
			}
			case DOWN:
			{
				newBoundaryPoint1 = new Point(outerBoundaryPoints.get(outerBoundaryPoints.size()-1).X(),outerBoundaryPoints.get(outerBoundaryPoints.size()-1).Y() - width - 1);
				newBoundaryPoint2 = new Point(innerBoundaryPoints.get(innerBoundaryPoints.size()-1).X(),innerBoundaryPoints.get(innerBoundaryPoints.size()-1).Y() - width - 1);
				newLineSegment1 = new LineSegment(newBoundaryPoint1,newBoundaryPoint2);
				switch (newDirection)
				{
					case RIGHT:
					{
						newLineSegment2 = new LineSegment(innerBoundaryPoints.get(innerBoundaryPoints.size()-1),newBoundaryPoint2);
						isOuterBoundary = false;
						break;
					}
					case LEFT:
					{
						newLineSegment2 = new LineSegment(outerBoundaryPoints.get(outerBoundaryPoints.size()-1),newBoundaryPoint1);
						isOuterBoundary = true;
						break;
					}
					default:
					{
						return false;
					}
				}
				break;
			}
			case RIGHT:
			{
				newBoundaryPoint1 = new Point(outerBoundaryPoints.get(outerBoundaryPoints.size()-1).X() + width + 1,outerBoundaryPoints.get(outerBoundaryPoints.size()-1).Y());
				newBoundaryPoint2 = new Point(innerBoundaryPoints.get(innerBoundaryPoints.size()-1).X() + width + 1,innerBoundaryPoints.get(innerBoundaryPoints.size()-1).Y());
				newLineSegment1 = new LineSegment(newBoundaryPoint1,newBoundaryPoint2);
				switch (newDirection)
				{
					case UP:
					{
						newLineSegment2 = new LineSegment(innerBoundaryPoints.get(innerBoundaryPoints.size()-1),newBoundaryPoint2);
						isOuterBoundary = false;
						break;
					}
					case DOWN:
					{
						newLineSegment2 = new LineSegment(outerBoundaryPoints.get(outerBoundaryPoints.size()-1),newBoundaryPoint1);
						isOuterBoundary = true;
						break;
					}
					default:
					{
						return false;
					}
				}
				break;
			}
			case LEFT:
			{
				newBoundaryPoint1 = new Point(outerBoundaryPoints.get(outerBoundaryPoints.size()-1).X() - width - 1,outerBoundaryPoints.get(outerBoundaryPoints.size()-1).Y());
				newBoundaryPoint2 = new Point(innerBoundaryPoints.get(innerBoundaryPoints.size()-1).X() - width - 1,innerBoundaryPoints.get(innerBoundaryPoints.size()-1).Y());
				newLineSegment1 = new LineSegment(newBoundaryPoint1,newBoundaryPoint2);
				switch (newDirection)
				{
					case DOWN:
					{
						newLineSegment2 = new LineSegment(innerBoundaryPoints.get(innerBoundaryPoints.size()-1),newBoundaryPoint2);
						isOuterBoundary = false;
						break;
					}
					case UP:
					{
						newLineSegment2 = new LineSegment(outerBoundaryPoints.get(outerBoundaryPoints.size()-1),newBoundaryPoint1);
						isOuterBoundary = true;
						break;
					}
					default:
					{
						return false;
					}
				}
				break;
			}
		}
		int newLineSegmentCrossings1 = 0;
		int newLineSegmentCrossings2 = 0;
		for (LineSegment ls : boundaries)
		{
			if (ls.IntersectWith(newLineSegment1))
			{
				newLineSegmentCrossings1++;
				if (newLineSegmentCrossings1 > 0)
				{
					return false;
				}
			}
			if (ls.IntersectWith(newLineSegment2))
			{
				newLineSegmentCrossings2++;
				if (newLineSegmentCrossings2 > 1)
				{
					return false;
				}
			}
		}
		if (newBoundaryPoint1.X() > zigZagDim.maxX)
		{
			zigZagDim.maxX = newBoundaryPoint1.X();
		}
		if (newBoundaryPoint2.X() > zigZagDim.maxX)
		{
			zigZagDim.maxX = newBoundaryPoint2.X();
		}
		if (newBoundaryPoint1.Y() > zigZagDim.maxY)
		{
			zigZagDim.maxY = newBoundaryPoint1.Y();
		}
		if (newBoundaryPoint2.Y() > zigZagDim.maxY)
		{
			zigZagDim.maxY = newBoundaryPoint2.Y();
		}
		if (newBoundaryPoint1.X() < zigZagDim.minX)
		{
			zigZagDim.minX = newBoundaryPoint1.X();
		}
		if (newBoundaryPoint2.X() < zigZagDim.minX)
		{
			zigZagDim.minX = newBoundaryPoint2.X();
		}
		if (newBoundaryPoint1.Y() < zigZagDim.minY)
		{
			zigZagDim.minY = newBoundaryPoint1.Y();
		}
		if (newBoundaryPoint2.Y() < zigZagDim.minY)
		{
			zigZagDim.minY = newBoundaryPoint2.Y();
		}
		if (isOuterBoundary)
		{
			outerBoundaryPoints.add(newBoundaryPoint1);
			outerBoundaryPoints.add(newBoundaryPoint2);
		}
		else
		{
			innerBoundaryPoints.add(newBoundaryPoint2);
			innerBoundaryPoints.add(newBoundaryPoint1);
		}
		boundaries.add(newLineSegment1);
		boundaries.add(newLineSegment2);
		return true;
	}
}
