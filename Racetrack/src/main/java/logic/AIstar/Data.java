package src.main.java.logic.AIstar;

import java.util.HashMap;

public class Data {
    static HashMap<Integer,HashMap<Integer,Point>> Points = new HashMap<Integer,HashMap<Integer,Point>>();;
	static HashMap<Point,HashMap<Point,LineSegment>> LineSegments = new HashMap<Point,HashMap<Point,LineSegment>>();
	static public HashMap<Point,HashMap<Integer,HashMap<Integer,State>>> States = new HashMap<Point,HashMap<Integer,HashMap<Integer,State>>>();
	static void Clear()
	{
		Points = new HashMap<Integer,HashMap<Integer,Point>>();;
		LineSegments = new HashMap<Point,HashMap<Point,LineSegment>>();
		States = new HashMap<Point,HashMap<Integer,HashMap<Integer,State>>>();
	}
}
