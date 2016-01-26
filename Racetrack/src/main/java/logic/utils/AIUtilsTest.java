package src.main.java.logic.utils;

import java.util.List;

import javafx.geometry.Point2D;
import src.main.java.logic.utils.AIUtils;

public class AIUtilsTest
{

	public static void main(String[] args)
	{
		//Test Case 1
		Point2D a = new Point2D(6,2);
		Point2D b = new Point2D(4,13);
		Point2D sa = new Point2D(1,1);
		Point2D sb = new Point2D(-2,1);
		
		/*//Test Case 2
		Point2D a = new Point2D(6,2);
		Point2D b = new Point2D(3,13);
		Point2D sa = new Point2D(1,1);
		Point2D sb = new Point2D(-1,1);*/
		
		List<Point2D> accelerations = AIUtils.CalculateAccelerations(a,b,sa,sb,0);
		for (int i = 0; i < accelerations.size(); i++)
		{
			System.out.println(String.format("(%d,%d)", (int)accelerations.get(i).getX(), (int)accelerations.get(i).getY()));
		}
	}

}
