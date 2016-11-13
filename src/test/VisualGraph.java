import java.util.Map;
import java.util.HashMap;
import java.awt.Point;
import java.util.Set;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class VisualGraph {

    private Graph<String, Integer> graph;
    private Map<String, Point> positions;

    public static void main(String[] args) {
        Graph<String, Integer> graph = new Graph<>();
        
        for(int i = (int)'A'; i <= (int)'Z'; i++) {
            graph.addPoint("" + (char)i);
        }
        
        Set<String> pointSet = graph.getPoints();
        String[] points = pointSet.toArray(new String[pointSet.size()]);
        System.out.println(pointSet);
        
        Random rng = new Random();
        
        for(int i = 0; i < 50; i++) {
            String start = points[rng.nextInt(points.length)];
            List<String> endpts = new ArrayList<>(pointSet);
            endpts.remove(start);
            endpts.removeAll(graph.getNeighbors(start));
            if(endpts.size() == 0)
                continue;
            String end = endpts.get(rng.nextInt(endpts.size()));
            graph.addEdge(start, end, rng.nextInt(50) + 50);
        }
        
        System.out.println(graph);
        
        for(String point : pointSet) {
            System.out.println(point + " connected to: ");
            Set<String> connected = graph.getNeighbors(point);
            if(connected.isEmpty())
                System.out.println(" Not connected ot anything :(");
            else {
                for(String conn : connected) {
                    System.out.println(" " + conn + " via " + graph.getEdge(point, conn));
                }
            }
        }
    }

}


