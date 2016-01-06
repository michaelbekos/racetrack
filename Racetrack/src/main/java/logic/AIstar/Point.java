package src.main.java.logic.AIstar;

import java.util.HashMap;

public class Point {
	private int x;
	private int y;
	public int X()
	{
		return x;
	}
	public int Y()
	{
		return y;
	}
	public Point(int _x,int _y)
	{
		this.x = _x;
		this.y = _y;		
	}
	public boolean equals(Point q)
	{
		if (this.x == q.X() && this.y == q.Y())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	static public Point GetPoint(int x, int y)
	{
		if (Data.Points.containsKey(x))
		{
			if (Data.Points.get(x).containsKey(y))
			{
				return Data.Points.get(x).get(y);
			}
			else
			{
				Point p = new Point (x,y);
				Data.Points.get(x).put(y, p);
				return p;
			}
		}
		else
		{
			Data.Points.put(x, new HashMap<Integer,Point>());
			Point p = new Point (x,y);
			Data.Points.get(x).put(y, p);
			return p;
		}
	}
	static public Point GetPoint(javafx.geometry.Point2D point2D)
	{
		return GetPoint((int)point2D.getX(),(int)point2D.getY());
	}
	public double DistanceTo(Point q)
	{
		return Math.pow((double)this.x-(double)q.X(), 2) + Math.pow((double)this.y-(double)q.Y(), 2); 
	}
	public double DistanceTo(double xi, double yi)
	{
		return Math.pow((double)this.x-xi, 2) + Math.pow((double)this.y-yi, 2); 
	}
	public javafx.geometry.Point2D ToPoint2D()
	{
		return new javafx.geometry.Point2D(x,y);
	}
}