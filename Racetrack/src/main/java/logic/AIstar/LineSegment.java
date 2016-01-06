package src.main.java.logic.AIstar;

import java.util.HashMap;
import src.main.java.logic.Line2D;

public class LineSegment {
	static double threshold = 0.0001f;
	private Point p1;
	private Point p2;
	private double a;
	private double b;
	private boolean aIsInf;
	private boolean aIsZero;
	public double A()
	{
		return a;
	}
	public double B()
	{
		return b;
	}
	public boolean AIsInf()
	{
		return aIsInf;
	}
	public boolean AIsZero()
	{
		return aIsZero;
	}
	public Point P1()
	{
		return p1;
	}
	public Point P2()
	{
		return p2;
	}
	public LineSegment (Point q1, Point q2)
	{
		p1 = q1;
		p2 = q2;
		if (q1.X() == q2.X())
		{
			aIsInf = true;
			a = -1;
			b = -1;
		}
		else
		{
			aIsInf = false;
			if (q1.Y() == q2.Y())
			{
				aIsZero = true;
				a = 0;
			}
			else
			{
				a = ((double)q2.Y() - (double)q1.Y())/((double)q2.X() - (double)q1.X());
			}
			b = (double)q1.Y() - a * (double)q1.X();
		}
	}
	public static LineSegment GetLineSegment (Point p1, Point p2)
	{
		if (Data.LineSegments.containsKey(p1))
		{
			if (Data.LineSegments.get(p1).containsKey(p2))
			{
				return Data.LineSegments.get(p1).get(p2);
			}
			else
			{
				LineSegment ls = new LineSegment (p1,p2);
				Data.LineSegments.get(p1).put(p2, ls);
				return ls;
			}
		}
		else
		{
			if (Data.LineSegments.containsKey(p2))
			{
				if (Data.LineSegments.get(p2).containsKey(p1))
				{
					return Data.LineSegments.get(p2).get(p1);
				}
				else
				{
					LineSegment ls = new LineSegment (p2,p1);
					Data.LineSegments.get(p2).put(p1, ls);
					return ls;
				}
			}
			else
			{
				Data.LineSegments.put(p1, new HashMap<Point,LineSegment>());
				LineSegment ls = new LineSegment (p1,p2);
				Data.LineSegments.get(p1).put(p2, ls);
				return ls;
			}
		}
	}
	public static LineSegment GetLineSegment(Line2D line2D)
	{
		return GetLineSegment(Point.GetPoint(line2D.getStartPoint()),Point.GetPoint(line2D.getEndPoint()));
	}
	public boolean IntersectWith(LineSegment ls)
	{
		double xi = 0;
		double yi = 0;
		if (this.aIsInf)
		{
			if (ls.AIsInf())
			{
				if (this.p1.X() == ls.P1().X())
				{
					if ((this.p1.Y() <= ls.P1().Y() && this.p1.Y() >= ls.P2().Y()) || (this.p1.Y() >= ls.P1().Y() && this.p1.Y() <= ls.P2().Y()))
					{
						return true;
					}
					if ((this.p2.Y() <= ls.P1().Y() && this.p2.Y() >= ls.P2().Y()) || (this.p2.Y() >= ls.P1().Y() && this.p2.Y() <= ls.P2().Y()))
					{
						return true;
					}
				}
				return false;
			}
			else
			{
				if (ls.P1().X() == this.P1().X())
				{
					if ((this.p1.Y() <= ls.P1().Y() && this.p2.Y() >= ls.P1().Y()) || (this.p1.Y() >= ls.P1().Y() && this.p2.Y() <= ls.P1().Y()))
					{
						return true;
					}
				}
				else
				{
					if (ls.P2().X() == this.P1().X())
					{
						if ((this.p1.Y() <= ls.P2().Y() && this.p2.Y() >= ls.P2().Y()) || (this.p1.Y() >= ls.P2().Y() && this.p2.Y() <= ls.P2().Y()))
						{
							return true;
						}
					}
					else
					{
						xi = this.p1.X();
						yi = ls.A() * xi + ls.B();
					}
				}
			}
		}
		else
		{
			if (ls.AIsInf())
			{
				if (this.p1.X() == ls.P1().X())
				{
					if ((ls.P1().Y() <= this.p1.Y() && ls.p2.Y() >= this.p1.Y()) || (ls.P1().Y() >= this.p1.Y() && ls.P2().Y() <= this.p1.Y()))
					{
						return true;
					}
				}
				else
				{
					if (this.p2.X() == ls.P1().X())
					{
						if ((ls.P1().Y() <= this.p2.Y() && ls.p2.Y() >= this.p2.Y()) || (ls.P1().Y() >= this.p2.Y() && ls.P2().Y() <= this.p2.Y()))
						{
							return true;
						}
					}
					else
					{
						xi = ls.P1().X();
						yi = this.a * xi + this.b;
					}
				}
			}
			else
			{
				if (this.a == ls.A())
				{
					if ((this.p1.Y() <= ls.P1().Y() && this.p1.Y() >= ls.P2().Y()) || (this.p1.Y() >= ls.P1().Y() && this.p1.Y() <= ls.P2().Y()))
					{
						if ((this.p1.X() <= ls.P1().X() && this.p1.X() >= ls.P2().X()) || (this.p1.X() >= ls.P1().X() && this.p1.X() <= ls.P2().X()))
						{
							return true;
						}
					}
					if ((this.p2.Y() <= ls.P1().Y() && this.p2.Y() >= ls.P2().Y()) || (this.p2.Y() >= ls.P1().Y() && this.p2.Y() <= ls.P2().Y()))
					{
						if ((this.p2.X() <= ls.P1().X() && this.p2.X() >= ls.P2().X()) || (this.p2.X() >= ls.P1().X() && this.p2.X() <= ls.P2().X()))
						{
							return true;
						}
					}
					return false;
				}
				else
				{
					xi = (this.b - ls.B())/(ls.A()-this.a);
					if (this.aIsZero)
					{
						if ((this.p1.X() <= xi && this.p2.X() >= xi) || (this.p1.X() >= xi && this.p2.X() <= xi))
						{
							if ((ls.P1().X() <= xi && ls.P2().X() >= xi) || (ls.P1().X() >= xi && ls.P2().X() <= xi))
							{
								if ((ls.P1().Y() <= this.b && ls.P2().Y() >= this.b) || (ls.P1().Y() >= this.b && ls.P2().Y() <= this.b))
								{
									return true;
								}
							}
							if ((ls.P1().DistanceTo(xi, this.b) <= threshold) || (ls.P2().DistanceTo(xi, this.b) <= threshold))
							{
								return true;
							}
						}
						return false;
					}
					else
					{
						if (ls.AIsZero())
						{
							if ((ls.P1().X() <= xi && ls.P2().X() >= xi) || (ls.P1().X() >= xi && ls.P2().X() <= xi))
							{
								if ((this.p1.Y() <= ls.B() && this.p2.Y() >= ls.B()) || (this.p1.Y() >= ls.B() && this.p2.Y() <= ls.B()))
								{
									if ((this.p1.X() <= xi && this.p2.X() >= xi) || (this.p1.X() >= xi && this.p2.X() <= xi))
									{
										return true;
									}
								}
								if ((this.p1.DistanceTo(xi, ls.B()) <= threshold) || (this.p2.DistanceTo(xi, ls.B()) <= threshold))
								{
									return true;
								}
							}
							return false;
						}
						else
						{
							yi = this.a * xi + this.b;
						}
					}
				}
			}
		}	
		if ((this.p1.X() <= xi && this.p2.X() >= xi) || (this.p1.X() >= xi && this.p2.X() <= xi))
		{
			if ((this.p1.Y() <= yi && this.p2.Y() >= yi) || (this.p1.Y() >= yi && this.p2.Y() <= yi))
			{
				if ((ls.P1().X() <= xi && ls.P2().X() >= xi) || (ls.P1().X() >= xi && ls.P2().X() <= xi))
				{
					if ((ls.P1().Y() <= yi && ls.P2().Y() >= yi) || (ls.P1().Y() >= yi && ls.P2().Y() <= yi))
					{
						return true;
					}
				}
				if (ls.P1().DistanceTo(xi, yi) <= threshold || (ls.P2().DistanceTo(xi, yi) <= threshold))
				{
					return true;
				}
			}
		}
		if ((this.p1.DistanceTo(xi, yi) <= threshold) || (this.p2.DistanceTo(xi, yi) <= threshold))
		{
			if ((ls.P1().X() <= xi && ls.P2().X() >= xi) || (ls.P1().X() >= xi && ls.P2().X() <= xi))
			{
				if ((ls.P1().Y() <= yi && ls.P2().Y() >= yi) || (ls.P1().Y() >= yi && ls.P2().Y() <= yi))
				{
					return true;
				}
			}
			if (ls.P1().DistanceTo(xi, yi) <= threshold || (ls.P2().DistanceTo(xi, yi) <= threshold))
			{
				return true;
			}
		}
		return false;
	}
	public double GetIntersectionDistance(LineSegment ls, Point referencePoint)
	{
		double xi = 0;
		double yi = 0;
		if (this.aIsInf)
		{
			if (ls.AIsInf())
			{
				double distance1 = Math.pow(this.p1.X()-referencePoint.X(),2) + Math.pow(this.p1.Y()-referencePoint.Y(), 2);
				double distance2 = Math.pow(this.p2.X()-referencePoint.X(),2) + Math.pow(this.p2.Y()-referencePoint.Y(), 2);
				return Math.min(distance1, distance2);
			}
			else
			{
				xi = this.p1.X();
				yi = ls.A() * xi + ls.B();
			}
		}
		else
		{
			if (ls.AIsInf())
			{
				xi = ls.P1().X();
				yi = this.a * xi + this.b;
			}
			else
			{
				if (this.a == ls.A())
				{
					double distance1 = Math.pow(this.p1.X()-referencePoint.X(),2) + Math.pow(this.p1.Y()-referencePoint.Y(), 2);
					double distance2 = Math.pow(this.p2.X()-referencePoint.X(),2) + Math.pow(this.p2.Y()-referencePoint.Y(), 2);
					return Math.min(distance1, distance2);
				}
				else
				{
					xi = (this.b - ls.B())/(ls.A()-this.a);
					yi = this.a * xi + this.b;
				}
			}
		}	
		return (Math.pow(xi - referencePoint.X(),2) + Math.pow(yi - referencePoint.Y(),2));
	}
}