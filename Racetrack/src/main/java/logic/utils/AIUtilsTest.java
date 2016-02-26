package src.main.java.logic.utils;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;
import src.main.java.logic.AIstar.LineSegment;
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
		List<Point2D> accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 2
		a = new Point2D(6,2);
		b = new Point2D(3,13);
		sa = new Point2D(1,1);
		sb = new Point2D(-1,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 3
		a = new Point2D(4,13);
		b = new Point2D(6,2);
		sa = new Point2D(2,-1);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);		
		
		//Test Case 4
		a = new Point2D(3,13);
		b = new Point2D(6,2);
		sa = new Point2D(1,-1);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 5
		a = new Point2D(6,2);
		b = new Point2D(4,11);
		sa = new Point2D(1,1);
		sb = new Point2D(-2,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 6
		a = new Point2D(6,2);
		b = new Point2D(4,15);
		sa = new Point2D(1,1);
		sb = new Point2D(-2,1);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 7
		a = new Point2D(4,11);
		b = new Point2D(6,2);
		sa = new Point2D(2,-1);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 8
		a = new Point2D(4,15);
		b = new Point2D(6,2);
		sa = new Point2D(2,-1);
		sb = new Point2D(-1,-1);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 9
		a = new Point2D(6,2);
		b = new Point2D(8,13);
		sa = new Point2D(1,1);
		sb = new Point2D(2,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 10
		a = new Point2D(6,2);
		b = new Point2D(8,15);
		sa = new Point2D(1,1);
		sb = new Point2D(2,1);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 11
		a = new Point2D(6,2);
		b = new Point2D(4,13);
		sa = new Point2D(1,1);
		sb = new Point2D(-2,2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 12
		a = new Point2D(6,2);
		b = new Point2D(4,13);
		sa = new Point2D(1,1);
		sb = new Point2D(-2,3);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 13
		a = new Point2D(6,2);
		b = new Point2D(4,13);
		sa = new Point2D(1,2);
		sb = new Point2D(-2,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 14
		a = new Point2D(6,2);
		b = new Point2D(4,13);
		sa = new Point2D(1,3);
		sb = new Point2D(-2,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 15
		a = new Point2D(2,2);
		b = new Point2D(13,13);
		sa = new Point2D(1,1);
		sb = new Point2D(1,1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 16
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(1,1);
		sb = new Point2D(1,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 17
		a = new Point2D(2,6);
		b = new Point2D(13,3);
		sa = new Point2D(1,1);
		sb = new Point2D(1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 18
		a = new Point2D(13,4);
		b = new Point2D(2,6);
		sa = new Point2D(-1,2);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 19
		a = new Point2D(13,3);
		b = new Point2D(2,6);
		sa = new Point2D(-1,1);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 20
		a = new Point2D(2,6);
		b = new Point2D(11,4);
		sa = new Point2D(1,1);
		sb = new Point2D(1,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 21
		a = new Point2D(2,6);
		b = new Point2D(15,4);
		sa = new Point2D(1,1);
		sb = new Point2D(1,-2);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 22
		a = new Point2D(11,4);
		b = new Point2D(2,6);
		sa = new Point2D(-1,2);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 23
		a = new Point2D(15,4);
		b = new Point2D(2,6);
		sa = new Point2D(-1,2);
		sb = new Point2D(-1,-1);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 24
		a = new Point2D(2,6);
		b = new Point2D(13,8);
		sa = new Point2D(1,1);
		sb = new Point2D(1,2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 25
		a = new Point2D(2,6);
		b = new Point2D(15,8);
		sa = new Point2D(1,1);
		sb = new Point2D(1,2);
				
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 26
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(1,1);
		sb = new Point2D(2,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 27
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(1,1);
		sb = new Point2D(3,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 28
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(2,1);
		sb = new Point2D(1,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 29
		a = new Point2D(2,6);
		b = new Point2D(13,4);
		sa = new Point2D(3,1);
		sb = new Point2D(1,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 30
		a = new Point2D(2,6);
		b = new Point2D(13,8);
		sa = new Point2D(1,1);
		sb = new Point2D(3,2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 30
		a = new Point2D(6,2);
		b = new Point2D(8,13);
		sa = new Point2D(1,1);
		sb = new Point2D(2,3);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 32
		a = new Point2D(2,13);
		b = new Point2D(13,2);
		sa = new Point2D(1,-1);
		sb = new Point2D(1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 33
		a = new Point2D(6,2);
		b = new Point2D(130,2);
		sa = new Point2D(1,0);
		sb = new Point2D(1,0);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
//		System.out.println(accelerations.size());
//		
//		int x = 6;
//		int y = 2;
//		int sx = 1;
//		int sy = 0;
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			sx += (int)accelerations.get(i).getX();
//			sy += (int)accelerations.get(i).getY();
//			x += sx;
//			y += sy;
//			System.out.println(String.format("(%d,%d)", x,y));
//		}
		
		//Test Case 34
		a = new Point2D(0,2);
		b = new Point2D(100,2);
		sa = new Point2D(1,10);
		sb = new Point2D(1,10);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
//		
//		System.out.println(accelerations.size());
		
//		x = 0;
//		y = 2;
//		sx = 0;
//		sy = 10;
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			sx += (int)accelerations.get(i).getX();
//			sy += (int)accelerations.get(i).getY();
//			x += sx;
//			y += sy;
//			System.out.println(String.format("(%d,%d)", x,y));
//		}
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 35
		a = new Point2D(49,24);
		b = new Point2D(45,14);
		sa = new Point2D(2,-3);
		sb = new Point2D(-3,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 36
		a = new Point2D(49,24);
		b = new Point2D(45,14);
		sa = new Point2D(2,-3);
		sb = new Point2D(-2,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 37
		a = new Point2D(49,24);
		b = new Point2D(45,14);
		sa = new Point2D(2,-3);
		sb = new Point2D(-1,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);

		//Test Case 38
		a = new Point2D(49,143);
		b = new Point2D(46,121);
		sa = new Point2D(2,-3);
		sb = new Point2D(-2,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
//		System.out.println("--------------------------");
//		System.out.println(String.format("Test Case %d",testCase++));
//		System.out.println(String.format("(%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)a.getX(),(int)a.getY(),(int)sa.getX(),(int)sa.getY(),(int)b.getX(),(int)b.getY(),(int)sb.getX(),(int)sb.getY()));
//		System.out.println("--------------------------");
//		for (int i = 0; i < accelerations.size(); i++)
//		{
//			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
//		}
//		System.out.println("--------------------------");
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 39
		a = new Point2D(3,5);
		b = new Point2D(18,10);
		sa = new Point2D(0,0);
		sb = new Point2D(2,3);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 40
		a = new Point2D(18,10);
		b = new Point2D(25,28);
		sa = new Point2D(2,3);
		sb = new Point2D(3,2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
		Validate(a, b, sa, sb, accelerations, testCase++);
	
		//Test Case 41
		a = new Point2D(25,28);
		b = new Point2D(48,24);
		sa = new Point2D(3,2);
		sb = new Point2D(2,-3);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
		Validate(a, b, sa, sb, accelerations, testCase++);
	
		//Test Case 42
		a = new Point2D(48,24);
		b = new Point2D(46,14);
		sa = new Point2D(2,-3);
		sb = new Point2D(-2,-1);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 43
		a = new Point2D(49,27);
		b = new Point2D(45,13);
		sa = new Point2D(2,-1);
		sb = new Point2D(-1,-2);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
		Validate(a, b, sa, sb, accelerations, testCase++);
		
		//Test Case 44
		a = new Point2D(32,26);
		b = new Point2D(48,32);
		sa = new Point2D(3,3);
		sb = new Point2D(3,3);
		
		accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb, new LinkedList<LineSegment>());
		Validate(a, b, sa, sb, accelerations, testCase++);
	}

	private static void Validate(Point2D a, Point2D b, Point2D sa, Point2D sb, List<Point2D> accelerations, int testCaseNumber)
	{
		int x = (int) a.getX();
		int y = (int) a.getY();
		int sx = (int) sa.getX();
		int sy = (int) sa.getY();
		for (int i = 0; i < accelerations.size(); i++)
		{
			int ax = (int)accelerations.get(i).getX();
			int ay = (int)accelerations.get(i).getY();
			sx = sx + ax;
			sy = sy + ay;
			x = x + sx;
			y = y + sy;
			if (ax > 1 || ax < -1)
			{
				System.out.println(String.format("Illegal acceleration computed in test case %d", testCaseNumber));
			}
			if (ay > 1 || ay < -1)
			{
				System.out.println(String.format("Illegal acceleration computed in test case %d", testCaseNumber));
			}
		}
		if (sx != (int) sb.getX() || sy != (int) sb.getY())
		{
			System.out.println(String.format("Speed computed not matching with required final speed in test case %d", testCaseNumber));
		}
		if (x != (int) b.getX() || y != (int) b.getY())
		{
			System.out.println(String.format("Position computed not matching with required final position in test case %d", testCaseNumber));
		}
		if (sx == (int)sb.getX() && sy == (int)sb.getY() && x == (int)b.getX() && y == (int)b.getY())
		{	
			System.out.println(String.format("Validation successful for test case %d!", testCaseNumber));
		}
	}
}
