package src.main.java.logic.AIstar;

import java.util.Comparator;

public class StateComparator implements Comparator<State> {
    @Override
    public int compare(State a, State b) {
        return a.ApproximateTurnsToFinish() - b.ApproximateTurnsToFinish();
    }
}
