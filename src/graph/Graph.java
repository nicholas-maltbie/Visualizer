import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

public class Graph<V, E> {
    
    private Set<Node> nodes;
    private Set<Edge> edges;
    
    public Graph() {
        nodes = new HashSet<>();
        edges = new HashSet<>();
    }
    
    private Node getNode(V value) {
        for(Node node : nodes) 
            if(node.getValue().equals(value))
                return node;
        return null;
    }
    
    public boolean addPoint(V point) {
        return nodes.add(new Node(point));
    }
    
    public void addEdge(V source, V target, E edge) {
        Node sourceNode = getNode(source);
        Node targetNode = getNode(target);
        Edge edgeContainer = new Edge(sourceNode, targetNode, edge);
        edges.add(edgeContainer);
        sourceNode.addEdge(edgeContainer);
        targetNode.addEdge(edgeContainer);
    }

    public Set<V> getNodes() {
        Set<V> contained = new HashSet<>();
        for(Node node : nodes)
            contained.add(node.getValue());
        return contained;
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
                connected.add(edge.getSource());
                connected.add(edge.getDest());
            }
            connected.remove(this);
            return connected;
        }
        
        public boolean containsEdge(Edge edge) {
            return edges.contains(edge);
        }
        
        public boolean isConnected(Node other) {
            for(Edge edge : edges) {
                if(edge.getSource().equals(other) || edge.getDest().equals(other))
                    return true;
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
                    ((Graph.Node) other).value.equals(value);
        }
        
        @Override
        public int hashCode() {
            return value.hashCode();
        }
        
        @Override
        public String toString() {
            return "Node " + value.toString() + "  with " + 
                    Integer.toString(edges.size()) + " edges";
        }
    }
    
    private class Edge {
        private E value;
        private Node source;
        private Node dest;
        
        public Edge(Node source, Node dest, E value) {
            this.value = value;
            this.source = source;
            this.value = value;
        }
    
        public Node getSource() {
            return source;
        }
        
        public Node getDest() {
            return dest;
        }
        
        public E getValue() {
            return value;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(
                value != null ? value : 0,
                source != null ? source : 0,
                dest != null ? dest : 0);
                
        }
        
        @Override
        public boolean equals(Object other) {
            if (other != null && other instanceof Graph.Edge) {
                Graph.Edge otherEdge = (Graph.Edge) other;
                return otherEdge.value.equals(value) && otherEdge.source.equals(source) &&
                        otherEdge.dest.equals(dest);
            }
            return false;
        }
        
        @Override
        public String toString() {
            return "Edge from " + source.getValue().toString() + " to " + dest.getValue().toString() + 
                    " with value " + value.toString();
        }
    }
}

