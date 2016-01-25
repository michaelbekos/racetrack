package src.main.java.logic.utils;

import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;

public class AIUtils
{
	public static List<Point2D> CalculateAccelerations(Point2D startPosition, Point2D endPosition, Point2D startSpeed, Point2D endSpeed, int turns)
	{
		LinkedList<Point2D> accelerations = new LinkedList<Point2D>();
		int x1 = (int)startPosition.getX();
		int y1 = (int)startPosition.getY();
		int sx1 = (int)startSpeed.getX();
		int sy1 = (int)startSpeed.getY();
		int x2 = (int)endPosition.getX();
		int y2 = (int)endPosition.getY();
		int sx2 = (int)endSpeed.getX();
		int sy2 = (int)endSpeed.getY();
		int Dx = x1 - x2;
		//contains one even number in multiplication -> result is integer
		int Ax = (sx2 - sx1)*(sx2 + sx1 + 1)/2;				
		int Dy = y1 - y2;
		int Ay = (sy2 - sy1)*(sy2 + sy1 + 1)/2;
		double zx = -2.0f * Math.abs(sx2) -1.0f + Math.sqrt(4.0f * sx2 * sx2 + 4.0f * Math.abs(sx2) + 1.0f + Math.abs(Dx-Ax));
		double zy = -2.0f * Math.abs(sy2) -1.0f + Math.sqrt(4.0f * sy2 * sy2 + 4.0f * Math.abs(sy2) + 1.0f + Math.abs(Dy-Ay));
		//we need at least z additional steps -> round up, also even number of moves required
		int tx = Math.abs(sx2 - sx1) + (int)Math.ceil(zx) + (int)Math.ceil(zx)%2; 	
		int ty = Math.abs(sy2 - sy1) + (int)Math.ceil(zy) + (int)Math.ceil(zx)%2;
		if (tx > ty)
		{
			double sminY_double = (sy1  + sy2 - Math.signum(sy2)*tx)/2.0f;
			int sminY;
			if (Math.signum(sy2)>0)
			{
				sminY = (int) Math.floor(sminY_double);
			}
			else
			{
				sminY = (int) Math.ceil(sminY_double);
			}
			int dminY = (int) (Math.abs(sminY - sy1) * sy1 - (int)Math.signum(sy2)*(Math.abs(sminY - sy1) * (Math.abs(sminY  - sy1) + 1))/2 + Math.abs(sy2 - sminY) * sminY + (int)Math.signum(sy2)*(Math.abs(sy2 - sminY) * (Math.abs(sy2  - sminY) + 1))/2);
			if (Math.abs(dminY) <= Math.abs(Dy))
			{
				int deltaDY = Math.abs(Dy) - Math.abs(dminY);
				int layersSkippedY = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDY)));
				int additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
				int doNotAccelerateY = 2*(layersSkippedY+1) - additionalPointsSkippedY;
				int sminReachedY = sminY + (int)Math.signum(sy2) * layersSkippedY;
				int zx_int = (int)Math.ceil(zx) + (int)Math.ceil(zx)%2;
				int dmaxX = Ax + sx2*zx_int + zx_int*zx_int/4+zx_int/2;
				int deltaDX = Math.abs(Dx) - Math.abs(dmaxX);
				int layersSkippedX = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDX)));
				int additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
				int doNotAccelerateX = 2*(layersSkippedX+1) - additionalPointsSkippedX;
				int smaxReachedX = sx2 + zx_int/2 - (int)Math.signum(sx2) * layersSkippedX;
				boolean skippingX = false;
				boolean skippingY = false;
				boolean smaxXReached = false;
				boolean sminYReached = false;
				int skippedX = 0;
				int skippedY = 0;
				int sx = sx1;
				int sy = sy1;
				int ax = 0;
				int ay = 0;
				for (int t = 0; t < tx; t++)
				{
					if(!smaxXReached)
					{
						ax = (int)Math.signum(smaxReachedX-sx);
						sx = sx + ax;
						if (sx == smaxReachedX)
						{
							smaxXReached = true;
							skippingX = true;
						}
					}
					else
					{
						if (skippedX == doNotAccelerateX)
						{
							skippingX = false;
						}
						if(skippingX)
						{
							skippedX += 1;
							ax = 0;
						}
						else
						{
							ax = (int)Math.signum(sx2-sx);
							sx = sx + ax;
						}
					}
					if(!sminYReached)
					{
						ay = (int)Math.signum(sminReachedY-sy);
						sy = sy + ay;
						if (sy == sminReachedY)
						{
							sminYReached = true;
							skippingY = true;
						}
					}
					else
					{
						if (skippedY == doNotAccelerateY)
						{
							skippingY = false;
						}
						if(skippingY)
						{
							skippedY += 1;
							ay = 0;
						}
						else
						{
							ay = (int)Math.signum(sy2-sy);
							sy = sy + ay;
						}
					}
					accelerations.add(new Point2D(ax,ay));
				}
			}
			else
			{
				sminY_double = Math.sqrt((2*Math.signum(sy2)*Dy - 3 * sy1 *sy1 + Math.signum(sy2) * sy1 - sy2 *sy2 - Math.signum(sy2)*sy2)/-2.0f) * (-Math.signum(sy2));
				if (Math.signum(sy2)>0)
				{
					sminY = (int) Math.floor(sminY_double);
				}
				else
				{
					sminY = (int) Math.ceil(sminY_double);
				}
				int t_new = (int)Math.signum(sy2)*(sy1-sminY) + (int)Math.signum(sy2) *(sy2 - sminY);
				if ((int) Math.floor(sminY_double) != (int) Math.ceil(sminY_double))
				{
					t_new += 1;
				}
				int zx_new = t_new - ((int)Math.abs(sx2 - sx1));
				if (zx_new%2 != 0)
				{
					t_new += 1;
					zx_new +=1;
					sminY_double = (sy1  + sy2 - Math.signum(sy2)*t_new)/2.0f;
					if (Math.signum(sy2)>0)
					{
						sminY = (int) Math.floor(sminY_double);
					}
					else
					{
						sminY = (int) Math.ceil(sminY_double);
					}
				}
				dminY = (int) (Math.abs(sminY - sy1) * sy1 - (int)Math.signum(sy2)*(Math.abs(sminY - sy1) * (Math.abs(sminY  - sy1) + 1))/2 + Math.abs(sy2 - sminY) * sminY + (int)Math.signum(sy2)*(Math.abs(sy2 - sminY) * (Math.abs(sy2  - sminY) + 1))/2);
				int deltaDY = Math.abs(Dy) - Math.abs(dminY);
				int layersSkippedY = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDY)));
				int additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
				int doNotAccelerateY = 2*(layersSkippedY+1) - additionalPointsSkippedY;
				int sminReachedY = sminY + (int)Math.signum(sy2) * layersSkippedY;
				int dmaxX = Ax + sx2*zx_new + zx_new*zx_new/4+zx_new/2;
				int deltaDX = Math.abs(Dx) - Math.abs(dmaxX);
				int layersSkippedX = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDX)));
				int additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
				int doNotAccelerateX = 2*(layersSkippedX+1) - additionalPointsSkippedX;
				int smaxReachedX = sx2 + zx_new/2 - (int)Math.signum(sx2) * layersSkippedX;
				boolean skippingX = false;
				boolean skippingY = false;
				boolean smaxXReached = false;
				boolean sminYReached = false;
				int skippedX = 0;
				int skippedY = 0;
				int sx = sx1;
				int sy = sy1;
				int ax = 0;
				int ay = 0;
				for (int t = 0; t < t_new; t++)
				{
					if(!smaxXReached)
					{
						ax = (int)Math.signum(smaxReachedX-sx);
						sx = sx + ax;
						if (sx == smaxReachedX)
						{
							smaxXReached = true;
							skippingX = true;
						}
					}
					else
					{
						if (skippedX == doNotAccelerateX)
						{
							skippingX = false;
						}
						if(skippingX)
						{
							skippedX += 1;
							ax = 0;
						}
						else
						{
							ax = (int)Math.signum(sx2-sx);
							sx = sx + ax;
						}
					}
					if(!sminYReached)
					{
						ay = (int)Math.signum(sminReachedY-sy);
						sy = sy + ay;
						if (sy == sminReachedY)
						{
							sminYReached = true;
							skippingY = true;
						}
					}
					else
					{
						if (skippedY == doNotAccelerateY)
						{
							skippingY = false;
						}
						if(skippingY)
						{
							skippedY += 1;
							ay = 0;
						}
						else
						{
							ay = (int)Math.signum(sy2-sy);
							sy = sy + ay;
						}
					}
					accelerations.add(new Point2D(ax,ay));
				}
			}
		}
		else
		{
			if (tx < ty)
			{
				double sminX_double = (sx1  + sx2 - Math.signum(sx2)*tx)/2.0f;
				int sminX;
				if (Math.signum(sx2)>0)
				{
					sminX = (int) Math.floor(sminX_double);
				}
				else
				{
					sminX = (int) Math.ceil(sminX_double);
				}
				int dminX = (int) (Math.abs(sminX - sx1) * sx1 - (int)Math.signum(sx2)*(Math.abs(sminX - sx1) * (Math.abs(sminX  - sx1) + 1))/2 + Math.abs(sx2 - sminX) * sminX + (int)Math.signum(sx2)*(Math.abs(sx2 - sminX) * (Math.abs(sx2  - sminX) + 1))/2);
				if (Math.abs(dminX) <= Math.abs(Dx))
				{
					int deltaDX = Math.abs(Dx) - Math.abs(dminX);
					int layersSkippedX = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDX)));
					int additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
					int doNotAccelerateX = 2*(layersSkippedX+1) - additionalPointsSkippedX;
					int sminReachedX = sminX + (int)Math.signum(sx2) * layersSkippedX;
					int zy_int = (int)Math.ceil(zy) + (int)Math.ceil(zy)%2;
					int dmaxY = Ay + sy2*zy_int + zy_int*zy_int/4+zy_int/2;
					int deltaDY = Math.abs(Dy) - Math.abs(dmaxY);
					int layersSkippedY = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDY)));
					int additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
					int doNotAccelerateY= 2*(layersSkippedY+1) - additionalPointsSkippedY;
					int smaxReachedY = sy2 + zy_int/2 - (int)Math.signum(sy2) * layersSkippedY;
					boolean skippingX = false;
					boolean skippingY = false;
					boolean smaxYReached = false;
					boolean sminXReached = false;
					int skippedX = 0;
					int skippedY = 0;
					int sx = sx1;
					int sy = sy1;
					int ax = 0;
					int ay = 0;
					for (int t = 0; t < ty; t++)
					{
						if(!smaxYReached)
						{
							ay = (int)Math.signum(smaxReachedY-sy);
							sy = sy + ay;
							if (sy == smaxReachedY)
							{
								smaxYReached = true;
								skippingY = true;
							}
						}
						else
						{
							if (skippedY == doNotAccelerateY)
							{
								skippingY = false;
							}
							if(skippingY)
							{
								skippedY += 1;
								ay = 0;
							}
							else
							{
								ay = (int)Math.signum(sy2-sy);
								sy = sy + ay;
							}
						}
						if(!sminXReached)
						{
							ax = (int)Math.signum(sminReachedX-sx);
							sx = sx + ax;
							if (sx == sminReachedX)
							{
								sminXReached = true;
								skippingX = true;
							}
						}
						else
						{
							if (skippedX == doNotAccelerateX)
							{
								skippingX = false;
							}
							if(skippingX)
							{
								skippedX += 1;
								ax = 0;
							}
							else
							{
								ax = (int)Math.signum(sx2-sx);
								sx = sx + ax;
							}
						}
						accelerations.add(new Point2D(ax,ay));
					}
				}
				else
				{
					sminX_double = Math.sqrt((2*Math.signum(sx2)*Dx - 3 * sx1 *sx1 + Math.signum(sx2) * sx1 - sx2 *sx2 - Math.signum(sx2)*sx2)/-2.0f) * (-Math.signum(sx2));
					if (Math.signum(sx2)>0)
					{
						sminX = (int) Math.floor(sminX_double);
					}
					else
					{
						sminX = (int) Math.ceil(sminX_double);
					}
					int t_new = (int)Math.signum(sx2)*(sx1-sminX) + (int)Math.signum(sx2) *(sx2 - sminX);
					if ((int) Math.floor(sminX_double) != (int) Math.ceil(sminX_double))
					{
						t_new += 1;
					}
					int zy_new = t_new - ((int)Math.abs(sy2 - sy1));
					if (zy_new%2 != 0)
					{
						t_new += 1;
						zy_new +=1;
						sminX_double = (sx1  + sx2 - Math.signum(sx2)*t_new)/2.0f;
						if (Math.signum(sx2)>0)
						{
							sminX = (int) Math.floor(sminX_double);
						}
						else
						{
							sminX = (int) Math.ceil(sminX_double);
						}
					}
					dminX = (int) (Math.abs(sminX - sx1) * sx1 - (int)Math.signum(sx2)*(Math.abs(sminX - sx1) * (Math.abs(sminX  - sx1) + 1))/2 + Math.abs(sx2 - sminX) * sminX + (int)Math.signum(sx2)*(Math.abs(sx2 - sminX) * (Math.abs(sx2  - sminX) + 1))/2);
					int deltaDX = Math.abs(Dx) - Math.abs(dminX);
					int layersSkippedX = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDX)));
					int additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
					int doNotAccelerateX = 2*(layersSkippedX+1) - additionalPointsSkippedX;
					int sminReachedX = sminX + (int)Math.signum(sx2) * layersSkippedX;
					int dmaxY = Ay + sy2*zy_new + zy_new*zy_new/4+zy_new/2;
					int deltaDY = Math.abs(Dy) - Math.abs(dmaxY);
					int layersSkippedY = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDY)));
					int additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
					int doNotAccelerateY = 2*(layersSkippedY+1) - additionalPointsSkippedY;
					int smaxReachedY = sy2 + zy_new/2 - (int)Math.signum(sy2) * layersSkippedY;
					boolean skippingX = false;
					boolean skippingY = false;
					boolean smaxYReached = false;
					boolean sminXReached = false;
					int skippedX = 0;
					int skippedY = 0;
					int sx = sx1;
					int sy = sy1;
					int ax = 0;
					int ay = 0;
					for (int t = 0; t < t_new; t++)
					{
						if(!smaxYReached)
						{
							ay = (int)Math.signum(smaxReachedY-sy);
							sy = sy + ay;
							if (sy == smaxReachedY)
							{
								smaxYReached = true;
								skippingY = true;
							}
						}
						else
						{
							if (skippedY == doNotAccelerateY)
							{
								skippingY = false;
							}
							if(skippingY)
							{
								skippedY += 1;
								ay = 0;
							}
							else
							{
								ay = (int)Math.signum(sy2-sy);
								sy = sy + ay;
							}
						}
						if(!sminXReached)
						{
							ax = (int)Math.signum(sminReachedX-sx);
							sx = sx + ax;
							if (sx == sminReachedX)
							{
								sminXReached = true;
								skippingX = true;
							}
						}
						else
						{
							if (skippedX == doNotAccelerateX)
							{
								skippingX = false;
							}
							if(skippingX)
							{
								skippedX += 1;
								ax = 0;
							}
							else
							{
								ax = (int)Math.signum(sx2-sx);
								sx = sx + ax;
							}
						}
						accelerations.add(new Point2D(ax,ay));
					}
				}
			}
			else
			{
				int zx_int = (int)Math.ceil(zx) + (int)Math.ceil(zx)%2;
				int dmaxX = Ax + sx2*zx_int + zx_int*zx_int/4+zx_int/2;
				int deltaDX = Math.abs(Dx) - Math.abs(dmaxX);
				int layersSkippedX = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDX)));
				int additionalPointsSkippedX = Math.abs(deltaDX) - layersSkippedX * layersSkippedX;
				int doNotAccelerateX = 2*(layersSkippedX+1) - additionalPointsSkippedX;
				int smaxReachedX = sx2 + zx_int/2 - (int)Math.signum(sx2) * layersSkippedX;
				int zy_int = (int)Math.ceil(zy) + (int)Math.ceil(zy)%2;
				int dmaxY = Ay + sy2*zy_int + zy_int*zy_int/4+zy_int/2;
				int deltaDY = Math.abs(Dy) - Math.abs(dmaxY);
				int layersSkippedY = (int)Math.floor((int)Math.sqrt(Math.abs(deltaDY)));
				int additionalPointsSkippedY = Math.abs(deltaDY) - layersSkippedY * layersSkippedY;
				int doNotAccelerateY= 2*(layersSkippedY+1) - additionalPointsSkippedY;
				int smaxReachedY = sy2 + zy_int/2 - (int)Math.signum(sy2) * layersSkippedY;
				boolean skippingX = false;
				boolean skippingY = false;
				boolean smaxYReached = false;
				boolean smaxXReached = false;
				int skippedX = 0;
				int skippedY = 0;
				int sx = sx1;
				int sy = sy1;
				int ax = 0;
				int ay = 0;
				for (int t = 0; t < tx; t++)
				{
					if(!smaxYReached)
					{
						ay = (int)Math.signum(smaxReachedY-sy);
						sy = sy + ay;
						if (sy == smaxReachedY)
						{
							smaxYReached = true;
							skippingY = true;
						}
					}
					else
					{
						if (skippedY == doNotAccelerateY)
						{
							skippingY = false;
						}
						if(skippingY)
						{
							skippedY += 1;
							ay = 0;
						}
						else
						{
							ay = (int)Math.signum(sy2-sy);
							sy = sy + ay;
						}
					}
					if(!smaxXReached)
					{
						ax = (int)Math.signum(smaxReachedX-sx);
						sx = sx + ax;
						if (sx == smaxReachedX)
						{
							smaxXReached = true;
							skippingX = true;
						}
					}
					else
					{
						if (skippedX == doNotAccelerateX)
						{
							skippingX = false;
						}
						if(skippingX)
						{
							skippedX += 1;
							ax = 0;
						}
						else
						{
							ax = (int)Math.signum(sx2-sx);
							sx = sx + ax;
						}
					}
					accelerations.add(new Point2D(ax,ay));
				}
			}
		}
		return accelerations;
	}
}
