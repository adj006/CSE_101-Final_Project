
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int [] graphPointCounts = { 20, 100, 500, 1000 };
		System.out.println("          Convex Hulls          ");
		System.out.println("Point Count          Shell Iterations");
		
		for (int graphSizeIndex = 0; graphSizeIndex < graphPointCounts.length; ++graphSizeIndex)
	    {
			ConvexHull c = new ConvexHull(graphPointCounts[graphSizeIndex]);
			System.out.println("    " + graphPointCounts[graphSizeIndex] + "               " + c.findShells());
	    }
	}

}
