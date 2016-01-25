package src.main.java.logic.AIstar;

import java.util.Random;
import java.util.Stack;


import src.main.java.logic.AI;
import src.main.java.logic.Game;
import src.main.java.logic.IAI;
import src.main.java.logic.Line2D;
import src.main.java.logic.Player;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class AIstar extends AI {
	
	public static Random random = new Random(System.currentTimeMillis());
	public HashSet<State> closedList;
	private PriorityQueue<State> openList;
	private HashSet<LineSegment> borders;
	private LineSegment goal;
	private Point startingPosition;
	private boolean pathComputed;
	private Point lastPosition;
	private HashSet<Point> forbiddenPositions;
	private State[] path;
	private int turn;
	
	public AIstar(Integer playerID, String name) {
		super(playerID, "AIstar");
		closedList = new HashSet<State>();
		openList = new PriorityQueue<State>(11,new StateComparator());
		borders = new HashSet<LineSegment>();
		path = null;
		startingPosition = null;
		goal = null;
		pathComputed = false;
		forbiddenPositions = new HashSet<Point>();
	}
	
	@Override
	public  javafx.geometry.Point2D move() {
		// TODO Auto-generated method stub
		Game g=mGame;
		if(!pathComputed)
		{
			startingPosition = Point.GetPoint(this.getCurrentPosition());
			goal = LineSegment.GetLineSegment(g.getTrack().getFinishLine());
			Line2D[] boundaries = g.getTrack().getOuterBoundary();
			for (int i = 0; i < boundaries.length; i++)
			{
				borders.add(LineSegment.GetLineSegment(boundaries[i]));
			}
			boundaries = g.getTrack().getInnerBoundary();
			for (int i = 0; i < boundaries.length; i++)
			{
				borders.add(LineSegment.GetLineSegment(boundaries[i]));
			}
			for (int i = 0; i < g.getPlayerList().length; i++)
			{
				forbiddenPositions.add(Point.GetPoint(g.getPlayerList()[i].getCurrentPosition()));
			}
			State state = A_star(0,0);
			path = new State[state.TurnsToReach()];
			turn = state.TurnsToReach()-1;
			while (state.Predecessor() != null)
			{
				path[turn] = state;
				state = state.Predecessor();
				turn--;
			}
			turn = turn + 1;
			pathComputed = true;
		}
		else 
		{
			turn++;
			if (lastPosition.X() == (int)this.getCurrentPosition().getX() && lastPosition.Y() == (int)this.getCurrentPosition().getY() && ((int)this.getCurrentVelocity().getX() != 0 || (int)this.getCurrentVelocity().getY() != 0))
			{
				startingPosition = Point.GetPoint(this.getCurrentPosition());
				closedList = new HashSet<State>();
				openList = new PriorityQueue<State>(11,new StateComparator());
				Data.Clear();
				forbiddenPositions = new HashSet<Point>();
				for (int i = 0; i < g.getPlayerList().length; i++)
				{
					forbiddenPositions.add(Point.GetPoint(g.getPlayerList()[i].getCurrentPosition()));
				}
				State state = A_star((int)this.getCurrentVelocity().getX(),(int)this.getCurrentVelocity().getY());
				path = new State[state.TurnsToReach()];
				turn = state.TurnsToReach()-1;
				while (state.Predecessor() != null)
				{
					path[turn] = state;
					state = state.Predecessor();
					turn--;
				}
				turn = turn +1;
			}
		}
		lastPosition = Point.GetPoint(this.getCurrentPosition());
		System.out.println(String.format("turn = %d", turn));
		return path[turn].Position().ToPoint2D();
	}
	
	public boolean isAI() {
		return true;
	}
	
	private State A_star(int velocityX, int velocityY)
	{
		State startingState = State.GetState(startingPosition,velocityX,velocityY);
		startingState.TurnsToReach(0);
		openList.add(startingState);
		while (openList.size() > 0)
		{
			State currentState = openList.poll();
			if (closedList.contains(currentState))
			{
				continue;
			}
			if (currentState.Predecessor() != null)
			{
				if (goal.IntersectWith(LineSegment.GetLineSegment(currentState.Position(),currentState.Predecessor().Position())))
				{
					return currentState;
				}
			}
			closedList.add(currentState);
			expandState(currentState);
		}
		return null;		
	}
	private void expandState(State currentState)
	{
		for (State successor : getSuccessors(currentState))
		{
			if (closedList.contains(successor))
			{
				continue;
			}
			int turnsToReach = currentState.TurnsToReach() + 1;
			if (openList.contains(successor) && turnsToReach >= successor.TurnsToReach())
			{
				continue;
			}
			successor.Predecessor(currentState);
			successor.TurnsToReach(turnsToReach);
			successor.ApproximateTurnsToFinish(turnsToReach + heuristicDistanceToGoal(successor));
			openList.add(successor);
		}
	}
	private HashSet<State> getSuccessors(State currentState)
	{
		HashSet<State> successors = new HashSet<State>();
		for (int accelerationX = -1; accelerationX <= 1; accelerationX++)
		{
			for (int accelerationY = -1; accelerationY <= 1; accelerationY++)
			{
				boolean valid = true;
				boolean reachesGoal = false;
				double goalDistance = 0;
				State successor = State.GetState(currentState,accelerationX,accelerationY);
				if (forbiddenPositions.contains(successor.Position()))
				{
					continue;
				}
				LineSegment nextMove = LineSegment.GetLineSegment(currentState.Position(), successor.Position());
				if (nextMove.IntersectWith(goal))
				{
					reachesGoal = true;
					goalDistance = goal.GetIntersectionDistance(nextMove, currentState.Position());
				}
				for (LineSegment border : borders)
				{
					if(nextMove.IntersectWith(border))
					{
						if (reachesGoal)
						{
							double borderDistance = border.GetIntersectionDistance(nextMove, currentState.Position());
							//double: goalDistance < borderDistance
							if (goalDistance < borderDistance)
							{
								continue;
							}
						}
						valid = false;
						break;
					}
				}
				if (valid)
				{
					successors.add(successor);
				}
			}
		}
		return successors;
	}
	private int heuristicDistanceToGoal(State s)
	{
		int velocityX = s.VelocityX();
		int velocityY = s.VelocityY();
		int positionX = s.Position().X();
		int positionY = s.Position().Y();
		int k = 0;
		int accumulatedAcceleration = 0;
		if (LineSegment.GetLineSegment(s.Position(), s.Predecessor().Position()).IntersectWith(goal))
		{
			return 0;
		}
		while (true)
		{
			k++;
			accumulatedAcceleration += k;
			positionX = positionX+velocityX;
			positionY = positionY+velocityY;
			Point bottomLeft = Point.GetPoint(positionX - accumulatedAcceleration,positionY - accumulatedAcceleration);
			Point bottomRight =  Point.GetPoint(positionX + accumulatedAcceleration,positionY - accumulatedAcceleration);
			Point topLeft = Point.GetPoint(positionX - accumulatedAcceleration,positionY + accumulatedAcceleration);
			Point topRight =  Point.GetPoint(positionX + accumulatedAcceleration,positionY + accumulatedAcceleration);
			LineSegment left = LineSegment.GetLineSegment(bottomLeft, topLeft);
			LineSegment bottom = LineSegment.GetLineSegment(bottomLeft, bottomRight);
			LineSegment right = LineSegment.GetLineSegment(bottomRight, topRight);
			LineSegment top = LineSegment.GetLineSegment(topLeft, topRight);
			if (left.IntersectWith(goal))
			{
				break;
			}
			if (bottom.IntersectWith(goal))
			{
				break;
			}
			if (right.IntersectWith(goal))
			{
				break;
			}
			if (top.IntersectWith(goal))
			{
				break;
			}
			if (bottomLeft.X() <= goal.P1().X() && goal.P1().X() <= topRight.X())
			{
				if (bottomLeft.Y() <= goal.P1().Y() && goal.P1().Y() <= topRight.Y())
				{
					break;
				}
			}
		}
		return k;
	}

}