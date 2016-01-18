package src.main.java.logic;

import java.io.File;
import java.util.Comparator;

public class TrackFilesComparator implements Comparator<File> {
    @Override
    public int compare(File a, File b) {
        return a.getName().compareTo(b.getName());
    }
}
