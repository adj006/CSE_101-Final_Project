import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ConvexHull
{
	private int numOfPoints;                             //number of points within the Unit Circle
	private List<Point> pointList = new ArrayList<Point>();  //Set of all points
	
	public ConvexHull( int p )                           //Constructor sets number of points
	{
		setNumOfPoints(p);                               //sets numOfPoints
		createPointList();                                //creates random set of points
	}

	/*Method to create randomized set of points
	 * within a unit circle of Radius 1.
	 */
	private void createPointList()                        
	{
		for ( int i = 0; i < getNumOfPoints(); i++ ) //until all points are created
		{
			Point rp = randomPoint();                //random point created
			//System.out.print("Randomly generated point " + i + " at ");
			//rp.printLocation();
			
			pointList.add(rp);
		}
	}

	/*
	 * Creates a random point in polar coords
	 * within a Unit Circle and returns it
	 */
	private Point randomPoint()
	{
		double u = Math.random() + Math.random();  //initial radius to random value
		double t = Math.random()*2*Math.PI;        //random angle set
		double r;                                  //final radius
		
		if ( u > 1 )                               //check if u is within Unit circle
			r = 2 - u;                             //Fold back 
		else
			r = u;                                 //set final radius to u
		
		return new Point( r*Math.cos(t), r*Math.sin(t) ); //returns Point
	}
	
	private int getNumOfPoints()
	{
		return numOfPoints;
	}
	
	private void setNumOfPoints(int p)
	{
		this.numOfPoints = p;
	}
	
	/*
	 * Point class for ConvexHull.
	 * Made Comparable to be used within SetTree
	 */
	public static class Point implements Comparable<Point>
	{
		private double x;
		private double y;
		public Point( double x, double y )
		{
			setX(x);
			setY(y);
		}
		
		public void setX( double x )
		{
			this.x = x;
		}
		
		public void setY( double y )
		{
			this.y = y;
		}
		
		public double getX()
		{
			return x;
		}
		
		public double getY()
		{
			return y;
		}
		
		public void printLocation() {
		    System.out.println("(" + getX() + "," + getY() + ")");	
		}
		
		public boolean equals( Point p )
		{
			if( (getX() == p.getX()) && (getY() == p.getY()) )
				return true;
			return false;
		}
		
		public int compareTo( Point other )
		{
			if( other.getX() < getX() )
				return 1;
			else if( other.getX() == getX() )
				return 0;
			return -1;
		}

	}
	
	/*
	 * for Testing in main()
	 */
	public int listSize()
	{
		return pointList.size();
	}
	
	public int findShells() {
		int iterations = 0;
		
		while (!pointList.isEmpty()) {
			if (pointList.size() > 3) {
				int a = 0;
				Point[] points = new Point[pointList.size()];
				List<Point> hull = new ArrayList<Point>();
				Point pointOnHull = findSource();
				Iterator<Point> iterator = pointList.iterator();
				//System.out.print("Source at " );
				//pointOnHull.printLocation();
				Point current;
				
				while (iterator.hasNext()) {
					points[a] = iterator.next();
					//System.out.print("Point in array with coords ");
					//points[a].printLocation();
					++a;
				}
				//System.out.println("Iteration " + iterations + " array size is: " + points.length);
				pointList.remove(pointOnHull);
				
				do {
					hull.add(pointOnHull);
					//System.out.print("pointOnHull is ");
					//pointOnHull.printLocation();
					current = points[0];
					//System.out.print("current is ");
					//current.printLocation();
					for (int i = 1; i < points.length; i++) {
						Point nextPoint = points[i];
						//System.out.println("inside array loop");
						if (current.equals(pointOnHull) || 
						    (orientation(pointOnHull, current, nextPoint) == -1)) {
							//System.out.println("inside orientation if");
							current = nextPoint;
						}
					}
					pointOnHull = current;
					pointList.remove(pointOnHull);
					//System.out.println("Size of hull" + iterations + " is: " + hull.size());
				}
				while (!current.equals(hull.get(0)));
				++iterations;
			}
			else {
				pointList.clear();
				++iterations;
			}
		}
		
		return iterations;
	}
	
	/*
	 * finds left most source point to start iterative shelling from
	 */
	public Point findSource()
	{
		Iterator<Point> itr = pointList.iterator();
		Point source = itr.next();
		
		while( itr.hasNext() )
		{
			Point p = itr.next();
			
			if( source.getX() > p.getX() )
				source = p;
		}
		
		return source;
	}
	
	/*
	 * used within iterative shelling to check for outermost points 
	 * of the Hull of the current pointSet
	 */
	public int orientation( Point p1, Point p2, Point p )
	{
		double val = (p2.getX() - p1.getX())*(p.getY() - p1.getY()) -
				  (p.getX() - p1.getX())*(p2.getY() - p1.getY());
		if (val > 0)
			return -1;
		if (val < 0)
			return 1;
		return 0;	    
	}
}