package src.main.java.logic;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;

public class ZigZagVertex
{
	private Point2D position;
	private Point2D speed;
	private int distance;
	private ZigZagVertex predecessor;
	
	public ZigZagVertex(Point2D _position, Point2D _speed)
	{
		position = _position;
		speed = _speed;
		distance = -1;
		predecessor = null;
	}
	
	public Point2D Position()
	{
		return position;
	}
	
	public Point2D Speed()
	{
		return speed;
	}
	
	public int Distance()
	{
		return distance;
	}
	
	public void Distance(int _distance)
	{
		distance = _distance;
	}
	
	public ZigZagVertex Predecessor()
	{
		return predecessor;
	}
	
	public void Predecessor(ZigZagVertex _predecessor)
	{
		predecessor = _predecessor;
	}
	
	@Override
	public String toString()
	{
		Point2D p=position;
		Point2D s=speed;
		return new String( "( x:" + p.getX() + " , y:" + p.getY() + " ); ( sx:" + s.getX() + " , sy:" + s.getY() + " ) " );
	}
	
	public boolean isAtPosition( Point2D p )
	{
		return (p.getX()==this.position.getX() && p.getY()==this.position.getY() );
	}
}
