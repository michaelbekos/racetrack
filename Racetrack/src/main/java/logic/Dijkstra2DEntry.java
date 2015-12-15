package src.main.java.logic;

import javafx.geometry.Point2D;

public class Dijkstra2DEntry {
	private int val;
	private Point2D pos;

	public Dijkstra2DEntry( int x, int y, int value )
	{
		pos=new Point2D( x, y );
		val=value;
	}
	
	public int getValue()
	{
		return val;
	}
	
	public int x()
	{
		return (int)pos.getX();
	}
	
	public int y()
	{
		return (int)pos.getY();
	}
	
	public void  setValue( int v )
	{
		val=v;
	}
	public String toString()
	{
		return "Pos:( "+pos.getX()+", "+pos.getY()+" ); Val:"+val;
	}
}
