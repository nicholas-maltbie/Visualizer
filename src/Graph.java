import java.util.Set;
import java.util.HashSet;
import java.util.Objects;
import java.util.Vector;
import java.util.Collection;

public class Graph<V, E> {

    private Set<Node> points;
    private Set<Edge> edges;

    public Graph() {
        points = new HashSet<>();
        edges = new HashSet<>();
    }

    private Node getNode(V value) {
        for(Node point : points)
            if(point.getValue().equals(value))
                return point;
        return null;
    }

    public boolean addPoint(V point) {
        if(containsPoint(point)) {
            throw new IllegalArgumentException("Graph already contians a point with that value");
        }
        return points.add(new Node(point));
    }

    public boolean containsPoint(V point) {
        return points.contains(new Node(point));
    }

    public E getEdge(V source, V target) {
        if(source == null || target == null) {
            throw new IllegalArgumentException("Target and source must both be non null values");
        }
        if(!(containsPoint(source) && containsPoint(target))) {
            throw new ValueNotFoundException("Could not find target or source in the graph");
        }
        Set<Edge> connected = getNode(source).getEdges();
        connected.addAll(getNode(target).getEdges());
        for(Edge edge : connected)
            if(edge.contains(source) && edge.contains(target))
                return edge.getValue();
        return null;
    }

    public void disconnect(V source, V target) {
        if(source == null || target == null) {
            throw new IllegalArgumentException("Target and source must both be non null values");
        }
        if(!(containsPoint(source) && containsPoint(target))) {
            throw new ValueNotFoundException("Could not find target or source in the graph");
        }
        Node sourceNode = getNode(source);
        Node targetNode = getNode(target);
        for(Edge edge : getConnectingEdbes(source, target)) {
            sourceNode.removeEdge(edge);
            targetNode.removeEdge(edge);
            edges.remove(edge);
        }
    }

    private Set<Edge> getConnectingEdbes(V source, V target) {
        if(source == null || target == null) {
            throw new IllegalArgumentException("Target and source must both be non null values");
        }
        if(!(containsPoint(source) && containsPoint(target))) {
            throw new ValueNotFoundException("Could not find target or source in the graph");
        }
        Set<Edge> connecting = new HashSet<>();
        Set<Edge> connected = getNode(source).getEdges();
        connected.addAll(getNode(target).getEdges());
        for(Edge edge : connected) {
            if(edge.contains(source) && edge.contains(target)) {
                connecting.add(edge);
            }
        }
        return connecting;

    }

    public Set<E> getEdges(V source, V target) {
        if(source == null || target == null) {
            throw new IllegalArgumentException("Target and source must both be non null values");
        }
        if(!(containsPoint(source) && containsPoint(target))) {
            throw new ValueNotFoundException("Could not find target or source in the graph");
        }
        Set<E> connecting = new HashSet<>();
        Set<Edge> connected = getNode(source).getEdges();
        connected.addAll(getNode(target).getEdges());
        for(Edge edge : connected) {
            if(edge.contains(source) && edge.contains(target)) {
                connecting.add(edge.getValue());
            }
        }
        return connecting;
    }

    public Set<E> getEdges(V point) {
        Node node = getNode(point);
        Set<E> edges = new HashSet<>();
        for(Edge edge : node.getEdges()) {
            edges.add(edge.getValue());
        }
        return edges;
    }

    public Set<V> getNeighbors(V point) {
        Set<V> neighbors = new HashSet<>();
        for(Edge edge : getNode(point).getEdges()) {
            neighbors.addAll(edge.getConnectedValues());
        }
        neighbors.remove(point);
        return neighbors;
    }

    public void addEdge(V source, V target, E edge) {
        Node sourceNode = getNode(source);
        Node targetNode = getNode(target);
        Edge edgeContainer = new Edge(sourceNode, targetNode, edge);
        edges.add(edgeContainer);
        sourceNode.addEdge(edgeContainer);
        targetNode.addEdge(edgeContainer);
    }

    public Set<V> getPoints() {
        Set<V> contained = new HashSet<>();
        for(Node point : points)
            contained.add(point.getValue());
        return contained;
    }

    public boolean removePoint(V point) {
        if(!containsPoint(point)) {
            return false;
        }
        Node node = getNode(point);
        for(Edge edge : node.getEdges()) {
            for(Node connected : edge.getConnectedNodes()) {
                connected.removeEdge(edge);
            }
            edges.remove(edge);
        }
        points.remove(node);
        return true;
    }

    private class Node {
        private V value;
        private Set<Edge> edges;

        public Node(V value) {
            this.value = value;
        }

        public Set<Edge> getEdges() {
            return new HashSet<Edge>(edges);
        }

        public void removeEdge(Edge edge) {
            edges.remove(edge);
        }

        public Set<Node> getConnected() {
            Set<Node> connected = new HashSet<>();
            for(Edge edge : edges) {
                connected.addAll(edge.getConnectedNodes());
            }
            connected.remove(this);
            return connected;
        }

        public boolean containsEdge(Edge edge) {
            return edges.contains(edge);
        }

        public boolean isConnected(Node other) {
            for(Edge edge : edges) {
                if(edge.getConnectedNodes().contains(other)) {
                    return true;
                }
            }
            return false;
        }

        public void addEdge(Edge edge) {
            edges.add(edge);
        }

        public V getValue() {
            return value;
        }

        @Override
        public boolean equals(Object other) {
            return other != null && other instanceof Graph.Node &&
                    ((value == null && ((Graph.Node) other).value == null) ||
                    ((Graph.Node) other).value.equals(value));
        }

        @Override
        public int hashCode() {
            if (value == null)
                return 0;
            return value.hashCode();
        }

        @Override
        public String toString() {
            if (value != null) {
                return "Node " + value.toString() + "  with " +
                        Integer.toString(edges.size()) + " edges";
            }
            return "Node null with " + Integer.toString(edges.size()) + " edges";
        }
    }

    private class Edge {
        private E value;
        private Collection<Node> nodes = new Vector<>();

        public Edge(Node source, Node dest, E value) {
            this.value = value;
            nodes.add(source);
            nodes.add(dest);
        }

        public boolean isConnected(Node node) {
            return nodes.contains(node);
        }

        public boolean contains(V value) {
            return nodes.contains(new Node(value));
        }

        public Collection<Node> getConnectedNodes() {
            return nodes;
        }

        public Collection<V> getConnectedValues() {
            Vector<V> values = new Vector<>();
            for(Node node : nodes)
                values.add(node.getValue());
            return values;
        }

        public E getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                value != null ? value : 0,
                nodes);
        }

        @Override
        public boolean equals(Object other) {
            if (other != null && other instanceof Graph.Edge) {
                Graph.Edge otherEdge = (Graph.Edge) other;
                return otherEdge.value.equals(value) && otherEdge.nodes.equals(nodes);
            }
            return false;
        }

        @Override
        public String toString() {
            return "Edge from " + nodes.toString() +
                    " with value " + value.toString();
        }
    }
}

class ValueNotFoundException extends RuntimeException
{
      //Parameterless Constructor
      public ValueNotFoundException() {}

      //Constructor that accepts a message
      public ValueNotFoundException(String message)
      {
         super(message);
      }
 }
