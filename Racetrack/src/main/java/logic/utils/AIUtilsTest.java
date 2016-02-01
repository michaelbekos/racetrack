package src.main.java.logic.utils;

import java.util.List;

import javafx.geometry.Point2D;
import src.main.java.logic.utils.AIUtils;

/**
 * Applicable class with test cases for CalculateAccelerations.
 * 
 * @author Henry
 */
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
		
		List<Point2D> accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
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
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
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
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
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
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
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
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
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
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 7
		a = new Point2D(4,11);
		b = new Point2D(6,2);
		sa = new Point2D(2,-1);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 8
		a = new Point2D(4,15);
		b = new Point2D(6,2);
		sa = new Point2D(2,-1);
		sb = new Point2D(-1,-1);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 9
		a = new Point2D(6,2);
		b = new Point2D(8,13);
		sa = new Point2D(1,1);
		sb = new Point2D(2,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 10
		a = new Point2D(6,2);
		b = new Point2D(8,15);
		sa = new Point2D(1,1);
		sb = new Point2D(2,1);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 11
		a = new Point2D(6,2);
		b = new Point2D(4,13);
		sa = new Point2D(1,1);
		sb = new Point2D(-2,2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 12
		a = new Point2D(6,2);
		b = new Point2D(4,13);
		sa = new Point2D(1,1);
		sb = new Point2D(-2,3);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 13
		a = new Point2D(6,2);
		b = new Point2D(4,13);
		sa = new Point2D(1,2);
		sb = new Point2D(-2,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 14
		a = new Point2D(6,2);
		b = new Point2D(4,13);
		sa = new Point2D(1,3);
		sb = new Point2D(-2,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 15
		a = new Point2D(2,2);
		b = new Point2D(13,13);
		sa = new Point2D(1,1);
		sb = new Point2D(1,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 16
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(1,1);
		sb = new Point2D(1,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 17
		a = new Point2D(2,6);
		b = new Point2D(13,3);
		sa = new Point2D(1,1);
		sb = new Point2D(1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 18
		a = new Point2D(13,4);
		b = new Point2D(2,6);
		sa = new Point2D(-1,2);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 19
		a = new Point2D(13,3);
		b = new Point2D(2,6);
		sa = new Point2D(-1,1);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");

		//Test Case 20
		a = new Point2D(2,6);
		b = new Point2D(11,4);
		sa = new Point2D(1,1);
		sb = new Point2D(1,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 21
		a = new Point2D(2,6);
		b = new Point2D(15,4);
		sa = new Point2D(1,1);
		sb = new Point2D(1,-2);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 22
		a = new Point2D(11,4);
		b = new Point2D(2,6);
		sa = new Point2D(-1,2);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 23
		a = new Point2D(15,4);
		b = new Point2D(2,6);
		sa = new Point2D(-1,2);
		sb = new Point2D(-1,-1);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 24
		a = new Point2D(2,6);
		b = new Point2D(13,8);
		sa = new Point2D(1,1);
		sb = new Point2D(1,2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 25
		a = new Point2D(2,6);
		b = new Point2D(15,8);
		sa = new Point2D(1,1);
		sb = new Point2D(1,2);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 26
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(1,1);
		sb = new Point2D(2,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 27
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(1,1);
		sb = new Point2D(3,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 28
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(2,1);
		sb = new Point2D(1,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 29
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(3,1);
		sb = new Point2D(1,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 30
		a = new Point2D(2,6);
		b = new Point2D(13,8);
		sa = new Point2D(1,1);
		sb = new Point2D(3,2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 30
		a = new Point2D(6,2);
		b = new Point2D(8,13);
		sa = new Point2D(1,1);
		sb = new Point2D(2,3);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 32
		a = new Point2D(2,13);
		b = new Point2D(13,2);
		sa = new Point2D(1,-1);
		sb = new Point2D(1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		//Test Case 33
		a = new Point2D(6,2);
		b = new Point2D(130,2);
		sa = new Point2D(1,0);
		sb = new Point2D(1,0);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		System.out.println(accelerations.size());
		
		int x = 6;
		int y = 2;
		int sx = 1;
		int sy = 0;
		for (int i = 0; i < accelerations.size(); i++)
		{
			sx += (int)accelerations.get(i).getX();
			sy += (int)accelerations.get(i).getY();
			x += sx;
			y += sy;
			System.out.println(String.format("(%d,%d)", x,y));
		}
		
		//Test Case 33
		a = new Point2D(0,2);
		b = new Point2D(100,2);
		sa = new Point2D(1,10);
		sb = new Point2D(1,10);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb);
		System.out.println("--------------------------");
		System.out.println(String.format("Test Case %d",testCase++));
		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
		System.out.println("--------------------------");
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
		System.out.println("--------------------------");
		
		System.out.println(accelerations.size());
		
		x = 0;
		y = 2;
		sx = 0;
		sy = 10;
		for (int i = 0; i < accelerations.size(); i++)
		{
			sx += (int)accelerations.get(i).getX();
			sy += (int)accelerations.get(i).getY();
			x += sx;
			y += sy;
			System.out.println(String.format("(%d,%d)", x,y));
		}
		
	}
}
