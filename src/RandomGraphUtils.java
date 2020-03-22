import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RandomGraphUtils
{
  private RandomGraph                             _randomGraph;
  private HashMap<RandomGraph.Vertex, BreadCrumb> _crumbs;

  private int                                     _connectedComponents;
  private int                                     _visitCount;
  private double								  _mstDiameters;

  /* ( Constructors ) ******************************************************* */

  public RandomGraphUtils(RandomGraph randomGraph)
  {
    setVisitCount(0);
    setConnectedComponents(0);
    setRandomGraph(randomGraph);
    createCrumbs();
  }

  /* ( Initialization ) ***************************************************** */

  private void createCrumbs()
  {
    _crumbs = new HashMap<RandomGraph.Vertex, BreadCrumb>();

    Iterator<RandomGraph.Vertex> i = _randomGraph.iterator();

    while (i.hasNext())
      _crumbs.put(i.next(), new BreadCrumb());
  }

  /* ( Traversal ) ********************************************************** */

  public void performDfs()
  {
    setAllCrumbsVisitedTo(false);

    setConnectedComponents(0);
    setVisitCount(0);

    Iterator<RandomGraph.Vertex> i = _randomGraph.iterator();

    while (i.hasNext())
    {
      RandomGraph.Vertex v = i.next();
      BreadCrumb b = _crumbs.get(v);

      if (!b.isVisited())
      {
        setConnectedComponents(getConnectedComponents() + 1);
        explore(v);
      }
    }
  }

  public double[] findMst()
  {
	double[] mstCosts = new double[getConnectedComponents()];
	
    int connectedComponents = getConnectedComponents();

    List<RandomGraph.Vertex> vertexList = null;

    setAllCrumbsVisitedTo(false);

    for (int cc = 0; cc < connectedComponents; ++cc)
    {
      vertexList = new LinkedList<RandomGraph.Vertex>();
      mstCosts[cc] = 0;

      // Prepare to find the first vertex for the MST
      Iterator<RandomGraph.Vertex> vertexIterator = getRandomGraph().iterator();

      RandomGraph.Vertex selectedVertex = null;

      // Find the first vertex to start the MST
      boolean firstNodeFound = false;

      while (vertexIterator.hasNext() && !firstNodeFound)
      {
        selectedVertex = vertexIterator.next();
        BreadCrumb selectedVertexCrumb = _crumbs.get(selectedVertex);

        if (selectedVertexCrumb.getConnectedComponent() == cc + 1)
        {
          firstNodeFound = true;
          selectedVertexCrumb.setVisited(true);
          vertexList.add(selectedVertex);          
        }
      }

      // Create a new tree with the selected vertex as the root
      RandomGraph.Tree tree = new RandomGraph.Tree(selectedVertex);

      boolean minimumSpanningTreeComplete = false;

      while (!minimumSpanningTreeComplete)
      {
        // We visit all the edges of all the vertices in the tree searching for
        // the cheapest edge
        boolean edgeFound = false;
        double cheapestEdge = Double.MAX_VALUE;

        RandomGraph.Vertex parentVertex = null;
        RandomGraph.Vertex nextVertex = null;

        Iterator<RandomGraph.Vertex> knownVertexIterator = vertexList.iterator();

        // Find the cheapest edge
        while (knownVertexIterator.hasNext())
        {
          RandomGraph.Vertex candidateVertex = knownVertexIterator.next();

          Iterator<RandomGraph.Edge> edges = candidateVertex.iterator();

          while (edges.hasNext())
          {
            RandomGraph.Edge edge = edges.next();

            RandomGraph.Vertex tempVertex = edge.getVertex();
            BreadCrumb tempCrumb = _crumbs.get(tempVertex);

            if (!tempCrumb.isVisited() && edge.getWeight() < cheapestEdge)
            {
              parentVertex = candidateVertex;
              cheapestEdge = edge.getWeight();
              nextVertex = tempVertex;
              edgeFound = true;
            }
          }
        }

        if (edgeFound)
        {
          mstCosts[cc] += cheapestEdge;
          vertexList.add(nextVertex);
          _crumbs.get(nextVertex).setVisited(true);

          tree.insert(parentVertex, nextVertex, cheapestEdge);
        }
        else
        {
          minimumSpanningTreeComplete = true;
          setMstDiameters(tree.longestPath(tree.getRoot()));
        }
      }
    }
    
    return mstCosts;
  }

  private void explore(RandomGraph.Vertex vertex)
  {
    BreadCrumb currentVertexCrumb = _crumbs.get(vertex);
    currentVertexCrumb.setVisited(true);
    currentVertexCrumb.setConnectedComponent(getConnectedComponents());
    setVisitCount(getVisitCount() + 1);
    currentVertexCrumb.setPrevisit(getVisitCount());

    Iterator<RandomGraph.Edge> edges = vertex.iterator();

    while (edges.hasNext())
    {
      RandomGraph.Vertex edgeVertex = edges.next().getVertex();
      BreadCrumb edgeVertexCrumb = _crumbs.get(edgeVertex);

      if (!edgeVertexCrumb.isVisited())
      {
        explore(edgeVertex);
      }
    }

    setVisitCount(getVisitCount() + 1);
    currentVertexCrumb.setPostvisit(getVisitCount());
  }

  private void setAllCrumbsVisitedTo(boolean visited)
  {
    Iterator<RandomGraph.Vertex> i = _randomGraph.iterator();

    while (i.hasNext())
    {
      BreadCrumb b = _crumbs.get(i.next());
      b.setVisited(visited);
    }
  }

  /* ( Accessors/Mutators ) ************************************************** */

  public int getConnectedComponents()
  {
    return _connectedComponents;
  }

  private void setConnectedComponents(int connectedComponents)
  {
    _connectedComponents = connectedComponents;
  }

  private void setRandomGraph(RandomGraph randomGraph)
  {
    _randomGraph = randomGraph;
  }

  private RandomGraph getRandomGraph()
  {
    return _randomGraph;
  }

  private int getVisitCount()
  {
    return _visitCount;
  }

  private void setVisitCount(int visitCount)
  {
    _visitCount = visitCount;
  }

  private void setMstDiameters(double d) 
  {
	_mstDiameters += d;
  }
  
  public double getMstDiameters()
  {
	return _mstDiameters;
  }
  /* ( Graph Display ) ****************************************************** */

  public void printGraphStructure()
  {
    Iterator<RandomGraph.Vertex> i = getRandomGraph().iterator();

    while (i.hasNext())
      System.out.println(i.next().toString());
  }

  public void printDfsSummary()
  {
    System.out.println("Connected Components: " + _connectedComponents);
    System.out.println("Vertices visited    : " + _visitCount);
  }

  public void printDfsDetails()
  {
    Iterator<RandomGraph.Vertex> i = getRandomGraph().iterator();

    while (i.hasNext())
    {
      RandomGraph.Vertex v = i.next();
      BreadCrumb b = _crumbs.get(v);

      System.out.println("Vertex: " + v.getLabel() + ": previsit(" + b.getPrevisit()
          + "), postvisit(" + b.getPostvisit() + "), connected component("
          + b.getConnectedComponent() + ")");
    }
  }

  /* ( Classes ) ************************************************************ */

  private class BreadCrumb
  {
    private boolean _visited;
    private int     _connectedComponent;
    private int     _previsit;
    private int     _postvisit;

    public BreadCrumb()
    {
      setVisited(false);
      setConnectedComponent(0);
      setPrevisit(0);
      setPostvisit(0);
    }

    public void setVisited(boolean visited)
    {
      _visited = visited;
    }

    public boolean isVisited()
    {
      return _visited;
    }

    public void setConnectedComponent(int connectedComponent)
    {
      _connectedComponent = connectedComponent;
    }

    public int getConnectedComponent()
    {
      return _connectedComponent;
    }

    public void setPrevisit(int previsit)
    {
      _previsit = previsit;
    }

    public int getPrevisit()
    {
      return _previsit;
    }

    public void setPostvisit(int postvisit)
    {
      _postvisit = postvisit;
    }

    public int getPostvisit()
    {
      return _postvisit;
    }
  }

}
