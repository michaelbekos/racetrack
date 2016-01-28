package src.main.java.logic.utils;

import java.util.List;

import javafx.geometry.Point2D;
import src.main.java.logic.utils.AIUtils;

public class AIUtilsTest
{

	public static void main(String[] args)
	{
		int testCase = 1;
		//Test Case 1
		Point2D a = new Point2D(6,2);
		Point2D b = new Point2D(4,13);
		Point2D sa = new Point2D(1,1);
		Point2D sb = new Point2D(-2,1);
		
		List<Point2D> accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb,0);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 2
		a = new Point2D(6,2);
		b = new Point2D(3,13);
		sa = new Point2D(1,1);
		sb = new Point2D(-1,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb,0);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 3
		a = new Point2D(4,13);
		b = new Point2D(6,2);
		sa = new Point2D(2,-1);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb,0);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 4
		a = new Point2D(3,13);
		b = new Point2D(6,2);
		sa = new Point2D(1,-1);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb,0);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");

		//Test Case 5
		a = new Point2D(6,2);
		b = new Point2D(4,11);
		sa = new Point2D(1,1);
		sb = new Point2D(-2,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb,0);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 6
		a = new Point2D(6,2);
		b = new Point2D(4,15);
		sa = new Point2D(1,1);
		sb = new Point2D(-2,1);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb,0);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
	}
}
