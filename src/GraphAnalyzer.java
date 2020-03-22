public class GraphAnalyzer
{

  /* ( Program Entry ) ****************************************************** */

  public static void main(String[] args)
  {
    //startFullTest();
	startMstTest();
  }
  
  public static void startMstTest() {
	  int [] graphVertexCounts = { 20, 100, 500, 1000 };
	  
	  double startingProbability = 0.0;
	  double endingProbability = 1.00004;
	  double probabilityIncrement = 0.02;
	  
	  
	  
	  System.out.print("Connected component analysis for undirected graphs of sizes ");

	    for (int i = 0; i < graphVertexCounts.length; ++i)
	      System.out.print(graphVertexCounts[i] + ", ");

	    System.out.println();
	  System.out
        .format(
            "with edge probabilities ranging from %.2f to %.2f in increments of %.2f",
            startingProbability, endingProbability, probabilityIncrement);
      System.out.println();
      
      for (int graphSizeIndex = 0; graphSizeIndex < graphVertexCounts.length; ++graphSizeIndex)
      {
    	  System.out.println("========= " + graphVertexCounts[graphSizeIndex] + " Nodes =========");
    	  for (double currentProbability = startingProbability; currentProbability <= endingProbability; currentProbability += probabilityIncrement)
          {
    		  RandomGraph randomGraph = new RandomGraph(graphVertexCounts[graphSizeIndex],
    	              currentProbability);
    		  RandomGraphUtils randomGraphUtils = new RandomGraphUtils(randomGraph);
    		    
    		  randomGraphUtils.performDfs();
    		  double[] mstResults = randomGraphUtils.findMst();
    		  double mstAvg = 0;
    		  
    		  for (int i = 0; i < mstResults.length; ++i)
    		  {
    		     mstAvg += mstResults[i];
    		  }
    		  System.out.println("   " + mstAvg/randomGraphUtils.getConnectedComponents() + "      " + randomGraphUtils.getMstDiameters()/randomGraphUtils.getConnectedComponents());
          }
      }
  }

  public static void startFullTest()
  {
    int[] graphVertexCounts = { 20, 50, 500, 1000 };

    double startingProbability = 0.0;
    double endingProbability = 1.00004;
    double probabilityIncrement = 0.02;

    int sampleSize = 100;

    int[] connectedComponents = new int[sampleSize];

    int sum;
    double mean;
    double variance;
    double standardDeviation;

    System.out.print("Connected component analysis for undirected graphs of sizes ");

    for (int i = 0; i < graphVertexCounts.length; ++i)
      System.out.print(graphVertexCounts[i] + ", ");

    System.out.println();

    System.out
        .format(
            "with edge probabilities ranging from %.2f to %.2f in increments of %.2f on a sample size of %d\n",
            startingProbability, endingProbability, probabilityIncrement, sampleSize);
    System.out.println();

    // Iterate through each graph size
    for (int graphSizeIndex = 0; graphSizeIndex < graphVertexCounts.length; ++graphSizeIndex)
    {
      System.out.println("Graph: " + graphVertexCounts[graphSizeIndex] + " vertices");
      System.out
          .println("--------------------------------------------------------------------------------");

      for (double currentProbability = startingProbability; currentProbability <= endingProbability; currentProbability += probabilityIncrement)
      {
        sum = 0;
        mean = 0.0;
        variance = 0.0;

        for (int currentSample = 0; currentSample < sampleSize; ++currentSample)
        {
          // Create graph
          RandomGraph randomGraph = new RandomGraph(graphVertexCounts[graphSizeIndex],
              currentProbability);

          RandomGraphUtils randomGraphUtils = new RandomGraphUtils(randomGraph);

          // Perform a DFS
          randomGraphUtils.performDfs();

          // Store number of connected components
          connectedComponents[currentSample] = randomGraphUtils.getConnectedComponents();
          sum += randomGraphUtils.getConnectedComponents();
        }

        // Calculate mean (��) and standard deviation (��)
        mean = (double) sum / (double) sampleSize;

        for (int i = 0; i < sampleSize; ++i)
          variance += Math.pow((double) connectedComponents[i] - mean, 2.0);

        standardDeviation = Math.sqrt(variance / (double) sampleSize);

        // Display results
        System.out.format("G(%d, %.2f), ��(%.3f), ��(%.3f)\n",
            graphVertexCounts[graphSizeIndex], currentProbability, mean,
            standardDeviation);
      }

      System.out.println();
    }
  }

}
