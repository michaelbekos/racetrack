package src.main.java.logic.utils;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;
import src.main.java.logic.Line2D;
import src.main.java.logic.AIstar.LineSegment;
import src.main.java.logic.AIstar.Point;

/**
 * Class which provides utility functions for our AI.
 * 
 * @author Henry
 */
public class AIUtils
{
	/** Calculates the sequence of accelerations required to move from a starting position to an end position with given starting and end speed in minimal number of turns. 
	 * 
	 * @param startPosition The starting position.
	 * @param endPosition The end position.
	 * @param startSpeed The starting speed. 
	 * @param endSpeed The end speed.
	 * @return list of accelerations for minimal sequence of moves
	 */
	public static List<Point2D> CalculateAccelerations(Point2D startPosition, Point2D endPosition, Point2D startSpeed, Point2D endSpeed, List<LineSegment> borders)
	{
		//initialize some variables
		LinkedList<Point2D> accelerations = new LinkedList<Point2D>();
		int x1 = (int)startPosition.getX();
		int y1 = (int)startPosition.getY();
		int sx1 = (int)startSpeed.getX();
		int sy1 = (int)startSpeed.getY();
		int x2 = (int)endPosition.getX();
		int y2 = (int)endPosition.getY();
		int sx2 = (int)endSpeed.getX();
		int sy2 = (int)endSpeed.getY();
		//calculate total distance and distance passed by accelerating
		int Dx = x2 - x1;
		//contains one even number in multiplication -> result is integer
		int Ax = (int)Math.abs(sx2-sx1) * sx1 + (int)Math.signum(sx2 - sx1)*(int)Math.abs(sx2 - sx1)*((int)Math.abs(sx2 - sx1) + 1)/2;				
		//check, if we overshoot the goal
		boolean overshootX = false;
		if (Math.signum(sx2)*(Dx-Ax) <= 0)
		{
			overshootX = true;
		}
		//now do the same in y-direction
		int Dy = y2 - y1;
		int Ay = (int)Math.abs(sy2-sy1) * sy1 + (int)Math.signum(sy2 - sy1)*(int)Math.abs(sy2 - sy1)*((int)Math.abs(sy2 - sy1) + 1)/2;	
		boolean overshootY = false;
		if (Math.signum(sy2)*(Dy-Ay) <= 0)
		{
			overshootY = true;
		}
		//check if starting or end speed are closer to the maximum speed
		int sxForZxCalculation;
		int syForZyCalculation;
		if (Math.abs(Dx-sx2)<=Math.abs(Dx-sx1))
		{
			sxForZxCalculation = sx2;
		}
		else
		{
			sxForZxCalculation = sx1;
		}
		if (Math.abs(Dy-sy2)<=Math.abs(Dy-sy1))
		{
			syForZyCalculation = sy2;
		}
		else
		{
			syForZyCalculation = sy1;
		}
		//calculate additional turns needed depending on the two cases: max speed is only hold one turn or max speed is hold two turns
		int zx_even = 2*(int)Math.ceil(- sxForZxCalculation * (int)Math.signum(Dx-Ax) + Math.sqrt(sxForZxCalculation * sxForZxCalculation + Math.abs(Dx-Ax)));
		int zx_odd = 2*(int)Math.ceil(- sxForZxCalculation * (int)Math.signum(Dx-Ax) - 0.5f + Math.sqrt(sxForZxCalculation * sxForZxCalculation + 0.25f + Math.abs(Dx-Ax))) + 1;
		int zy_even = 2*(int)Math.ceil(- syForZyCalculation * (int)Math.signum(Dy-Ay) + Math.sqrt(syForZyCalculation * syForZyCalculation + Math.abs(Dy-Ay)));
		int zy_odd = 2*(int)Math.ceil(- syForZyCalculation * (int)Math.signum(Dy-Ay) - 0.5f + Math.sqrt(syForZyCalculation * syForZyCalculation + 0.25f + Math.abs(Dy-Ay))) + 1;
		int zx;
		boolean zxIsEven;
		int zy;
		boolean zyIsEven;
		if (zx_even < zx_odd)
		{
			zx = zx_even;
			zxIsEven = true;
		}
		else
		{
			zx = zx_odd;
			zxIsEven = false;
		}
		if (zy_even < zy_odd)
		{
			zy = zy_even;
			zyIsEven = true;
		}
		else
		{
			zy = zy_odd;
			zyIsEven = false;
		}
		//we need at least z additional steps
		int tx = Math.abs(sx2 - sx1) + zx; 	
		int ty = Math.abs(sy2 - sy1) + zy;
		//if we overshoot in both directions we can assume that this edge is not part of the best path
		if (overshootX && overshootY)
		{
			return null;
		}
		if (((tx > ty) || overshootY) && !overshootX)
		{
			//x is the dominant direction
			//calculate minimal covered distance
			double sminY_double = (sy1  + sy2 - Math.signum(sy2)*tx)/2.0f;
			int sminY;
			//check if in this case we hold min speed for 2 turns
			boolean sminYHoldForTwoTurns = ((int) Math.floor(sminY_double) != (int) Math.ceil(sminY_double));
			if (Math.signum(sminY_double)>0)
			{
				sminY = (int) Math.floor(sminY_double);
			}
			else
			{
				sminY = (int) Math.ceil(sminY_double);
			}
			int dminY;
			//calculate the minimal distance covered depending on the case
			if (!sminYHoldForTwoTurns)
			{
				dminY = (int) (Math.abs(sminY - sy1) * sy1 - (int)Math.signum(sy1-sminY)*(Math.abs(sminY - sy1) * (Math.abs(sminY  - sy1) + 1))/2 + Math.abs(sy2 - sminY) * sminY + (int)Math.signum(sy2-sminY)*(Math.abs(sy2 - sminY) * (Math.abs(sy2  - sminY) + 1))/2);
			}
			else
			{
				dminY = (int) (Math.abs(sminY - sy1) * sy1 - (int)Math.signum(sy1-sminY)*(Math.abs(sminY - sy1) * (Math.abs(sminY  - sy1) + 1))/2 + (Math.abs(sy2 - sminY) + 1) * sminY + (int)Math.signum(sy2-sminY)*(Math.abs(sy2 - sminY) * (Math.abs(sy2  - sminY) + 1))/2);
			}
			//the following condition checks, if dmin < D -> if sy2 is in the same direction as the difference of distances, we are able to decrease the distance
			if (Math.signum(sy2) * (Dy-dminY) >= 0 && tx >= Math.abs(sy2-sy1))
			{
				int deltaDY = (int)Math.signum(sy2) * (Dy-dminY);
				int layersSkippedY;
				int additionalPointsSkippedY;
				int doNotAccelerateY;
				int sminReachedY;
				//calculate layersSkipped, actual reached smin, turns we don't accelerate at and turns we don't accelerate again after accelerating once
				if (!sminYHoldForTwoTurns)
				{
					layersSkippedY = (int)Math.floor(Math.sqrt(Math.abs(deltaDY)));
					additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
					doNotAccelerateY = 2*(layersSkippedY) - additionalPointsSkippedY;
					sminReachedY = sminY + (int)Math.signum(sy2) * layersSkippedY;
				}
				else
				{
					layersSkippedY = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDY)));
					additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY - layersSkippedY;
					doNotAccelerateY = 2*(layersSkippedY+1) - 1  - additionalPointsSkippedY;
					sminReachedY = sminY + (int)Math.signum(sy2) * layersSkippedY;
				}
				int dmaxX;
				int deltaDX;
				int layersSkippedX;
				int additionalPointsSkippedX;
				int doNotAccelerateX;
				int smaxReachedX;
				/*if (doNotAccelerateY == tx && (int)Math.signum(sy1-sminReachedY) == (int)Math.signum(sy1-sy2) && Math.abs(sy1-sminReachedY)+1 == Math.abs(sy1-sy2))
				{
					sminReachedY += (int)Math.signum(sy1-sy2);
					additionalPointsSkippedY = doNotAccelerateY - 3;
					doNotAccelerateY = 1;
				}
				if (sminReachedY == sy2 && Math.abs(sy1-sy2) == 2)
				{
					sminReachedY = sy1;
					doNotAccelerateY = 0;
					additionalPointsSkippedY = doNotAccelerateY + additionalPointsSkippedY; 
				}*/
				//do the same for x-coordinate
				int smaxX;
				if (zxIsEven)
				{
					dmaxX = Ax + zx * sxForZxCalculation + (int)Math.signum(sx2) * (zx/2-1)*(zx/2) + (int)Math.signum(sx2) * zx/2;
					deltaDX = Math.abs(Dx - dmaxX);
					layersSkippedX = (int)Math.floor(Math.sqrt(Math.abs(deltaDX)));
					additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
					doNotAccelerateX= 2*(layersSkippedX) - additionalPointsSkippedX;
					smaxReachedX = sxForZxCalculation + (int)Math.signum(sx2) * zx/2 - (int)Math.signum(sx2) * layersSkippedX;
					smaxX = sxForZxCalculation + (int)Math.signum(sx2) * zx/2;
				}
				else
				{
					dmaxX = Ax + zx * sxForZxCalculation + (int)Math.signum(sx2) * ((zx-1)/2+1)*((zx-1)/2);
					deltaDX = Math.abs(Dx - dmaxX);
					layersSkippedX = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDX)));
					additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX - layersSkippedX;
					doNotAccelerateX= 2*(layersSkippedX+1) - 1 - additionalPointsSkippedX;
					smaxReachedX = sxForZxCalculation + (int)Math.signum(sx2) * (zx-1)/2 - (int)Math.signum(sx2) * layersSkippedX;
					smaxX = sxForZxCalculation + (int)Math.signum(sx2) * (zx-1)/2;
				}
				//initialize stuff for the output
				boolean skippingX = false;
				boolean skippingY = false;
				boolean additionalSkippingX = false;
				boolean additionalSkippingY = false;
				boolean sminYReached = false;
				boolean smaxXReached = false;
				int skippedX = 0;
				int skippedY = 0;
				int additionalSkippedX = 0;
				int additionalSkippedY = 0;
				int x = x1;
				int y = y1;
				int sx = sx1;
				int sy = sy1;
				int ax = 0;
				int ay = 0;
//				for (int t = 0; t < tx; t++)
//				{
//					if(!smaxXReached)
//					{
//						//we have not reaches smax yet
//						if (sx2 == smaxReachedX && (int)Math.abs(sx - sx2) == 1 && t+1+doNotAccelerateX < tx)
//						{
//							//special case: sx2 = smax and we also have to skip some turns in smax -> skip them from the left and not from the right
//							ax = 0;
//							skippedX += 1;
//						}
//						else
//						{
//							//increase speed to smax
//							ax = (int)Math.signum(smaxReachedX-sx);
//						}
//						sx = sx + ax;
//						if (sx == smaxReachedX)
//						{
//							//did we reach smax?
//							smaxXReached = true;
//							skippingX = true;
//							if (t == 0)
//							{
//								//special case: we did reach smax in first turn
//								if (ax == 0)
//								{
//									//we did not accelerate -> we already skipped one of the turns we are not allowed to accelerate
//									skippedX += 1;
//								}
//								else
//								{
//									if (Math.signum(ax) == Math.signum(sx2-sx1))
//									{
//										//we did decelerate -> we missed one of the doNotAcceleratePoints -> add it to the additionalPointsSkipped
//										skippedX += 2;
//										doNotAccelerateX -=1;
//										additionalPointsSkippedX += 1;
//									}
//								}
//								if (skippedX > doNotAccelerateX)
//								{
//									//we already did not accelerate enough
//									sx = sx - ax;
//									ax = (int)Math.signum(sx2 - sx1);
//									sx = sx + ax;
//									additionalSkippingX = true;
//									skippingX = false;
//								}
//							}			
//						}
//					}
//					else
//					{
//						//we reached smax
//						if (skippedX == doNotAccelerateX)
//						{
//							//we skipped enough -> don't skip again
//							skippingX = false;
//						}
//						if(skippingX)
//						{
//							//we still have to skip accelerations
//							skippedX += 1;
//							ax = 0;
//						}
//						else
//						{
//							//we already skipped accelerating after reaching smax
//							if (additionalSkippingX && (additionalSkippedX < additionalPointsSkippedX))
//							{
//								//we still have to skip accelerating in the next layer
//								additionalSkippedX += 1;
//								ax = 0;
//							}
//							else
//							{
//								//everything is already handled, now accelerate to sx2
//								ax = (int)Math.signum(sx2-sx);
//								sx = sx + ax;
//								additionalSkippingX = true;
//							}
//						}
//					}
//					if(!sminYReached)
//					{
//						//now do the same for y
//						if (sy2 == sminReachedY && (int)Math.abs(sy - sy2) == 1 && t+1+doNotAccelerateY < tx)
//						{
//							//special case: sx2 = smax and we also have to skip some turns in smax -> skip them from the left and not from the right
//							ay = 0;
//							skippedY += 1;
//						}
//						else
//						{
//							//increase speed to smax
//							ay = (int)Math.signum(sminReachedY-sy);
//						}
//						sy = sy + ay;
//						if (sy == sminReachedY)
//						{
//							sminYReached = true;
//							skippingY = true;
//							if (t == 0)
//							{
//								if (ay == 0)
//								{
//									skippedY += 1;
//								}
//								else
//								{
//									if (Math.signum(ay) == Math.signum(sy2-sy1))
//									{
//										skippedY += 2;
//										doNotAccelerateY -=1;
//										additionalPointsSkippedY += 1;
//									}
//								}
//								if (skippedY > doNotAccelerateY)
//								{
//									sy = sy - ay;
//									ay = (int)Math.signum(sy2 - sy1);
//									sy = sy + ay;
//									additionalSkippingY = true;
//									skippingY = false;
//								}
//							}
//						}
//					}
//					else
//					{
//						if (skippedY == doNotAccelerateY)
//						{
//							skippingY = false;
//						}
//						if(skippingY)
//						{
//							skippedY += 1;
//							ay = 0;
//						}
//						else
//						{
//							if (additionalSkippingY && (additionalSkippedY < additionalPointsSkippedY))
//							{
//								additionalSkippedY += 1;
//								ay = 0;
//							}
//							else
//							{
//								ay = (int)Math.signum(sy2-sy);
//								sy = sy + ay;
//								additionalSkippingY = true;
//							}
//						}
//					}
//					int newX = x + sx;
//					int newY = y + sy;
//					for (LineSegment border : borders)
//					{
//						if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
//						{
//							return null;
//						}
//					}
//					x = newX;
//					y = newY;
//					accelerations.add(new Point2D(ax,ay));
//				}
				if (sy1 == sminY)
				{
					sminYReached = true;
				}
				if (sx1 == smaxX)
				{
					smaxXReached = true;
				}
				int speedsX[] = new int[tx];
				int speedsY[] = new int[tx];
				boolean holdForTwoTurnsX = false;
				boolean holdForTwoTurnsY = false;
				for (int t = 0; t < tx; t++)
				{
					if(!smaxXReached)
					{
						//increase speed to smax
						ax = (int)Math.signum(smaxX-sx);
						sx = sx + ax;
						if (sx == smaxX)
						{
							//did we reach smax?
							smaxXReached = true;			
						}
					}
					else
					{
						//we reached smax
						if (!holdForTwoTurnsX && !zxIsEven)
						{
							holdForTwoTurnsX = true;
						}
						else
						{
							//everything is already handled, now accelerate to sx2
							ax = (int)Math.signum(sx2-sx);
							sx = sx + ax;
						}
					}
					if(!sminYReached)
					{
						//increase speed to smax
						ay = (int)Math.signum(sminY-sy);
						sy = sy + ay;
						if (sy == sminY)
						{
							//did we reach smax?
							sminYReached = true;			
						}
					}
					else
					{
						if (!holdForTwoTurnsY && sminYHoldForTwoTurns)
						{
							holdForTwoTurnsY = true;
						}
						else
						{
							ay = (int)Math.signum(sy2-sy);
							sy = sy + ay;
							additionalSkippingY = true;
						}
					}
					int newX = x + sx;
					int newY = y + sy;
					/*for (LineSegment border : borders)
					{
						if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
						{
							return null;
						}
					}*/
					x = newX;
					y = newY;
					speedsX[t] = sx;
					speedsY[t] = sy;
				}
				int distanceX = Math.abs(x-x2);
				int distanceY = Math.abs(y-y2);
				int potentialSpeed = smaxX;
				int directionToSx2= (int)Math.signum(sx2 - smaxX);
				while (distanceX != 0)
				{
					int potentialTurn = -1;
					for (int t = speedsX.length - 2; t > 0; t--)
					{
						if (speedsX[t] == potentialSpeed && ((speedsX[t + 1] == potentialSpeed + directionToSx2 && speedsX[t - 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed  + directionToSx2)))
						{
							potentialTurn = t;
							break;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsX[0] == potentialSpeed && Math.abs(speedsX[0] + directionToSx2 - sx1) <= 1 && Math.abs(speedsX[0] + directionToSx2 - speedsX[1]) <= 1)
						{
							potentialTurn = 0;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsX[speedsX.length -2] == potentialSpeed && speedsX[speedsX.length -3] == potentialSpeed && speedsX[speedsX.length -1] == potentialSpeed + directionToSx2)
						{
							potentialTurn = speedsX.length -2;
						}
					}
					if (potentialTurn == -1)
					{
						boolean allSpeedsTheSame = true;
						for (int t = 0; t < speedsX.length; t++)
						{
							if (speedsX[t] != potentialSpeed)
							{
								allSpeedsTheSame = false;
								break;
							}
						}
						if (allSpeedsTheSame)
						{
							potentialTurn = speedsX.length - 2;
						}
					}
					if (potentialTurn != -1)
					{
						speedsX[potentialTurn] = speedsX[potentialTurn] + directionToSx2;
						distanceX--;
					}
					else
					{
						potentialSpeed = potentialSpeed + directionToSx2;
						if (Math.abs(potentialSpeed - sx2) > tx)
						{
							System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
							return null;
						}
					}
				}
				potentialSpeed = sminY;
				int directionToSy2= (int)Math.signum(sy2 - sminY);
				while (distanceY != 0)
				{
					int potentialTurn = -1;
					
					for (int t = speedsY.length - 2; t > 0; t--)
					{
						if (speedsY[t] == potentialSpeed && ((speedsY[t + 1] == potentialSpeed + directionToSy2 && speedsY[t - 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed  + directionToSy2)))
						{
							potentialTurn = t;
							break;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsY[0] == potentialSpeed && Math.abs(speedsY[0] + directionToSy2 - sy1) <= 1)
						{
							potentialTurn = 0;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsY[speedsY.length -2] == potentialSpeed && speedsY[speedsY.length -3] == potentialSpeed && speedsY[speedsY.length -1] == potentialSpeed + directionToSy2)
						{
							potentialTurn = speedsY.length -2;
						}
					}
					if (potentialTurn == -1)
					{
						boolean allSpeedsTheSame = true;
						for (int t = 0; t < speedsY.length; t++)
						{
							if (speedsY[t] != potentialSpeed)
							{
								allSpeedsTheSame = false;
								break;
							}
						}
						if (allSpeedsTheSame)
						{
							potentialTurn = speedsY.length - 2;
						}
					}
					if (potentialTurn != -1)
					{
						speedsY[potentialTurn] = speedsY[potentialTurn] + directionToSy2;
						distanceY--;
					}
					else
					{
						potentialSpeed = potentialSpeed + directionToSy2;
						if (Math.abs(potentialSpeed - sy2) > tx)
						{
							System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
							return null;
						}
					}
				}
				x = x1;
				y = y1;
				for (int t = 0; t < tx; t++)
				{
					int newX = x + speedsX[t];
					int newY = y + speedsY[t];
					for (LineSegment border : borders)
					{
						if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
						{
							return null;
						}
					}
					x = newX;
					y = newY;
					if (t == 0)
					{
						accelerations.add(new Point2D(speedsX[t]-sx1,speedsY[t]-sy1));
					}
					else
					{
						accelerations.add(new Point2D(speedsX[t]-speedsX[t-1],speedsY[t]-speedsY[t-1]));
					}
				}
			}
			else
			{
				//we have to compute a new smin -> two cases again
				int sminY_even;
				int sminY_odd; 
				double sminY_double_even = Math.sqrt((sy1*sy1 + sy2*sy2 + (int)Math.abs(sy2) - (int)Math.signum(sy2)*sy1 - 2*Math.signum(sy2)*Dy)/2.0f)*(-Math.signum(sy2));
				double sminY_double_odd = Math.signum(sy2) - Math.sqrt(0.25+(sy1*sy1 + sy2*sy2 + (int)Math.abs(sy2) - (int)Math.signum(sy2)*sy1 - 2*Math.signum(sy2)*Dy)/2.0f)*(Math.signum(sy2));
				if (Math.signum(sy2)>0)
				{
					sminY_even = (int) Math.floor(sminY_double_even);
					sminY_odd = (int) Math.floor(sminY_double_odd);
				}
				else
				{
					sminY_even = (int) Math.ceil(sminY_double_even);
					sminY_odd = (int) Math.ceil(sminY_double_odd);
				}
				//the new smin yields us a new t
				int t_new;
				int t_new_even = (int)Math.signum(sy2)*(sy1-sminY_even) + (int)Math.signum(sy2) *(sy2 - sminY_even);
				int t_new_odd = (int)Math.signum(sy2)*(sy1-sminY_odd) + (int)Math.signum(sy2) *(sy2 - sminY_odd) + 1;
				if (t_new_even < t_new_odd)
				{
					t_new = t_new_even;
					sminY = sminY_even;
					sminYHoldForTwoTurns = false;
				}
				else
				{
					t_new = t_new_odd;
					sminY = sminY_odd;
					sminYHoldForTwoTurns = true;
				}
				//and the new t a new zx
				int zx_new = t_new - ((int)Math.abs(sx2 - sx1));
				if (zx_new%2 != 0)
				{
					zxIsEven = false;
				}
				else
				{
					zxIsEven = true;
				}
				//now do everything just as before
				zx = zx_new;
				if (!sminYHoldForTwoTurns)
				{
					dminY = (int) (Math.abs(sminY - sy1) * sy1 - (int)Math.signum(sy1-sminY)*(Math.abs(sminY - sy1) * (Math.abs(sminY  - sy1) + 1))/2 + Math.abs(sy2 - sminY) * sminY + (int)Math.signum(sy2-sminY)*(Math.abs(sy2 - sminY) * (Math.abs(sy2  - sminY) + 1))/2);
				}
				else
				{
					dminY = (int) (Math.abs(sminY - sy1) * sy1 - (int)Math.signum(sy1-sminY)*(Math.abs(sminY - sy1) * (Math.abs(sminY  - sy1) + 1))/2 + (Math.abs(sy2 - sminY) + 1) * sminY + (int)Math.signum(sy2-sminY)*(Math.abs(sy2 - sminY) * (Math.abs(sy2  - sminY) + 1))/2);
				}					
				int deltaDY = (int)Math.signum(sy2) * (Dy-dminY);
				int layersSkippedY;
				int additionalPointsSkippedY;
				int doNotAccelerateY;
				int sminReachedY;
				if (!sminYHoldForTwoTurns)
				{
					layersSkippedY = (int)Math.floor(Math.sqrt(Math.abs(deltaDY)));
					additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
					doNotAccelerateY = 2*(layersSkippedY) - additionalPointsSkippedY;
					sminReachedY = sminY + (int)Math.signum(sy2) * layersSkippedY;
				}
				else
				{
					layersSkippedY = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDY)));
					additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY - layersSkippedY;
					doNotAccelerateY = 2*(layersSkippedY+1) - 1  - additionalPointsSkippedY;
					sminReachedY = sminY + (int)Math.signum(sy2) * layersSkippedY;
				}
				if (doNotAccelerateY == t_new && (int)Math.signum(sy1-sminReachedY) == (int)Math.signum(sy1-sy2) && Math.abs(sy1-sminReachedY)+1 == Math.abs(sy1-sy2))
				{
					sminReachedY += (int)Math.signum(sy1-sy2);
					additionalPointsSkippedY = doNotAccelerateY - 3;
					doNotAccelerateY = 1;
				}
				if (sminReachedY == sy2 && Math.abs(sy1-sy2) == 2)
				{
					sminReachedY = sy1;
					doNotAccelerateY = 0;
					additionalPointsSkippedY = doNotAccelerateY + additionalPointsSkippedY; 
				}
				int dmaxX;
				int deltaDX;
				int layersSkippedX;
				int additionalPointsSkippedX;
				int doNotAccelerateX;
				int smaxReachedX;
				int smaxX;
				if (zxIsEven)
				{
					dmaxX = Ax + zx * sxForZxCalculation + (int)Math.signum(sx2) * (zx/2-1)*(zx/2) + (int)Math.signum(sx2) * zx/2;
					deltaDX = Math.abs(Dx - dmaxX);
					layersSkippedX = (int)Math.floor(Math.sqrt(Math.abs(deltaDX)));
					additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
					doNotAccelerateX= 2*(layersSkippedX) - additionalPointsSkippedX;
					smaxReachedX = sxForZxCalculation + (int)Math.signum(sx2) * zx/2 - (int)Math.signum(sx2) * layersSkippedX;
					smaxX = sxForZxCalculation + (int)Math.signum(sx2) * zx/2;
				}
				else
				{
					dmaxX = Ax + zx * sxForZxCalculation + (int)Math.signum(sx2) * ((zx-1)/2+1)*((zx-1)/2);
					deltaDX = Math.abs(Dx - dmaxX);
					layersSkippedX = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDX)));
					additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX - layersSkippedX;
					doNotAccelerateX= 2*(layersSkippedX+1) - 1 - additionalPointsSkippedX;
					smaxReachedX = sxForZxCalculation + (int)Math.signum(sx2) * (zx-1)/2 - (int)Math.signum(sx2) * layersSkippedX;
					smaxX = sxForZxCalculation + (int)Math.signum(sx2) * (zx-1)/2;
				}
				boolean skippingX = false;
				boolean skippingY = false;
				boolean additionalSkippingX = false;
				boolean additionalSkippingY = false;
				boolean sminYReached = false;
				boolean smaxXReached = false;
				int skippedX = 0;
				int skippedY = 0;
				int additionalSkippedX = 0;
				int additionalSkippedY = 0;
				int sx = sx1;
				int sy = sy1;
				int ax = 0;
				int ay = 0;
				int x = x1;
				int y = y1;
//				for (int t = 0; t < t_new; t++)
//				{
//					if(!smaxXReached)
//					{
//						if (sx2 == smaxReachedX && (int)Math.abs(sx - sx2) == 1 && t+1+doNotAccelerateX < t_new)
//						{
//							ax = 0;
//							skippedX += 1;
//						}
//						else
//						{
//							ax = (int)Math.signum(smaxReachedX-sx);
//						}
//						sx = sx + ax;
//						if (sx == smaxReachedX)
//						{
//							smaxXReached = true;
//							skippingX = true;
//							if (t == 0)
//							{
//								if (ax == 0)
//								{
//									skippedX += 1;
//								}
//								else
//								{
//									if (Math.signum(ax) == Math.signum(sx2-sx1))
//									{
//										skippedX += 2;
//										doNotAccelerateX -=1;
//										additionalPointsSkippedX += 1;
//									}
//								}
//								if (skippedX > doNotAccelerateX)
//								{
//									sx = sx - ax;
//									ax = (int)Math.signum(sx2 - sx1);
//									sx = sx + ax;
//									additionalSkippingX = true;
//									skippingX = false;
//								}
//							}			
//						}
//					}
//					else
//					{
//						if (skippedX == doNotAccelerateX)
//						{
//							skippingX = false;
//						}
//						if(skippingX)
//						{
//							skippedX += 1;
//							ax= 0;
//						}
//						else
//						{
//							if (additionalSkippingX && (additionalSkippedX < additionalPointsSkippedX))
//							{
//								additionalSkippedX += 1;
//								ax = 0;
//							}
//							else
//							{
//								ax = (int)Math.signum(sx2-sx);
//								sx = sx + ax;
//								additionalSkippingX = true;
//							}
//						}
//					}
//					if(!sminYReached)
//					{
//						if (sy2 == sminReachedY && (int)Math.abs(sy - sy2) == 1 && t+1+doNotAccelerateY < t_new)
//						{
//							//special case: sx2 = smax and we also have to skip some turns in smax -> skip them from the left and not from the right
//							ay = 0;
//							skippedY += 1;
//						}
//						else
//						{
//							//increase speed to smax
//							ay = (int)Math.signum(sminReachedY-sy);
//						}
//						sy = sy + ay;
//						if (sy == sminReachedY)
//						{
//							sminYReached = true;
//							skippingY = true;
//							if (t == 0)
//							{
//								if (ay == 0)
//								{
//									skippedY += 1;
//								}
//								else
//								{
//									if (Math.signum(ay) == Math.signum(sy2-sy1))
//									{
//										skippedY += 2;
//										doNotAccelerateY -=1;
//										additionalPointsSkippedY += 1;
//									}
//								}
//								if (skippedY > doNotAccelerateY)
//								{
//									sy = sy - ay;
//									ay = (int)Math.signum(sy2 - sy1);
//									sy = sy + ay;
//									additionalSkippingY = true;
//									skippingY = false;
//								}
//							}
//						}
//					}
//					else
//					{
//						if (skippedY == doNotAccelerateY)
//						{
//							skippingY = false;
//						}
//						if(skippingY)
//						{
//							skippedY += 1;
//							ay = 0;
//						}
//						else
//						{
//							if (additionalSkippingY && (additionalSkippedY < additionalPointsSkippedY))
//							{
//								additionalSkippedY += 1;
//								ay = 0;
//							}
//							else
//							{
//								ay = (int)Math.signum(sy2-sy);
//								sy = sy + ay;
//								additionalSkippingY = true;
//							}
//						}
//					}
//					int newX = x + sx;
//					int newY = y + sy;
//					for (LineSegment border : borders)
//					{
//						if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
//						{
//							return null;
//						}
//					}
//					x = newX;
//					y = newY;
//					accelerations.add(new Point2D(ax,ay));
//				}
				if (sy1 == sminY)
				{
					sminYReached = true;
				}
				if (sx1 == smaxX)
				{
					smaxXReached = true;
				}
				int speedsX[] = new int[t_new];
				int speedsY[] = new int[t_new];
				boolean holdForTwoTurnsX = false;
				boolean holdForTwoTurnsY = false;
				for (int t = 0; t < t_new; t++)
				{
					if(!smaxXReached)
					{
						//increase speed to smax
						ax = (int)Math.signum(smaxX-sx);
						sx = sx + ax;
						if (sx == smaxX)
						{
							//did we reach smax?
							smaxXReached = true;			
						}
					}
					else
					{
						//we reached smax
						if (!holdForTwoTurnsX && !zxIsEven)
						{
							holdForTwoTurnsX = true;
						}
						else
						{
							//everything is already handled, now accelerate to sx2
							ax = (int)Math.signum(sx2-sx);
							sx = sx + ax;
						}
					}
					if(!sminYReached)
					{
						//increase speed to smax
						ay = (int)Math.signum(sminY-sy);
						sy = sy + ay;
						if (sy == sminY)
						{
							//did we reach smax?
							sminYReached = true;			
						}
					}
					else
					{
						if (!holdForTwoTurnsY && sminYHoldForTwoTurns)
						{
							holdForTwoTurnsY = true;
						}
						else
						{
							ay = (int)Math.signum(sy2-sy);
							sy = sy + ay;
							additionalSkippingY = true;
						}
					}
					int newX = x + sx;
					int newY = y + sy;
					/*for (LineSegment border : borders)
					{
						if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
						{
							return null;
						}
					}*/
					x = newX;
					y = newY;
					speedsX[t] = sx;
					speedsY[t] = sy;
				}
				int distanceX = Math.abs(x-x2);
				int distanceY = Math.abs(y-y2);
				int potentialSpeed = smaxX;
				int directionToSx2= (int)Math.signum(sx2 - smaxX);
				while (distanceX != 0)
				{
					int potentialTurn = -1;
					for (int t = speedsX.length - 2; t > 0; t--)
					{
						if (speedsX[t] == potentialSpeed && ((speedsX[t + 1] == potentialSpeed + directionToSx2 && speedsX[t - 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed  + directionToSx2)))
						{
							potentialTurn = t;
							break;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsX[0] == potentialSpeed && Math.abs(speedsX[0] + directionToSx2 - sx1) <= 1 && Math.abs(speedsX[0] + directionToSx2 - speedsX[1]) <= 1)
						{
							potentialTurn = 0;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsX[speedsX.length -2] == potentialSpeed && speedsX[speedsX.length -3] == potentialSpeed && speedsX[speedsX.length -1] == potentialSpeed + directionToSx2)
						{
							potentialTurn = speedsX.length -2;
						}
					}
					if (potentialTurn == -1)
					{
						boolean allSpeedsTheSame = true;
						for (int t = 0; t < speedsX.length; t++)
						{
							if (speedsX[t] != potentialSpeed)
							{
								allSpeedsTheSame = false;
								break;
							}
						}
						if (allSpeedsTheSame)
						{
							potentialTurn = speedsX.length - 2;
						}
					}
					if (potentialTurn != -1)
					{
						speedsX[potentialTurn] = speedsX[potentialTurn] + directionToSx2;
						distanceX--;
					}
					else
					{
						potentialSpeed = potentialSpeed + directionToSx2;
						if (Math.abs(potentialSpeed - sx2) > t_new)
						{
							System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
							return null;
						}
					}
				}
				potentialSpeed = sminY;
				int directionToSy2= (int)Math.signum(sy2 - sminY);
				while (distanceY != 0)
				{
					int potentialTurn = -1;
					
					for (int t = speedsY.length - 2; t > 0; t--)
					{
						if (speedsY[t] == potentialSpeed && ((speedsY[t + 1] == potentialSpeed + directionToSy2 && speedsY[t - 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed  + directionToSy2)))
						{
							potentialTurn = t;
							break;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsY[0] == potentialSpeed && Math.abs(speedsY[0] + directionToSy2 - sy1) <= 1)
						{
							potentialTurn = 0;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsY[speedsY.length -2] == potentialSpeed && speedsY[speedsY.length -3] == potentialSpeed && speedsY[speedsY.length -1] == potentialSpeed + directionToSy2)
						{
							potentialTurn = speedsY.length -2;
						}
					}
					if (potentialTurn == -1)
					{
						boolean allSpeedsTheSame = true;
						for (int t = 0; t < speedsY.length; t++)
						{
							if (speedsY[t] != potentialSpeed)
							{
								allSpeedsTheSame = false;
								break;
							}
						}
						if (allSpeedsTheSame)
						{
							potentialTurn = speedsY.length - 2;
						}
					}
					if (potentialTurn != -1)
					{
						speedsY[potentialTurn] = speedsY[potentialTurn] + directionToSy2;
						distanceY--;
					}
					else
					{
						potentialSpeed = potentialSpeed + directionToSy2;
						if (Math.abs(potentialSpeed - sy2) > t_new)
						{
							System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
							return null;
						}
					}
				}
				x = x1;
				y = y1;
				for (int t = 0; t < t_new; t++)
				{
					int newX = x + speedsX[t];
					int newY = y + speedsY[t];
					for (LineSegment border : borders)
					{
						if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
						{
							return null;
						}
					}
					x = newX;
					y = newY;
					if (t == 0)
					{
						accelerations.add(new Point2D(speedsX[t]-sx1,speedsY[t]-sy1));
					}
					else
					{
						accelerations.add(new Point2D(speedsX[t]-speedsX[t-1],speedsY[t]-speedsY[t-1]));
					}
				}
			}
		}
		else
		{
			if (((ty > tx) || overshootX) && !overshootY)
			{
				//y is the dominant direction -> do the same as above with y and x swapped
				double sminX_double = (sx1  + sx2 - Math.signum(sx2)*ty)/2.0f;
				int sminX;
				boolean sminXHoldForTwoTurns = ((int) Math.floor(sminX_double) != (int) Math.ceil(sminX_double));
				if (Math.signum(sminX_double)>0)
				{
					sminX = (int) Math.floor(sminX_double);
				}
				else
				{
					sminX = (int) Math.ceil(sminX_double);
				}
				int dminX;
				if (!sminXHoldForTwoTurns)
				{
					dminX = (int) (Math.abs(sminX - sx1) * sx1 - (int)Math.signum(sx1-sminX)*(Math.abs(sminX - sx1) * (Math.abs(sminX  - sx1) + 1))/2 + Math.abs(sx2 - sminX) * sminX + (int)Math.signum(sx2-sminX)*(Math.abs(sx2 - sminX) * (Math.abs(sx2  - sminX) + 1))/2);
				}
				else
				{
					dminX = (int) (Math.abs(sminX - sx1) * sx1 - (int)Math.signum(sx1-sminX)*(Math.abs(sminX - sx1) * (Math.abs(sminX  - sx1) + 1))/2 + (Math.abs(sx2 - sminX) + 1) * sminX + (int)Math.signum(sx2-sminX)*(Math.abs(sx2 - sminX) * (Math.abs(sx2  - sminX) + 1))/2);
				}
				if (Math.signum(sx2) * (Dx-dminX) >= 0  && ty >= Math.abs(sx2-sx1))
				{
					int deltaDX = (int)Math.signum(sx2) * (Dx-dminX);
					int layersSkippedX;
					int additionalPointsSkippedX;
					int doNotAccelerateX;
					int sminReachedX;
					if (!sminXHoldForTwoTurns)
					{
						layersSkippedX = (int)Math.floor(Math.sqrt(Math.abs(deltaDX)));
						additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
						doNotAccelerateX = 2*(layersSkippedX) - additionalPointsSkippedX;
						sminReachedX = sminX + (int)Math.signum(sx2) * layersSkippedX;
					}
					else
					{
						layersSkippedX = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDX)));
						additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX - layersSkippedX;
						doNotAccelerateX = 2*(layersSkippedX+1) - 1  - additionalPointsSkippedX;
						sminReachedX = sminX + (int)Math.signum(sx2) * layersSkippedX;
					}
					if (doNotAccelerateX == ty && (int)Math.signum(sx1-sminReachedX) == (int)Math.signum(sx1-sx2) && Math.abs(sx1-sminReachedX)+1 == Math.abs(sx1-sx2))
					{
						sminReachedX += (int)Math.signum(sx1-sx2);
						additionalPointsSkippedX = doNotAccelerateX - 3;
						doNotAccelerateX = 1;
					}
					if (sminReachedX == sx2 && Math.abs(sx1-sx2) == 2)
					{
						sminReachedX = sx1;
						doNotAccelerateX = 0;
						additionalPointsSkippedX = doNotAccelerateX + additionalPointsSkippedX; 
					}
					int dmaxY;
					int deltaDY;
					int layersSkippedY;
					int additionalPointsSkippedY;
					int doNotAccelerateY;
					int smaxReachedY;
					int smaxY;
					if (zyIsEven)
					{
						dmaxY = Ay + zy * syForZyCalculation + (int)Math.signum(sy2) * (zy/2-1)*(zy/2) + (int)Math.signum(sy2) * zy/2;
						deltaDY = Math.abs(Dy - dmaxY);
						layersSkippedY = (int)Math.floor(Math.sqrt(Math.abs(deltaDY)));
						additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
						doNotAccelerateY= 2*(layersSkippedY) - additionalPointsSkippedY;
						smaxReachedY = syForZyCalculation + (int)Math.signum(sy2) * zy/2 - (int)Math.signum(sy2) * layersSkippedY;
						smaxY = syForZyCalculation + (int)Math.signum(sy2) * zy/2;
					}
					else
					{
						dmaxY = Ay + zy * syForZyCalculation + (int)Math.signum(sy2) * ((zy-1)/2+1)*((zy-1)/2);
						deltaDY = Math.abs(Dy - dmaxY);
						layersSkippedY = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDY)));
						additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY - layersSkippedY;
						doNotAccelerateY= 2*(layersSkippedY+1) - 1 - additionalPointsSkippedY;
						smaxReachedY = syForZyCalculation + (int)Math.signum(sy2) * (zy-1)/2 - (int)Math.signum(sy2) * layersSkippedY;
						smaxY = syForZyCalculation + (int)Math.signum(sy2) * (zy-1)/2;
					}
					boolean skippingX = false;
					boolean skippingY = false;
					boolean additionalSkippingX = false;
					boolean additionalSkippingY = false;
					boolean smaxYReached = false;
					boolean sminXReached = false;
					int skippedX = 0;
					int skippedY = 0;
					int additionalSkippedX = 0;
					int additionalSkippedY = 0;
					int sx = sx1;
					int sy = sy1;
					int x = x1;
					int y = y1;
					int ax = 0;
					int ay = 0;
//					for (int t = 0; t < ty; t++)
//					{
//						if(!smaxYReached)
//						{
//							if (sy2 == smaxReachedY && (int)Math.abs(sy - sy2) == 1 && t+1+doNotAccelerateY < ty)
//							{
//								ay = 0;
//								skippedY += 1;
//							}
//							else
//							{
//								ay = (int)Math.signum(smaxReachedY-sy);
//							}
//							sy = sy + ay;
//							if (sy == smaxReachedY)
//							{
//								smaxYReached = true;
//								skippingY = true;
//								if (t == 0)
//								{
//									if (ay == 0)
//									{
//										skippedY += 1;
//									}
//									else
//									{
//										if (Math.signum(ay) == Math.signum(sy2-sy1))
//										{
//											skippedY += 2;
//											doNotAccelerateY -=1;
//											additionalPointsSkippedY += 1;
//										}
//									}
//									if (skippedY > doNotAccelerateY)
//									{
//										sy = sy - ay;
//										ay = (int)Math.signum(sy2 - sy1);
//										sy = sy + ay;
//										additionalSkippingY = true;
//										skippingY = false;
//									}
//								}			
//							}
//						}
//						else
//						{
//							if (skippedY == doNotAccelerateY)
//							{
//								skippingY = false;
//							}
//							if(skippingY)
//							{
//								skippedY += 1;
//								ay = 0;
//							}
//							else
//							{
//								if (additionalSkippingY && (additionalSkippedY < additionalPointsSkippedY))
//								{
//									additionalSkippedY += 1;
//									ay = 0;
//								}
//								else
//								{
//									ay = (int)Math.signum(sy2-sy);
//									sy = sy + ay;
//									additionalSkippingY = true;
//								}
//							}
//						}
//						if(!sminXReached)
//						{
//							if (sx2 == sminReachedX && (int)Math.abs(sx - sx2) == 1 && t+1+doNotAccelerateX < ty)
//							{
//								//special case: sx2 = smax and we also have to skip some turns in smax -> skip them from the left and not from the right
//								ax = 0;
//								skippedX += 1;
//							}
//							else
//							{
//								//increase speed to smax
//								ax = (int)Math.signum(sminReachedX-sx);
//							}
//							sx = sx + ax;
//							if (sx == sminReachedX)
//							{
//								sminXReached = true;
//								skippingX = true;
//								if (t == 0)
//								{
//									if (ax == 0)
//									{
//										skippedX += 1;
//									}
//									else
//									{
//										if (Math.signum(ax) == Math.signum(sx2-sx1))
//										{
//											skippedX += 2;
//											doNotAccelerateX -=1;
//											additionalPointsSkippedX += 1;
//										}
//									}
//									if (skippedX > doNotAccelerateX)
//									{
//										sx = sx - ax;
//										ax = (int)Math.signum(sx2 - sx1);
//										sx = sx + ax;
//										additionalSkippingX = true;
//										skippingX = false;
//									}
//								}
//							}
//						}
//						else
//						{
//							if (skippedX == doNotAccelerateX)
//							{
//								skippingX = false;
//							}
//							if(skippingX)
//							{
//								skippedX += 1;
//								ax = 0;
//							}
//							else
//							{
//								if (additionalSkippingX && (additionalSkippedX < additionalPointsSkippedX))
//								{
//									additionalSkippedX += 1;
//									ax = 0;
//								}
//								else
//								{
//									ax = (int)Math.signum(sx2-sx);
//									sx = sx + ax;
//									additionalSkippingX = true;
//								}
//							}
//						}
//						int newX = x + sx;
//						int newY = y + sy;
//						for (LineSegment border : borders)
//						{
//							if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
//							{
//								return null;
//							}
//						}
//						x = newX;
//						y = newY;
//						accelerations.add(new Point2D(ax,ay));
//					}
					int speedsX[] = new int[ty];
					int speedsY[] = new int[ty];
					boolean holdForTwoTurnsX = false;
					boolean holdForTwoTurnsY = false;
					if (sx1 == sminX)
					{
						sminXReached = true;
					}
					if (sy1 == smaxY)
					{
						smaxYReached = true;
					}
					for (int t = 0; t < ty; t++)
					{
						if(!smaxYReached)
						{
							//increase speed to smax
							ay = (int)Math.signum(smaxY-sy);
							sy = sy + ay;
							if (sy == smaxY)
							{
								//did we reach smax?
								smaxYReached = true;			
							}
						}
						else
						{
							//we reached smax
							if (!holdForTwoTurnsY && !zyIsEven)
							{
								holdForTwoTurnsY = true;
							}
							else
							{
								//everything is already handled, now accelerate to sx2
								ay = (int)Math.signum(sy2-sy);
								sy = sy + ay;
							}
						}
						if(!sminXReached)
						{
							//increase speed to smax
							ax = (int)Math.signum(sminX-sx);
							sx = sx + ax;
							if (sx == sminX)
							{
								//did we reach smax?
								sminXReached = true;			
							}
						}
						else
						{
							if (!holdForTwoTurnsX && sminXHoldForTwoTurns)
							{
								holdForTwoTurnsX = true;
							}
							else
							{
								ax = (int)Math.signum(sx2-sx);
								sx = sx + ax;
								additionalSkippingX = true;
							}
						}
						int newX = x + sx;
						int newY = y + sy;
						/*for (LineSegment border : borders)
						{
							if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
							{
								return null;
							}
						}*/
						x = newX;
						y = newY;
						speedsX[t] = sx;
						speedsY[t] = sy;
					}
					int distanceX = Math.abs(x-x2);
					int distanceY = Math.abs(y-y2);
					int potentialSpeed = smaxY;
					int directionToSy2= (int)Math.signum(sy2 - smaxY);
					while (distanceY != 0)
					{
						int potentialTurn = -1;
						for (int t = speedsY.length - 2; t > 0; t--)
						{
							if (speedsY[t] == potentialSpeed && ((speedsY[t + 1] == potentialSpeed + directionToSy2 && speedsY[t - 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed  + directionToSy2)))
							{
								potentialTurn = t;
								break;
							}
						}
						if (potentialTurn == -1)
						{
							if (speedsY[0] == potentialSpeed && Math.abs(speedsY[0] + directionToSy2 - sy1) <= 1 && Math.abs(speedsY[0] + directionToSy2 - speedsY[1]) <= 1)
							{
								potentialTurn = 0;
							}
						}
						if (potentialTurn == -1)
						{
							if (speedsY[speedsY.length -2] == potentialSpeed && speedsY[speedsY.length -3] == potentialSpeed && speedsY[speedsY.length -1] == potentialSpeed + directionToSy2)
							{
								potentialTurn = speedsY.length -2;
							}
						}
						if (potentialTurn == -1)
						{
							boolean allSpeedsTheSame = true;
							for (int t = 0; t < speedsY.length; t++)
							{
								if (speedsY[t] != potentialSpeed)
								{
									allSpeedsTheSame = false;
									break;
								}
							}
							if (allSpeedsTheSame)
							{
								potentialTurn = speedsY.length - 2;
							}
						}
						if (potentialTurn != -1)
						{
							speedsY[potentialTurn] = speedsY[potentialTurn] + directionToSy2;
							distanceY--;
						}
						else
						{
							potentialSpeed = potentialSpeed + directionToSy2;
							if (Math.abs(potentialSpeed - sy2) > ty)
							{
								System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
								return null;
							}
						}
					}
					potentialSpeed = sminX;
					int directionToSx2= (int)Math.signum(sx2 - sminX);
					while (distanceX != 0)
					{
						int potentialTurn = -1;
						for (int t = speedsX.length - 2; t > 0; t--)
						{
							if (speedsX[t] == potentialSpeed && ((speedsX[t + 1] == potentialSpeed + directionToSx2 && speedsX[t - 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed  + directionToSx2)))
							{
								potentialTurn = t;
								break;
							}
						}
						if (potentialTurn == -1)
						{
							if (speedsX[0] == potentialSpeed && Math.abs(speedsX[0] + directionToSx2 - sx1) <= 1)
							{
								potentialTurn = 0;
							}
						}
						if (potentialTurn == -1)
						{
							if (speedsX[speedsX.length -2] == potentialSpeed && speedsX[speedsX.length -3] == potentialSpeed && speedsX[speedsX.length -1] == potentialSpeed + directionToSx2)
							{
								potentialTurn = speedsX.length -2;
							}
						}
						if (potentialTurn == -1)
						{
							boolean allSpeedsTheSame = true;
							for (int t = 0; t < speedsX.length; t++)
							{
								if (speedsX[t] != potentialSpeed)
								{
									allSpeedsTheSame = false;
									break;
								}
							}
							if (allSpeedsTheSame)
							{
								potentialTurn = speedsX.length - 2;
							}
						}
						if (potentialTurn != -1)
						{
							speedsX[potentialTurn] = speedsX[potentialTurn] + directionToSx2;
							distanceX--;
						}
						else
						{
							potentialSpeed = potentialSpeed + directionToSx2;
							if (Math.abs(potentialSpeed - sx2) > ty)
							{
								System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
								return null;
							}
						}
					}
					x = x1;
					y = y1;
					for (int t = 0; t < ty; t++)
					{
						int newX = x + speedsX[t];
						int newY = y + speedsY[t];
						for (LineSegment border : borders)
						{
							if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
							{
								return null;
							}
						}
						x = newX;
						y = newY;
						if (t == 0)
						{
							accelerations.add(new Point2D(speedsX[t]-sx1,speedsY[t]-sy1));
						}
						else
						{
							accelerations.add(new Point2D(speedsX[t]-speedsX[t-1],speedsY[t]-speedsY[t-1]));
						}
					}
				}
				else
				{
					int sminX_even;
					int sminX_odd; 
					double sminX_double_even = Math.sqrt((sx1*sx1 + sx2*sx2 + (int)Math.abs(sx2) - (int)Math.signum(sx2)*sx1 - 2*Math.signum(sx2)*Dx)/2.0f)*(-Math.signum(sx2));
					double sminX_double_odd = Math.signum(sx2) - Math.sqrt(0.25+(sx1*sx1 + sx2*sx2 + (int)Math.abs(sx2) - (int)Math.signum(sx2)*sx1 - 2*Math.signum(sx2)*Dx)/2.0f)*(Math.signum(sx2));
					if (Math.signum(sx2)>0)
					{
						sminX_even = (int) Math.floor(sminX_double_even);
						sminX_odd = (int) Math.floor(sminX_double_odd);
					}
					else
					{
						sminX_even = (int) Math.ceil(sminX_double_even);
						sminX_odd = (int) Math.ceil(sminX_double_odd);
					}
					int t_new;
					int t_new_even = (int)Math.signum(sx2)*(sx1-sminX_even) + (int)Math.signum(sx2) *(sx2 - sminX_even);
					int t_new_odd = (int)Math.signum(sx2)*(sx1-sminX_odd) + (int)Math.signum(sx2) *(sx2 - sminX_odd) + 1;
					if (t_new_even < t_new_odd)
					{
						t_new = t_new_even;
						sminX = sminX_even;
						sminXHoldForTwoTurns = false;
					}
					else
					{
						t_new = t_new_odd;
						sminX = sminX_odd;
						sminXHoldForTwoTurns = true;
					}
					int zy_new = t_new - ((int)Math.abs(sy2 - sy1));
					if (zy_new%2 != 0)
					{
						zyIsEven = false;
					}
					else
					{
						zyIsEven = true;
					}
					zy = zy_new;
					if (!sminXHoldForTwoTurns)
					{
						dminX = (int) (Math.abs(sminX - sx1) * sx1 - (int)Math.signum(sx1-sminX)*(Math.abs(sminX - sx1) * (Math.abs(sminX  - sx1) + 1))/2 + Math.abs(sx2 - sminX) * sminX + (int)Math.signum(sx2-sminX)*(Math.abs(sx2 - sminX) * (Math.abs(sx2  - sminX) + 1))/2);
					}
					else
					{
						dminX = (int) (Math.abs(sminX - sx1) * sx1 - (int)Math.signum(sx1-sminX)*(Math.abs(sminX - sx1) * (Math.abs(sminX  - sx1) + 1))/2 + (Math.abs(sx2 - sminX) + 1) * sminX + (int)Math.signum(sx2-sminX)*(Math.abs(sx2 - sminX) * (Math.abs(sx2  - sminX) + 1))/2);
					}					
					int deltaDX = (int)Math.signum(sx2) * (Dx-dminX);
					int layersSkippedX;
					int additionalPointsSkippedX;
					int doNotAccelerateX;
					int sminReachedX;
					if (!sminXHoldForTwoTurns)
					{
						layersSkippedX = (int)Math.floor(Math.sqrt(Math.abs(deltaDX)));
						additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
						doNotAccelerateX = 2*(layersSkippedX) - additionalPointsSkippedX;
						sminReachedX = sminX + (int)Math.signum(sx2) * layersSkippedX;
					}
					else
					{
						layersSkippedX = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDX)));
						additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX - layersSkippedX;
						doNotAccelerateX = 2*(layersSkippedX+1) - 1  - additionalPointsSkippedX;
						sminReachedX = sminX + (int)Math.signum(sx2) * layersSkippedX;
					}
					if (doNotAccelerateX == t_new && (int)Math.signum(sx1-sminReachedX) == (int)Math.signum(sx1-sx2) && Math.abs(sx1-sminReachedX)+1 == Math.abs(sx1-sx2))
					{
						sminReachedX += (int)Math.signum(sx1-sx2);
						additionalPointsSkippedX = doNotAccelerateX - 3;
						doNotAccelerateX = 1;
					}
					if (sminReachedX == sx2 && Math.abs(sx1-sx2) == 2)
					{
						sminReachedX = sx1;
						doNotAccelerateX = 0;
						additionalPointsSkippedX = doNotAccelerateX + additionalPointsSkippedX; 
					}
					int dmaxY;
					int deltaDY;
					int layersSkippedY;
					int additionalPointsSkippedY;
					int doNotAccelerateY;
					int smaxReachedY;
					int smaxY;
					if (zyIsEven)
					{
						dmaxY = Ay + zy * syForZyCalculation + (int)Math.signum(sy2) * (zy/2-1)*(zy/2) + (int)Math.signum(sy2) * zy/2;
						deltaDY = Math.abs(Dy - dmaxY);
						layersSkippedY = (int)Math.floor(Math.sqrt(Math.abs(deltaDY)));
						additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
						doNotAccelerateY= 2*(layersSkippedY) - additionalPointsSkippedY;
						smaxReachedY = syForZyCalculation + (int)Math.signum(sy2) * zy/2 - (int)Math.signum(sy2) * layersSkippedY;
						smaxY = syForZyCalculation + (int)Math.signum(sy2) * zy/2;
					}
					else
					{
						dmaxY = Ay + zy * syForZyCalculation + (int)Math.signum(sy2) * ((zy-1)/2+1)*((zy-1)/2);
						deltaDY = Math.abs(Dy - dmaxY);
						layersSkippedY = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDY)));
						additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY - layersSkippedY;
						doNotAccelerateY= 2*(layersSkippedY+1) - 1 - additionalPointsSkippedY;
						smaxReachedY = syForZyCalculation + (int)Math.signum(sy2) * (zy-1)/2 - (int)Math.signum(sy2) * layersSkippedY;
						smaxY = syForZyCalculation + (int)Math.signum(sy2) * (zy-1)/2;
					}
					boolean skippingX = false;
					boolean skippingY = false;
					boolean additionalSkippingX = false;
					boolean additionalSkippingY = false;
					boolean smaxYReached = false;
					boolean sminXReached = false;
					int skippedX = 0;
					int skippedY = 0;
					int additionalSkippedX = 0;
					int additionalSkippedY = 0;
					int sx = sx1;
					int sy = sy1;
					int x = x1;
					int y = y1;
					int ax = 0;
					int ay = 0;
//					for (int t = 0; t < t_new; t++)
//					{
//						if(!smaxYReached)
//						{
//							if (sy2 == smaxReachedY && (int)Math.abs(sy - sy2) == 1 && t+1+doNotAccelerateY < t_new)
//							{
//								ay = 0;
//								skippedY += 1;
//							}
//							else
//							{
//								ay = (int)Math.signum(smaxReachedY-sy);
//							}
//							sy = sy + ay;
//							if (sy == smaxReachedY)
//							{
//								smaxYReached = true;
//								skippingY = true;
//								if (t == 0)
//								{
//									if (ay == 0)
//									{
//										skippedY += 1;
//									}
//									else
//									{
//										if (Math.signum(ay) == Math.signum(sy2-sy1))
//										{
//											skippedY += 2;
//											doNotAccelerateY -=1;
//											additionalPointsSkippedY += 1;
//										}
//									}
//									if (skippedY > doNotAccelerateY)
//									{
//										sy = sy - ay;
//										ay = (int)Math.signum(sy2 - sy1);
//										sy = sy + ay;
//										additionalSkippingY = true;
//										skippingY = false;
//									}
//								}			
//							}
//						}
//						else
//						{
//							if (skippedY == doNotAccelerateY)
//							{
//								skippingY = false;
//							}
//							if(skippingY)
//							{
//								skippedY += 1;
//								ay = 0;
//							}
//							else
//							{
//								if (additionalSkippingY && (additionalSkippedY < additionalPointsSkippedY))
//								{
//									additionalSkippedY += 1;
//									ay = 0;
//								}
//								else
//								{
//									ay = (int)Math.signum(sy2-sy);
//									sy = sy + ay;
//									additionalSkippingY = true;
//								}
//							}
//						}
//						if(!sminXReached)
//						{
//							if (sx2 == sminReachedX && (int)Math.abs(sx - sx2) == 1 && t+1+doNotAccelerateX < t_new)
//							{
//								//special case: sx2 = smax and we also have to skip some turns in smax -> skip them from the left and not from the right
//								ax = 0;
//								skippedX += 1;
//							}
//							else
//							{
//								//increase speed to smax
//								ax = (int)Math.signum(sminReachedX-sx);
//							}
//							sx = sx + ax;
//							if (sx == sminReachedX)
//							{
//								sminXReached = true;
//								skippingX = true;
//								if (t == 0)
//								{
//									if (ax == 0)
//									{
//										skippedX += 1;
//									}
//									else
//									{
//										if (Math.signum(ax) == Math.signum(sx2-sx1))
//										{
//											skippedX += 2;
//											doNotAccelerateX -=1;
//											additionalPointsSkippedX += 1;
//										}
//									}
//									if (skippedX > doNotAccelerateX)
//									{
//										sx = sx - ax;
//										ax = (int)Math.signum(sx2 - sx1);
//										sx = sx + ax;
//										additionalSkippingX = true;
//										skippingX = false;
//									}
//								}
//							}
//						}
//						else
//						{
//							if (skippedX == doNotAccelerateX)
//							{
//								skippingX = false;
//							}
//							if(skippingX)
//							{
//								skippedX += 1;
//								ax = 0;
//							}
//							else
//							{
//								if (additionalSkippingX && (additionalSkippedX < additionalPointsSkippedX))
//								{
//									additionalSkippedX += 1;
//									ax = 0;
//								}
//								else
//								{
//									ax = (int)Math.signum(sx2-sx);
//									sx = sx + ax;
//									additionalSkippingX = true;
//								}
//							}
//						}
//						int newX = x + sx;
//						int newY = y + sy;
//						for (LineSegment border : borders)
//						{
//							if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
//							{
//								return null;
//							}
//						}
//						x = newX;
//						y = newY;
//						accelerations.add(new Point2D(ax,ay));
//					}
					if (sx1 == sminX)
					{
						sminXReached = true;
					}
					if (sy1 == smaxY)
					{
						smaxYReached = true;
					}
					int speedsX[] = new int[t_new];
					int speedsY[] = new int[t_new];
					boolean holdForTwoTurnsX = false;
					boolean holdForTwoTurnsY = false;
					for (int t = 0; t < t_new; t++)
					{
						if(!smaxYReached)
						{
							//increase speed to smax
							ay = (int)Math.signum(smaxY-sy);
							sy = sy + ay;
							if (sy == smaxY)
							{
								//did we reach smax?
								smaxYReached = true;			
							}
						}
						else
						{
							//we reached smax
							if (!holdForTwoTurnsY && !zyIsEven)
							{
								holdForTwoTurnsY = true;
							}
							else
							{
								//everything is already handled, now accelerate to sx2
								ay = (int)Math.signum(sy2-sy);
								sy = sy + ay;
							}
						}
						if(!sminXReached)
						{
							//increase speed to smax
							ax = (int)Math.signum(sminX-sx);
							sx = sx + ax;
							if (sx == sminX)
							{
								//did we reach smax?
								sminXReached = true;			
							}
						}
						else
						{
							if (!holdForTwoTurnsX && sminXHoldForTwoTurns)
							{
								holdForTwoTurnsX = true;
							}
							else
							{
								ax = (int)Math.signum(sx2-sx);
								sx = sx + ax;
								additionalSkippingX = true;
							}
						}
						int newX = x + sx;
						int newY = y + sy;
						/*for (LineSegment border : borders)
						{
							if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
							{
								return null;
							}
						}*/
						x = newX;
						y = newY;
						speedsX[t] = sx;
						speedsY[t] = sy;
					}
					int distanceX = Math.abs(x-x2);
					int distanceY = Math.abs(y-y2);
					int potentialSpeed = smaxY;
					int directionToSy2= (int)Math.signum(sy2 - smaxY);
					while (distanceY != 0)
					{
						int potentialTurn = -1;
						for (int t = speedsY.length - 2; t > 0; t--)
						{
							if (speedsY[t] == potentialSpeed && ((speedsY[t + 1] == potentialSpeed + directionToSy2 && speedsY[t - 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed  + directionToSy2)))
							{
								potentialTurn = t;
								break;
							}
						}
						if (potentialTurn == -1)
						{
							if (speedsY[0] == potentialSpeed && Math.abs(speedsY[0] + directionToSy2 - sy1) <= 1 && Math.abs(speedsY[0] + directionToSy2 - speedsY[1]) <= 1)
							{
								potentialTurn = 0;
							}
						}
						if (potentialTurn == -1)
						{
							if (speedsY[speedsY.length -2] == potentialSpeed && speedsY[speedsY.length -3] == potentialSpeed && speedsY[speedsY.length -1] == potentialSpeed + directionToSy2)
							{
								potentialTurn = speedsY.length -2;
							}
						}
						if (potentialTurn == -1)
						{
							boolean allSpeedsTheSame = true;
							for (int t = 0; t < speedsY.length; t++)
							{
								if (speedsY[t] != potentialSpeed)
								{
									allSpeedsTheSame = false;
									break;
								}
							}
							if (allSpeedsTheSame)
							{
								potentialTurn = speedsY.length - 2;
							}
						}
						if (potentialTurn != -1)
						{
							speedsY[potentialTurn] = speedsY[potentialTurn] + directionToSy2;
							distanceY--;
						}
						else
						{
							potentialSpeed = potentialSpeed + directionToSy2;
							if (Math.abs(potentialSpeed - sy2) > t_new)
							{
								System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
								return null;
							}
						}
					}
					potentialSpeed = sminX;
					int directionToSx2= (int)Math.signum(sx2 - sminX);
					while (distanceX != 0)
					{
						int potentialTurn = -1;
						for (int t = speedsX.length - 2; t > 0; t--)
						{
							if (speedsX[t] == potentialSpeed && ((speedsX[t + 1] == potentialSpeed + directionToSx2 && speedsX[t - 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed  + directionToSx2)))
							{
								potentialTurn = t;
								break;
							}
						}
						if (potentialTurn == -1)
						{
							if (speedsX[0] == potentialSpeed && Math.abs(speedsX[0] + directionToSx2 - sx1) <= 1)
							{
								potentialTurn = 0;
							}
						}
						if (potentialTurn == -1)
						{
							if (speedsX[speedsX.length -2] == potentialSpeed && speedsX[speedsX.length -3] == potentialSpeed && speedsX[speedsX.length -1] == potentialSpeed + directionToSx2)
							{
								potentialTurn = speedsX.length -2;
							}
						}
						if (potentialTurn == -1)
						{
							boolean allSpeedsTheSame = true;
							for (int t = 0; t < speedsX.length; t++)
							{
								if (speedsX[t] != potentialSpeed)
								{
									allSpeedsTheSame = false;
									break;
								}
							}
							if (allSpeedsTheSame)
							{
								potentialTurn = speedsX.length - 2;
							}
						}
						if (potentialTurn != -1)
						{
							speedsX[potentialTurn] = speedsX[potentialTurn] + directionToSx2;
							distanceX--;
						}
						else
						{
							potentialSpeed = potentialSpeed + directionToSx2;
							if (Math.abs(potentialSpeed - sx2) > t_new)
							{
								System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
								return null;
							}
						}
					}
					x = x1;
					y = y1;
					for (int t = 0; t < t_new; t++)
					{
						int newX = x + speedsX[t];
						int newY = y + speedsY[t];
						for (LineSegment border : borders)
						{
							if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
							{
								return null;
							}
						}
						x = newX;
						y = newY;
						if (t == 0)
						{
							accelerations.add(new Point2D(speedsX[t]-sx1,speedsY[t]-sy1));
						}
						else
						{
							accelerations.add(new Point2D(speedsX[t]-speedsX[t-1],speedsY[t]-speedsY[t-1]));
						}
					}
				}
			}
			else
			{
				//last case: we need same number of steps in x- and y-direction
				int deltaDX;
				int layersSkippedX;
				int additionalPointsSkippedX;
				int doNotAccelerateX;
				int smaxReachedX;
				int smaxX;
				if (zxIsEven)
				{
					int dmaxX = Ax + zx * sxForZxCalculation + (int)Math.signum(sx2) * (zx/2-1)*(zx/2) + (int)Math.signum(sx2) * zx/2;
					deltaDX = Math.abs(Dx - dmaxX);
					layersSkippedX = (int)Math.floor(Math.sqrt(Math.abs(deltaDX)));
					additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
					doNotAccelerateX= 2*(layersSkippedX) - additionalPointsSkippedX;
					smaxReachedX = sxForZxCalculation + (int)Math.signum(sx2) * zx/2 - (int)Math.signum(sx2) * layersSkippedX;
					smaxX = sxForZxCalculation + (int)Math.signum(sx2) * zx/2;
				}
				else
				{
					int dmaxX = Ax + zx * sxForZxCalculation + (int)Math.signum(sx2) * ((zx-1)/2+1)*((zx-1)/2);
					deltaDX = Math.abs(Dx - dmaxX);
					layersSkippedX = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDX)));
					additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX - layersSkippedX;
					doNotAccelerateX= 2*(layersSkippedX+1) - 1 - additionalPointsSkippedX;
					smaxReachedX = sxForZxCalculation + (int)Math.signum(sx2) * (zx-1)/2 - (int)Math.signum(sx2) * layersSkippedX;
					smaxX = sxForZxCalculation + (int)Math.signum(sx2) * (zx-1)/2;
				}
				int deltaDY;
				int layersSkippedY;
				int additionalPointsSkippedY;
				int doNotAccelerateY;
				int smaxReachedY;
				int smaxY;
				if (zyIsEven)
				{
					int dmaxY = Ay + zy * syForZyCalculation + (int)Math.signum(sy2) * (zy/2-1)*(zy/2) + (int)Math.signum(sy2) * zy/2;
					deltaDY = Math.abs(Dy - dmaxY);
					layersSkippedY = (int)Math.floor(Math.sqrt(Math.abs(deltaDY)));
					additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
					doNotAccelerateY= 2*(layersSkippedY) - additionalPointsSkippedY;
					smaxReachedY = syForZyCalculation + (int)Math.signum(sy2) * zy/2 - (int)Math.signum(sy2) * layersSkippedY;
					smaxY = syForZyCalculation + (int)Math.signum(sy2) * zy/2;
				}
				else
				{
					int dmaxY = Ay + zy * syForZyCalculation + (int)Math.signum(sy2) * ((zy-1)/2+1)*((zy-1)/2);
					deltaDY = Math.abs(Dy - dmaxY);
					layersSkippedY = (int)Math.floor(-0.5f + Math.sqrt(Math.abs(0.25f + deltaDY)));
					additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY - layersSkippedY;
					doNotAccelerateY= 2*(layersSkippedY+1) - 1 - additionalPointsSkippedY;
					smaxReachedY = syForZyCalculation + (int)Math.signum(sy2) * (zy-1)/2 - (int)Math.signum(sy2) * layersSkippedY;
					smaxY = syForZyCalculation + (int)Math.signum(sy2) * (zy-1)/2;
				}
				boolean skippingX = false;
				boolean skippingY = false;
				boolean additionalSkippingX = false;
				boolean additionalSkippingY = false;
				boolean smaxYReached = false;
				boolean smaxXReached = false;
				int skippedX = 0;
				int skippedY = 0;
				int additionalSkippedX = 0;
				int additionalSkippedY = 0;
				int sx = sx1;
				int sy = sy1;
				int x = x1;
				int y = y1;
				int ax = 0;
				int ay = 0;
//				for (int t = 0; t < ty; t++)
//				{
//					if(!smaxYReached)
//					{
//						if (sy2 == smaxReachedY && (int)Math.abs(sy - sy2) == 1 && t+1+doNotAccelerateY < ty)
//						{
//							ay = 0;
//							skippedY += 1;
//						}
//						else
//						{
//							ay = (int)Math.signum(smaxReachedY-sy);
//						}
//						sy = sy + ay;
//						if (sy == smaxReachedY)
//						{
//							smaxYReached = true;
//							skippingY = true;
//							if (t == 0)
//							{
//								if (ay == 0)
//								{
//									skippedY += 1;
//								}
//								else
//								{
//									if (Math.signum(ay) == Math.signum(sy2-sy1))
//									{
//										skippedY += 2;
//										doNotAccelerateY -=1;
//										additionalPointsSkippedY += 1;
//									}
//								}
//								if (skippedY > doNotAccelerateY)
//								{
//									sy = sy - ay;
//									ay = (int)Math.signum(sy2 - sy1);
//									sy = sy + ay;
//									additionalSkippingY = true;
//									skippingY = false;
//								}
//							}			
//						}
//					}
//					else
//					{
//						if (skippedY == doNotAccelerateY)
//						{
//							skippingY = false;
//						}
//						if(skippingY)
//						{
//							skippedY += 1;
//							ay = 0;
//						}
//						else
//						{
//							if (additionalSkippingY && (additionalSkippedY < additionalPointsSkippedY))
//							{
//								additionalSkippedY += 1;
//								ay = 0;
//							}
//							else
//							{
//								ay = (int)Math.signum(sy2-sy);
//								sy = sy + ay;
//								additionalSkippingY = true;
//							}
//						}
//					}
//					if(!smaxXReached)
//					{
//						if (sx2 == smaxReachedX && (int)Math.abs(sx - sx2) == 1 && t+1+doNotAccelerateX < tx)
//						{
//							ax = 0;
//							skippedX += 1;
//						}
//						else
//						{
//							ax = (int)Math.signum(smaxReachedX-sx);
//						}
//						sx = sx + ax;
//						if (sx == smaxReachedX)
//						{
//							smaxXReached = true;
//							skippingX = true;
//							if (t == 0)
//							{
//								if (ax == 0)
//								{
//									skippedX += 1;
//								}
//								else
//								{
//									if (Math.signum(ax) == Math.signum(sx2-sx1))
//									{
//										skippedX += 2;
//										doNotAccelerateX -=1;
//										additionalPointsSkippedX += 1;
//									}
//								}
//								if (skippedX > doNotAccelerateX)
//								{
//									sx = sx - ax;
//									ax = (int)Math.signum(sx2 - sx1);
//									sx = sx + ax;
//									additionalSkippingX = true;
//									skippingX = false;
//								}
//							}
//						}
//					}
//					else
//					{
//						if (skippedX == doNotAccelerateX)
//						{
//							skippingX = false;
//						}
//						if(skippingX)
//						{
//							skippedX += 1;
//							ax = 0;
//						}
//						else
//						{
//							if (additionalSkippingX && (additionalSkippedX < additionalPointsSkippedX))
//							{
//								additionalSkippedX += 1;
//								ax = 0;
//							}
//							else
//							{
//								ax = (int)Math.signum(sx2-sx);
//								sx = sx + ax;
//								additionalSkippingX = true;
//							}
//						}
//					}
//					int newX = x + sx;
//					int newY = y + sy;
//					for (LineSegment border : borders)
//					{
//						if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
//						{
//							return null;
//						}
//					}
//					x = newX;
//					y = newY;
//					accelerations.add(new Point2D(ax,ay));
//				}
				if (sy1 == smaxY)
				{
					smaxXReached = true;
				}
				if (sx1 == smaxX)
				{
					smaxXReached = true;
				}
				int speedsX[] = new int[ty];
				int speedsY[] = new int[ty];
				boolean holdForTwoTurnsX = false;
				boolean holdForTwoTurnsY = false;
				for (int t = 0; t < ty; t++)
				{
					if(!smaxXReached)
					{
						//increase speed to smax
						ax = (int)Math.signum(smaxX-sx);
						sx = sx + ax;
						if (sx == smaxX)
						{
							//did we reach smax?
							smaxXReached = true;			
						}
					}
					else
					{
						//we reached smax
						if (!holdForTwoTurnsX && !zxIsEven)
						{
							holdForTwoTurnsX = true;
						}
						else
						{
							//everything is already handled, now accelerate to sx2
							ax = (int)Math.signum(sx2-sx);
							sx = sx + ax;
						}
					}
					if(!smaxYReached)
					{
						//increase speed to smax
						ay = (int)Math.signum(smaxY-sy);
						sy = sy + ay;
						if (sy == smaxY)
						{
							//did we reach smax?
							smaxYReached = true;			
						}
					}
					else
					{
						//we reached smax
						if (!holdForTwoTurnsY && !zyIsEven)
						{
							holdForTwoTurnsY = true;
						}
						else
						{
							//everything is already handled, now accelerate to sx2
							ay = (int)Math.signum(sy2-sy);
							sy = sy + ay;
						}
					}
					int newX = x + sx;
					int newY = y + sy;
					/*for (LineSegment border : borders)
					{
						if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
						{
							return null;
						}
					}*/
					x = newX;
					y = newY;
					speedsX[t] = sx;
					speedsY[t] = sy;
				}
				int distanceX = Math.abs(x-x2);
				int distanceY = Math.abs(y-y2);
				int potentialSpeed = smaxX;
				int directionToSx2= (int)Math.signum(sx2 - smaxX);
				while (distanceX != 0)
				{
					int potentialTurn = -1;
					for (int t = speedsX.length - 2; t > 0; t--)
					{
						if (speedsX[t] == potentialSpeed && ((speedsX[t + 1] == potentialSpeed + directionToSx2 && speedsX[t - 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed) || (speedsX[t - 1] == potentialSpeed + directionToSx2 && speedsX[t + 1] == potentialSpeed  + directionToSx2)))
						{
							potentialTurn = t;
							break;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsX[0] == potentialSpeed && Math.abs(speedsX[0] + directionToSx2 - sx1) <= 1 && Math.abs(speedsX[0] + directionToSx2 - speedsX[1]) <= 1)
						{
							potentialTurn = 0;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsX[speedsX.length -2] == potentialSpeed && speedsX[speedsX.length -3] == potentialSpeed && speedsX[speedsX.length -1] == potentialSpeed + directionToSx2)
						{
							potentialTurn = speedsX.length -2;
						}
					}
					if (potentialTurn == -1)
					{
						boolean allSpeedsTheSame = true;
						for (int t = 0; t < speedsX.length; t++)
						{
							if (speedsX[t] != potentialSpeed)
							{
								allSpeedsTheSame = false;
								break;
							}
						}
						if (allSpeedsTheSame)
						{
							potentialTurn = speedsX.length - 2;
						}
					}
					if (potentialTurn != -1)
					{
						speedsX[potentialTurn] = speedsX[potentialTurn] + directionToSx2;
						distanceX--;
					}
					else
					{
						potentialSpeed = potentialSpeed + directionToSx2;
						if (Math.abs(potentialSpeed - sx2) > tx)
						{
							System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
							return null;
						}
					}
				}
				potentialSpeed = smaxY;
				int directionToSy2= (int)Math.signum(sy2 - smaxY);
				while (distanceY != 0)
				{
					int potentialTurn = -1;
					
					for (int t = speedsY.length - 2; t > 0; t--)
					{
						if (speedsY[t] == potentialSpeed && ((speedsY[t + 1] == potentialSpeed + directionToSy2 && speedsY[t - 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed) || (speedsY[t - 1] == potentialSpeed + directionToSy2 && speedsY[t + 1] == potentialSpeed  + directionToSy2)))
						{
							potentialTurn = t;
							break;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsY[0] == potentialSpeed && Math.abs(speedsY[0] + directionToSy2 - sy1) <= 1)
						{
							potentialTurn = 0;
						}
					}
					if (potentialTurn == -1)
					{
						if (speedsY[speedsY.length -2] == potentialSpeed && speedsY[speedsY.length -3] == potentialSpeed && speedsY[speedsY.length -1] == potentialSpeed + directionToSy2)
						{
							potentialTurn = speedsY.length -2;
						}
					}
					if (potentialTurn == -1)
					{
						boolean allSpeedsTheSame = true;
						for (int t = 0; t < speedsY.length; t++)
						{
							if (speedsY[t] != potentialSpeed)
							{
								allSpeedsTheSame = false;
								break;
							}
						}
						if (allSpeedsTheSame)
						{
							potentialTurn = speedsY.length - 2;
						}
					}
					if (potentialTurn != -1)
					{
						speedsY[potentialTurn] = speedsY[potentialTurn] + directionToSy2;
						distanceY--;
					}
					else
					{
						potentialSpeed = potentialSpeed + directionToSy2;
						if (Math.abs(potentialSpeed - sy2) > tx)
						{
							System.out.println(String.format("Overflow while computing (%d,%d,%d,%d) -> (%d,%d,%d,%d)", (int)startPosition.getX(),(int)startPosition.getY(),(int)startSpeed.getX(),(int)startSpeed.getY(),(int)endPosition.getX(),(int)endPosition.getY(),(int)endSpeed.getX(),(int)endSpeed.getY()));
							return null;
						}
					}
				}
				x = x1;
				y = y1;
				for (int t = 0; t < ty; t++)
				{
					int newX = x + speedsX[t];
					int newY = y + speedsY[t];
					for (LineSegment border : borders)
					{
						if (border.IntersectWith(new LineSegment(new Point(x,y), new Point(newX,newY))))
						{
							return null;
						}
					}
					x = newX;
					y = newY;
					if (t == 0)
					{
						accelerations.add(new Point2D(speedsX[t]-sx1,speedsY[t]-sy1));
					}
					else
					{
						accelerations.add(new Point2D(speedsX[t]-speedsX[t-1],speedsY[t]-speedsY[t-1]));
					}
				}
			}
		}
		return accelerations;
	}
	
	/** Calculates the minimum number of turns required to move from a starting position to an end position with given starting and end speed. 
	 * 
	 * @param startPosition The starting position.
	 * @param endPosition The end position.
	 * @param startSpeed The starting speed. 
	 * @param endSpeed The end speed.
	 * @return the minimum number of turns required to move between both positions with the given velocities
	 */
	public static int CalculateMinimumNumberOfTurns(Point2D startPosition, Point2D endPosition, Point2D startSpeed, Point2D endSpeed)
	{
		//initialize some variables
		int x1 = (int)startPosition.getX();
		int y1 = (int)startPosition.getY();
		int sx1 = (int)startSpeed.getX();
		int sy1 = (int)startSpeed.getY();
		int x2 = (int)endPosition.getX();
		int y2 = (int)endPosition.getY();
		int sx2 = (int)endSpeed.getX();
		int sy2 = (int)endSpeed.getY();
		//calculate total distance and distance passed by accelerating
		int Dx = x2 - x1;
		//contains one even number in multiplication -> result is integer
		int Ax = (int)Math.abs(sx2-sx1) * sx1 + (int)Math.signum(sx2 - sx1)*(int)Math.abs(sx2 - sx1)*((int)Math.abs(sx2 - sx1) + 1)/2;				
		//check, if we overshoot the goal
		boolean overshootX = false;
		if (Math.signum(sx2)*(Dx-Ax) <= 0)
		{
			overshootX = true;
		}
		//now do the same in y-direction
		int Dy = y2 - y1;
		int Ay = (int)Math.abs(sy2-sy1) * sy1 + (int)Math.signum(sy2 - sy1)*(int)Math.abs(sy2 - sy1)*((int)Math.abs(sy2 - sy1) + 1)/2;	
		boolean overshootY = false;
		if (Math.signum(sy2)*(Dy-Ay) <= 0)
		{
			overshootY = true;
		}
		//check if starting or end speed are closer to the maximum speed
		int sxForZxCalculation;
		int syForZyCalculation;
		if (Math.abs(Dx-sx2)<=Math.abs(Dx-sx1))
		{
			sxForZxCalculation = sx2;
		}
		else
		{
			sxForZxCalculation = sx1;
		}
		if (Math.abs(Dy-sy2)<=Math.abs(Dy-sy1))
		{
			syForZyCalculation = sy2;
		}
		else
		{
			syForZyCalculation = sy1;
		}
		//calculate additional turns needed depending on the two cases: max speed is only hold one turn or max speed is hold two turns
		int zx_even = 2*(int)Math.ceil(- sxForZxCalculation * (int)Math.signum(Dx-Ax) + Math.sqrt(sxForZxCalculation * sxForZxCalculation + Math.abs(Dx-Ax)));
		int zx_odd = 2*(int)Math.ceil(- sxForZxCalculation * (int)Math.signum(Dx-Ax) - 0.5f + Math.sqrt(sxForZxCalculation * sxForZxCalculation + 0.25f + Math.abs(Dx-Ax))) + 1;
		int zy_even = 2*(int)Math.ceil(- syForZyCalculation * (int)Math.signum(Dy-Ay) + Math.sqrt(syForZyCalculation * syForZyCalculation + Math.abs(Dy-Ay)));
		int zy_odd = 2*(int)Math.ceil(- syForZyCalculation * (int)Math.signum(Dy-Ay) - 0.5f + Math.sqrt(syForZyCalculation * syForZyCalculation + 0.25f + Math.abs(Dy-Ay))) + 1;
		int zx;
		boolean zxIsEven;
		int zy;
		boolean zyIsEven;
		if (zx_even < zx_odd)
		{
			zx = zx_even;
			zxIsEven = true;
		}
		else
		{
			zx = zx_odd;
			zxIsEven = false;
		}
		if (zy_even < zy_odd)
		{
			zy = zy_even;
			zyIsEven = true;
		}
		else
		{
			zy = zy_odd;
			zyIsEven = false;
		}
		//we need at least z additional steps
		int tx = Math.abs(sx2 - sx1) + zx; 	
		int ty = Math.abs(sy2 - sy1) + zy;
		//if we overshoot in both directions we can assume that this edge is not part of the best path
		if (overshootX && overshootY)
		{
			return -1;
		}
		if (((tx > ty) || overshootY) && !overshootX)
		{
			//x is the dominant direction
			//calculate minimal covered distance
			double sminY_double = (sy1  + sy2 - Math.signum(sy2)*tx)/2.0f;
			int sminY;
			//check if in this case we hold min speed for 2 turns
			boolean sminYHoldForTwoTurns = ((int) Math.floor(sminY_double) != (int) Math.ceil(sminY_double));
			if (Math.signum(sminY_double)>0)
			{
				sminY = (int) Math.floor(sminY_double);
			}
			else
			{
				sminY = (int) Math.ceil(sminY_double);
			}
			int dminY;
			//calculate the minimal distance covered depending on the case
			if (!sminYHoldForTwoTurns)
			{
				dminY = (int) (Math.abs(sminY - sy1) * sy1 - (int)Math.signum(sy1-sminY)*(Math.abs(sminY - sy1) * (Math.abs(sminY  - sy1) + 1))/2 + Math.abs(sy2 - sminY) * sminY + (int)Math.signum(sy2-sminY)*(Math.abs(sy2 - sminY) * (Math.abs(sy2  - sminY) + 1))/2);
			}
			else
			{
				dminY = (int) (Math.abs(sminY - sy1) * sy1 - (int)Math.signum(sy1-sminY)*(Math.abs(sminY - sy1) * (Math.abs(sminY  - sy1) + 1))/2 + (Math.abs(sy2 - sminY) + 1) * sminY + (int)Math.signum(sy2-sminY)*(Math.abs(sy2 - sminY) * (Math.abs(sy2  - sminY) + 1))/2);
			}
			//the following condition checks, if dmin < D -> if sy2 is in the same direction as the difference of distances, we are able to decrease the distance
			if (Math.signum(sy2) * (Dy-dminY) >= 0 && tx >= Math.abs(sy2-sy1))
			{
				return tx;
			}
			else
			{
				//we have to compute a new smin -> two cases again
				int sminY_even;
				int sminY_odd; 
				double sminY_double_even = Math.sqrt((sy1*sy1 + sy2*sy2 + (int)Math.abs(sy2) - (int)Math.signum(sy2)*sy1 - 2*Math.signum(sy2)*Dy)/2.0f)*(-Math.signum(sy2));
				double sminY_double_odd = Math.signum(sy2) - Math.sqrt(0.25+(sy1*sy1 + sy2*sy2 + (int)Math.abs(sy2) - (int)Math.signum(sy2)*sy1 - 2*Math.signum(sy2)*Dy)/2.0f)*(Math.signum(sy2));
				if (Math.signum(sy2)>0)
				{
					sminY_even = (int) Math.floor(sminY_double_even);
					sminY_odd = (int) Math.floor(sminY_double_odd);
				}
				else
				{
					sminY_even = (int) Math.ceil(sminY_double_even);
					sminY_odd = (int) Math.ceil(sminY_double_odd);
				}
				//the new smin yields us a new t
				int t_new;
				int t_new_even = (int)Math.signum(sy2)*(sy1-sminY_even) + (int)Math.signum(sy2) *(sy2 - sminY_even);
				int t_new_odd = (int)Math.signum(sy2)*(sy1-sminY_odd) + (int)Math.signum(sy2) *(sy2 - sminY_odd) + 1;
				if (t_new_even < t_new_odd)
				{
					t_new = t_new_even;
					sminY = sminY_even;
					sminYHoldForTwoTurns = false;
				}
				else
				{
					t_new = t_new_odd;
					sminY = sminY_odd;
					sminYHoldForTwoTurns = true;
				}
				return t_new;
			}
		}
		else
		{
			if (((ty > tx) || overshootX) && !overshootY)
			{
				//y is the dominant direction -> do the same as above with y and x swapped
				double sminX_double = (sx1  + sx2 - Math.signum(sx2)*ty)/2.0f;
				int sminX;
				boolean sminXHoldForTwoTurns = ((int) Math.floor(sminX_double) != (int) Math.ceil(sminX_double));
				if (Math.signum(sminX_double)>0)
				{
					sminX = (int) Math.floor(sminX_double);
				}
				else
				{
					sminX = (int) Math.ceil(sminX_double);
				}
				int dminX;
				if (!sminXHoldForTwoTurns)
				{
					dminX = (int) (Math.abs(sminX - sx1) * sx1 - (int)Math.signum(sx1-sminX)*(Math.abs(sminX - sx1) * (Math.abs(sminX  - sx1) + 1))/2 + Math.abs(sx2 - sminX) * sminX + (int)Math.signum(sx2-sminX)*(Math.abs(sx2 - sminX) * (Math.abs(sx2  - sminX) + 1))/2);
				}
				else
				{
					dminX = (int) (Math.abs(sminX - sx1) * sx1 - (int)Math.signum(sx1-sminX)*(Math.abs(sminX - sx1) * (Math.abs(sminX  - sx1) + 1))/2 + (Math.abs(sx2 - sminX) + 1) * sminX + (int)Math.signum(sx2-sminX)*(Math.abs(sx2 - sminX) * (Math.abs(sx2  - sminX) + 1))/2);
				}
				if (Math.signum(sx2) * (Dx-dminX) >= 0  && ty >= Math.abs(sx2-sx1))
				{
					return ty;
				}
				else
				{
					int sminX_even;
					int sminX_odd; 
					double sminX_double_even = Math.sqrt((sx1*sx1 + sx2*sx2 + (int)Math.abs(sx2) - (int)Math.signum(sx2)*sx1 - 2*Math.signum(sx2)*Dx)/2.0f)*(-Math.signum(sx2));
					double sminX_double_odd = Math.signum(sx2) - Math.sqrt(0.25+(sx1*sx1 + sx2*sx2 + (int)Math.abs(sx2) - (int)Math.signum(sx2)*sx1 - 2*Math.signum(sx2)*Dx)/2.0f)*(Math.signum(sx2));
					if (Math.signum(sx2)>0)
					{
						sminX_even = (int) Math.floor(sminX_double_even);
						sminX_odd = (int) Math.floor(sminX_double_odd);
					}
					else
					{
						sminX_even = (int) Math.ceil(sminX_double_even);
						sminX_odd = (int) Math.ceil(sminX_double_odd);
					}
					int t_new;
					int t_new_even = (int)Math.signum(sx2)*(sx1-sminX_even) + (int)Math.signum(sx2) *(sx2 - sminX_even);
					int t_new_odd = (int)Math.signum(sx2)*(sx1-sminX_odd) + (int)Math.signum(sx2) *(sx2 - sminX_odd) + 1;
					if (t_new_even < t_new_odd)
					{
						t_new = t_new_even;
						sminX = sminX_even;
						sminXHoldForTwoTurns = false;
					}
					else
					{
						t_new = t_new_odd;
						sminX = sminX_odd;
						sminXHoldForTwoTurns = true;
					}
					return t_new;
				}
			}
			else
			{
				return tx;
			}
		}
	}
	public enum Direction 
	{
		LEFT, RIGHT, UP, DOWN 
	}
	
	public static List<Point2D> CalculateFinalAccelerations(Point2D lastLandingRegionPosition, Point2D lastLandingRegionSpeed, LineSegment finishLine, Direction direction, LinkedList<LineSegment> borders)
	{
		LinkedList<Point2D> accelerations = new LinkedList<Point2D>();
		int x = (int) lastLandingRegionPosition.getX();
		int y = (int) lastLandingRegionPosition.getY();
		int sx = (int) lastLandingRegionSpeed.getX();
		int sy = (int) lastLandingRegionSpeed.getY();
		int ax = 0;
		int ay = 0;
		boolean xIsDominant = false;
		switch (direction)
		{
			case UP:
			{
				ay = 1;
				ax = - (int)Math.signum(sx);
				xIsDominant = false;
				break;
			}
			case DOWN:
			{
				ay = -1;
				ax = - (int)Math.signum(sx);
				xIsDominant = false;
				break;
			}
			case LEFT:
			{
				ax = -1;
				ay = - (int)Math.signum(sy);
				xIsDominant = true;
				break;
			}
			case RIGHT:
			{
				ax = 1;
				ay = - (int)Math.signum(sy);
				xIsDominant = true;
				break;
			}
		}
		sx = sx + ax;
		sy = sy + ay;
		int newX = x + sx;
		int newY = y + sy;
		accelerations.add(new Point2D(ax,ay));
		LineSegment nextMove = new LineSegment(new Point(x,y),new Point(newX,newY));
		while (!nextMove.IntersectWith(finishLine))
		{
			x = newX;
			y = newY;
			if (xIsDominant)
			{
				ay = - (int)Math.signum(sy);
			}
			else
			{
				ax = - (int)Math.signum(sx);
			}
			sx = sx + ax;
			sy = sy + ay;
			newX = x + sx;
			newY = y + sy;
			accelerations.add(new Point2D(ax,ay));
			nextMove = new LineSegment(new Point(x,y),new Point(newX,newY));
			for (LineSegment border : borders)
			{
				if (nextMove.IntersectWith(border))
				{
					if (nextMove.IntersectWith(finishLine))
					{
						if (nextMove.GetIntersectionDistance(finishLine, new Point(x,y)) < nextMove.GetIntersectionDistance(border, new Point(x,y)))
						{
							continue;
						}
					}
					return null;
				}
			}
		}
		return accelerations;
	}
	
	public static int CalculateFinalNumberOfTurns(Point2D lastLandingRegionPosition, Point2D lastLandingRegionSpeed, LineSegment finishLine, Direction direction, LinkedList<LineSegment> borders)
	{
		int turns = 1;
		int x = (int) lastLandingRegionPosition.getX();
		int y = (int) lastLandingRegionPosition.getY();
		int sx = (int) lastLandingRegionSpeed.getX();
		int sy = (int) lastLandingRegionSpeed.getY();
		int ax = 0;
		int ay = 0;
		boolean xIsDominant = false;
		switch (direction)
		{
			case UP:
			{
				ay = 1;
				ax = - (int)Math.signum(sx);
				xIsDominant = false;
				break;
			}
			case DOWN:
			{
				ay = -1;
				ax = - (int)Math.signum(sx);
				xIsDominant = false;
				break;
			}
			case LEFT:
			{
				ax = -1;
				ay = - (int)Math.signum(sy);
				xIsDominant = true;
				break;
			}
			case RIGHT:
			{
				ax = 1;
				ay = - (int)Math.signum(sy);
				xIsDominant = true;
				break;
			}
		}
		sx = sx + ax;
		sy = sy + ay;
		int newX = x + sx;
		int newY = y + sy;
		LineSegment nextMove = new LineSegment(new Point(x,y),new Point(newX,newY));
		while (!nextMove.IntersectWith(finishLine))
		{
			x = newX;
			y = newY;
			if (xIsDominant)
			{
				ay = - (int)Math.signum(sy);
			}
			else
			{
				ax = - (int)Math.signum(sx);
			}
			sx = sx + ax;
			sy = sy + ay;
			newX = x + sx;
			newY = y + sy;
			turns++;
			nextMove = new LineSegment(new Point(x,y),new Point(newX,newY));
			for (LineSegment border : borders)
			{
				if (nextMove.IntersectWith(border))
				{
					if (nextMove.IntersectWith(finishLine))
					{
						if (nextMove.GetIntersectionDistance(finishLine, new Point(x,y)) < nextMove.GetIntersectionDistance(border, new Point(x,y)))
						{
							continue;
						}
					}
					return -1;
				}
			}
		}
		return turns;
	}
}
