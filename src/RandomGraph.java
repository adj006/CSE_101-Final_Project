import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class RandomGraph
{
  private Random       _randomGenerator;

  private int          _numVertices;
  private double       _edgeProbability;

  private List<Vertex> _vertices;

  /* ( Constructors ) ******************************************************* */

  public RandomGraph(int numVertices, double edgeProbability)
  {
    _randomGenerator = new Random();

    setNumVertices(numVertices);
    setEdgeProbability(edgeProbability);

    createVertices();
    createEdges();
  }

  /* ( Initialization ) ***************************************************** */

  private void createVertices()
  {
    _vertices = new LinkedList<Vertex>();

    for (int i = 0; i < _numVertices; ++i)
      _vertices.add(new Vertex(i));
  }

  private void createEdges()
  {
    ListIterator<Vertex> currentVertexIterator = _vertices.listIterator();

    int index = 1;
    while (currentVertexIterator.hasNext())
    {
      Vertex currentVertex = currentVertexIterator.next();
      ListIterator<Vertex> nextVertexIterator = _vertices.listIterator(index);

      while (nextVertexIterator.hasNext())
      {
        Vertex nextVertex = nextVertexIterator.next();

        if (_randomGenerator.nextDouble() < getEdgeProbability())
        {
          // Edge from (v, u)
          double edgeWeight = _randomGenerator.nextDouble();
          
          Edge toCurrent = new Edge(currentVertex, edgeWeight);

          // Edge from (u, v)
          Edge toNext = new Edge(nextVertex, edgeWeight);

          currentVertex.addEdge(toNext);
          nextVertex.addEdge(toCurrent);
        }
      }

      ++index;
    }
  }

  /* ( Iterators ) ********************************************************** */

  public Iterator<Vertex> iterator()
  {
    return _vertices.iterator();
  }

  /* ( Private Helpers ) **************************************************** */

  private void setEdgeProbability(double edgeProbability)
  {
    _edgeProbability = edgeProbability;
  }

  private void setNumVertices(int numVertices)
  {
    _numVertices = numVertices;
  }

  private double getEdgeProbability()
  {
    return _edgeProbability;
  }

  private int getNumVertices()
  {
    return _numVertices;
  }

  /* ( Classes ) ************************************************************ */

  public static class Vertex
  {
    private int        _label;
    private List<Edge> _edges;

    public Vertex(int label)
    {
      setLabel(label);

      _edges = new LinkedList<Edge>();
    }

    public int getLabel()
    {
      return _label;
    }

    public void setLabel(int label)
    {
      _label = label;
    }

    public Iterator<Edge> iterator()
    {
      return _edges.iterator();
    }

    public void addEdge(Edge edge)
    {
      _edges.add(edge);
    }

    @Override
    public String toString()
    {
      String s = "Vertex [" + Integer.toString(getLabel()) + "], Edges { ";

      Iterator<Edge> i = this.iterator();

      while (i.hasNext())
        s += i.next().toString() + ", ";

      s += " }";

      return s;
    }
  }

  public static class Edge
  {
    private Vertex _vertex;
    private double _weight;

    public Edge(Vertex vertex, double weight)
    {
      setVertex(vertex);
      setWeight(weight);
    }

    public Vertex getVertex()
    {
      return _vertex;
    }

    public void setVertex(Vertex vertex)
    {
      _vertex = vertex;
    }

    public double getWeight()
    {
      return _weight;
    }

    public void setWeight(double weight)
    {
      _weight = weight;
    }

    @Override
    public String toString()
    {
      BigDecimal bd = new BigDecimal(_weight);
      bd = bd.setScale(3, BigDecimal.ROUND_HALF_UP);
      return "(" + Integer.toString(_vertex.getLabel()) + ", " + bd.doubleValue() + ")";
    }

  }
 ///////////******BEGIN JOHN'S EDIT*******////////////
 //////HANDLES THE MST TREE TO DETERMINE DIAMETER//////
  public static class Tree {
	  private HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
	  private Node root;
	  
	  public Tree (Vertex r) {
		  setRoot(r);
		  nodes.put(new Integer(r.getLabel()), getRoot());
	  }
	  
	  public void insert (Vertex parent, Vertex child, double minEdge) {
		  Node n = new Node(child);
		  Integer parentKey = new Integer(parent.getLabel());
		  n.setCost(minEdge);
		  n.setParent(nodes.get(parentKey));
		  nodes.get(parentKey).addChild(n);
		  nodes.put(new Integer(child.getLabel()), n);
	  }
	  
	  public double longestPath (Node n) {
		  double lp = 0.0;
		  
		  if (n.children.isEmpty()) {
			  return n.getCost();
		  }
		  else {
			  Iterator<Node> nodeIterator = n.childIterator();
			  
			  while (nodeIterator.hasNext()) {
				  Node c = nodeIterator.next();
				  double current = n.getCost() + longestPath(c);
				  if (lp < current) {
					  lp = current;
				  }
			  }
		  }
		  
		  return lp;
	  }
	  
	  
	  public void setRoot (Vertex r) {
		  root = new Node (r);
	  }
	  
	  public Node getRoot () {
		  return root;
	  }
  }
  
 //////TREE REPRESENTATION OF VERTEXES FROM THE GRAPH///////
  public static class Node {
	  private Vertex refVertex;
	  private Node parent;
	  private List<Node> children;
	  private double cost;
	  
	  public Node (Vertex v) {
		  setVertex(new Vertex(v.getLabel()));
		  setCost(0.0);
		  children = new LinkedList<Node>();
	  }
	  
	  public void setVertex (Vertex v) {
		  refVertex = v;
	  }
	  
	  public Vertex getVertex () {
		  return refVertex;
	  }
	  
	  public void setParent (Node p) {
		  parent = p;
	  }
	  
	  public Node getParent () {
		  return parent;
	  }
	  
	  public void setCost (double c) {
		  cost = c;
	  }
	  
	  public double getCost() {
		  return cost;
	  }
	  
	  public Iterator<Node> childIterator() {
	      return children.iterator();
	  }

	  public void addChild(Node child) {
	      children.add(child);
	  }
  }
////////////*******END JOHN'S EDIT********////////////
}
