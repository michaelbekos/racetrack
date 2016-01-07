package src.main.logic.TrackCreator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;


public class IndianapolisCreator
{
	public static void main(String[] args)
	{
		if (args.length == 3)
		{
			int l = Integer.parseInt(args[0]);
			int h = Integer.parseInt(args[1]);
			int w = Integer.parseInt(args[2]);
			writeIndianapolisXML(l,h,w);
			System.out.println("Track generation done.");
		}
		else
		{
			System.out.println("Usage: IndianapolisCreator l h w");
			int l = 150;
			int h = 10;
			int w = 5;
			System.out.println("Using default values l=150, h=10, w=5.");
			writeIndianapolisXML(l,h,w);
			System.out.println("Track generation done.");
		}
	}
	
	private static void writeIndianapolisXML(int l, int h, int w)
	{
		//Start XML structure
		Element indyTrackElement = new Element("Track");
		Document indyFile = new Document( indyTrackElement );
		indyTrackElement.setAttribute("name", String.format("Indianapolis(%d,%d,%d)",l,h,w));
		indyTrackElement.setAttribute("type", "Circuit");
		
		//Create outer boundaries
		Element indyOuterBoundariesElement = new Element("OuterBoundaries");
		indyTrackElement.addContent(indyOuterBoundariesElement);
		Element indyOuterBoundaryPointElement1 = new Element("Point");
		indyOuterBoundaryPointElement1.setAttribute("id", "0");
		indyOuterBoundaryPointElement1.setAttribute("x", String.format("%d", 5+w+l/2+1)); 
		indyOuterBoundaryPointElement1.setAttribute("y", String.format("%d", 5)); 
		indyOuterBoundariesElement.addContent(indyOuterBoundaryPointElement1);
		Element indyOuterBoundaryPointElement2 = new Element("Point");
		indyOuterBoundaryPointElement2.setAttribute("id", "1");
		indyOuterBoundaryPointElement2.setAttribute("x", String.format("%d", 5)); 
		indyOuterBoundaryPointElement2.setAttribute("y", String.format("%d", 5)); 
		indyOuterBoundariesElement.addContent(indyOuterBoundaryPointElement2);
		Element indyOuterBoundaryPointElement3 = new Element("Point");
		indyOuterBoundaryPointElement3.setAttribute("id", "2");
		indyOuterBoundaryPointElement3.setAttribute("x", String.format("%d", 5)); 
		indyOuterBoundaryPointElement3.setAttribute("y", String.format("%d", 6+2*w+h)); 
		indyOuterBoundariesElement.addContent(indyOuterBoundaryPointElement3);
		Element indyOuterBoundaryPointElement4 = new Element("Point");
		indyOuterBoundaryPointElement4.setAttribute("id", "3");
		indyOuterBoundaryPointElement4.setAttribute("x", String.format("%d", 6+l+2*w)); 
		indyOuterBoundaryPointElement4.setAttribute("y", String.format("%d", 6+2*w+h)); 
		indyOuterBoundariesElement.addContent(indyOuterBoundaryPointElement4);
		Element indyOuterBoundaryPointElement5 = new Element("Point");
		indyOuterBoundaryPointElement5.setAttribute("id", "4");
		indyOuterBoundaryPointElement5.setAttribute("x", String.format("%d", 6+l+2*w)); 
		indyOuterBoundaryPointElement5.setAttribute("y", String.format("%d", 5)); 
		indyOuterBoundariesElement.addContent(indyOuterBoundaryPointElement5);
		Element indyOuterBoundaryPointElement6 = new Element("Point");
		indyOuterBoundaryPointElement6.setAttribute("id", "5");
		indyOuterBoundaryPointElement6.setAttribute("x", String.format("%d", 5+w+l/2+1)); 
		indyOuterBoundaryPointElement6.setAttribute("y", String.format("%d", 5)); 
		indyOuterBoundariesElement.addContent(indyOuterBoundaryPointElement6);

		//Create inner boundaries
		Element indyInnerBoundariesElement = new Element("InnerBoundaries");
		indyTrackElement.addContent(indyInnerBoundariesElement);
		Element indyInnerBoundaryPointElement1 = new Element("Point");
		indyInnerBoundaryPointElement1.setAttribute("id", "0");
		indyInnerBoundaryPointElement1.setAttribute("x", String.format("%d", 5+w+l/2+1)); 
		indyInnerBoundaryPointElement1.setAttribute("y", String.format("%d", 6+w)); 
		indyInnerBoundariesElement.addContent(indyInnerBoundaryPointElement1);
		Element indyInnerBoundaryPointElement2 = new Element("Point");
		indyInnerBoundaryPointElement2.setAttribute("id", "1");
		indyInnerBoundaryPointElement2.setAttribute("x", String.format("%d", 6+w)); 
		indyInnerBoundaryPointElement2.setAttribute("y", String.format("%d", 6+w)); 
		indyInnerBoundariesElement.addContent(indyInnerBoundaryPointElement2);
		Element indyInnerBoundaryPointElement3 = new Element("Point");
		indyInnerBoundaryPointElement3.setAttribute("id", "2");
		indyInnerBoundaryPointElement3.setAttribute("x", String.format("%d", 6+w)); 
		indyInnerBoundaryPointElement3.setAttribute("y", String.format("%d", 5+w+h)); 
		indyInnerBoundariesElement.addContent(indyInnerBoundaryPointElement3);
		Element indyInnerBoundaryPointElement4 = new Element("Point");
		indyInnerBoundaryPointElement4.setAttribute("id", "3");
		indyInnerBoundaryPointElement4.setAttribute("x", String.format("%d", 5+w+l)); 
		indyInnerBoundaryPointElement4.setAttribute("y", String.format("%d", 5+w+h)); 
		indyInnerBoundariesElement.addContent(indyInnerBoundaryPointElement4);
		Element indyInnerBoundaryPointElement5 = new Element("Point");
		indyInnerBoundaryPointElement5.setAttribute("id", "4");
		indyInnerBoundaryPointElement5.setAttribute("x", String.format("%d", 5+w+l)); 
		indyInnerBoundaryPointElement5.setAttribute("y", String.format("%d", 6+w)); 
		indyInnerBoundariesElement.addContent(indyInnerBoundaryPointElement5);
		Element indyInnerBoundaryPointElement6 = new Element("Point");
		indyInnerBoundaryPointElement6.setAttribute("id", "5");
		indyInnerBoundaryPointElement6.setAttribute("x", String.format("%d", 5+w+l/2)); 
		indyInnerBoundaryPointElement6.setAttribute("y", String.format("%d", 6+w)); 
		indyInnerBoundariesElement.addContent(indyInnerBoundaryPointElement6);
		Element indyInnerBoundaryPointElement7 = new Element("Point");
		indyInnerBoundaryPointElement7.setAttribute("id", "6");
		indyInnerBoundaryPointElement7.setAttribute("x", String.format("%d", 5+w+l/2)); 
		indyInnerBoundaryPointElement7.setAttribute("y", String.format("%d", 5)); 
		indyInnerBoundariesElement.addContent(indyInnerBoundaryPointElement7);
		Element indyInnerBoundaryPointElement8 = new Element("Point");
		indyInnerBoundaryPointElement8.setAttribute("id", "7");
		indyInnerBoundaryPointElement8.setAttribute("x", String.format("%d", 5+w+l/2)); 
		indyInnerBoundaryPointElement8.setAttribute("y", String.format("%d", 6+w)); 
		indyInnerBoundariesElement.addContent(indyInnerBoundaryPointElement8);
		Element indyInnerBoundaryPointElement9 = new Element("Point");
		indyInnerBoundaryPointElement9.setAttribute("id", "8");
		indyInnerBoundaryPointElement9.setAttribute("x", String.format("%d", 5+w+l/2+1)); 
		indyInnerBoundaryPointElement9.setAttribute("y", String.format("%d", 6+w)); 
		indyInnerBoundariesElement.addContent(indyInnerBoundaryPointElement9);

		//Dimension
		Element indyDimensionElement = new Element("Dimension");
		indyTrackElement.addContent(indyDimensionElement);
		indyDimensionElement.setAttribute("width", String.format("%d",10 + 2*w + l));
		indyDimensionElement.setAttribute("height", String.format("%d",10 + 2*w + h));
		
		//Starting positions
		Element indyStartingPositionsElement = new Element("StartingPositions");
		indyTrackElement.addContent(indyStartingPositionsElement);
		for (int i = 0; i < w; i++)
		{
			Element indyStartingPointElement = new Element("Point");
			indyStartingPointElement.setAttribute("x", String.format("%d", 5+w+l/2+1)); 
			indyStartingPointElement.setAttribute("y", String.format("%d", 6+i)); 
			indyStartingPositionsElement.addContent(indyStartingPointElement);
		}
		
		//Finish line
		Element indyFinishLineElement = new Element("FinishLine");
		indyTrackElement.addContent(indyFinishLineElement);
		indyFinishLineElement.setAttribute("x1", String.format("%d", 5+w+l/2-1)); 
		indyFinishLineElement.setAttribute("y1", String.format("%d", 5)); 
		indyFinishLineElement.setAttribute("x2", String.format("%d", 5+w+l/2-1)); 
		indyFinishLineElement.setAttribute("y2", String.format("%d", 6+w)); 
		
		XMLOutputter out = new XMLOutputter();
		try
		{
			out.output( indyFile, new FileOutputStream(System.getProperty("user.dir") + String.format("\\Tracks\\Indianapolis-%d-%d-%d.xml",l,h,w)));
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
