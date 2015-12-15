package src.main.java.logic;
import java.util.Comparator;
public class Dijkstra2DEntryComparator implements Comparator<Dijkstra2DEntry>
{
    @Override
    public int compare(Dijkstra2DEntry a, Dijkstra2DEntry b)
    {
    	//TODO: Check this
    	if( a.getValue() == -1 && b.getValue() != -1 )
    		return 1;
    	if( a.getValue() != -1 && b.getValue() == -1 )
    		return -1;
    	
        return a.getValue() - b.getValue();
    }
}
