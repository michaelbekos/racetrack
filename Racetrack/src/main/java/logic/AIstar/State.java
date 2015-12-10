package src.main.java.logic.AIstar;

import java.util.HashMap;

public class State {
	private Point position;
	private int velocityX;
	private int velocityY;
	private State predecessor;
	private int turnsToReach;
	private int approximateTurnsToFinish;
	public Point Position()
	{
		return position;
	}
	public int VelocityX()
	{
		return velocityX;
	}
	public int VelocityY()
	{
		return velocityY;
	}
	public State Predecessor()
	{
		return predecessor;
	}
	public void Predecessor(State _predecessor)
	{
		predecessor = _predecessor;
	}
	public int TurnsToReach()
	{
		return turnsToReach;
	}
	public void TurnsToReach(int _turnsToReach)
	{
		turnsToReach = _turnsToReach;
	}
	public int ApproximateTurnsToFinish()
	{
		return approximateTurnsToFinish;
	}
	public void ApproximateTurnsToFinish(int _approximateTurnsToFinish)
	{
		approximateTurnsToFinish = _approximateTurnsToFinish;
	}
	private State(Point _position, int _velocityX, int _velocityY)
	{
		position = _position;
		velocityX = _velocityX;
		velocityY = _velocityY;
		predecessor = null;
		turnsToReach = Integer.MAX_VALUE;
		approximateTurnsToFinish = Integer.MAX_VALUE;
	}
	public static State GetState(Point _position, int _velocityX, int _velocityY)
	{
		if (Data.States.containsKey(_position))
		{
			if (Data.States.get(_position).containsKey(_velocityX))
			{
				if (Data.States.get(_position).get(_velocityX).containsKey(_velocityY))
				{
					return Data.States.get(_position).get(_velocityX).get(_velocityY);
				}
				else
				{
					State s = new State(_position,_velocityX,_velocityY);
					Data.States.get(_position).get(_velocityX).put(_velocityY,s);
					return s;
				}
			}
			else
			{
				Data.States.get(_position).put(_velocityX, new HashMap<Integer,State>());
				State s = new State(_position,_velocityX,_velocityY);
				Data.States.get(_position).get(_velocityX).put(_velocityY,s);
				return s;
			}
		}
		else
		{
			Data.States.put(_position, new HashMap<Integer,HashMap<Integer,State>>());
			Data.States.get(_position).put(_velocityX, new HashMap<Integer,State>());
			State s = new State(_position,_velocityX,_velocityY);
			Data.States.get(_position).get(_velocityX).put(_velocityY,s);
			return s;
		}				
	}
	public static State GetState(Point _position)
	{
		return GetState(_position, 0,0);
	}
	public static State GetState(State _predecessor, int accelerationX, int accelerationY)
	{
		int velocityX = _predecessor.VelocityX() + accelerationX;
		int velocityY = _predecessor.VelocityY() + accelerationY;
		Point position = Point.GetPoint(_predecessor.Position().X() + velocityX, _predecessor.Position().Y() + velocityY);
		return GetState(position, velocityX,velocityY);
	}
}
