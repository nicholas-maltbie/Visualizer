import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.awt.Point;
import java.util.Set;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import java.awt.Color;
import java.util.Random;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

abstract public class VisualGraph extends Visualizer {

    private Graph<ColoredPoint, ColoredEdge> graph;
    public static final Color BACKGROUND = Color.BLACK, DEFAULT_POINT = Color.BLUE,
            DEFAULT_EDGE = Color.WHITE, DEFAULT_TEXT = Color.WHITE;
    public static final int CIRCLE_DIAMETER = 50;

    public static final Random rng = new Random();

    public VisualGraph() {
        Collection<String> pointNames = new ArrayList<>();
        for(int i = (int)'A'; i < (int)'A' + 10; i++) {
            pointNames.add("" + (char)i);
        }
        
        graph = new Graph<>();
        
        for(String name : pointNames) {
            Point pos = new Point(CIRCLE_DIAMETER / 2 + rng.nextInt(Visualizer.DRAW_WIDTH - CIRCLE_DIAMETER), 
                    CIRCLE_DIAMETER + rng.nextInt(Visualizer.DRAW_HEIGHT - CIRCLE_DIAMETER * 2));
            ColoredPoint point = new ColoredPoint(DEFAULT_POINT, DEFAULT_TEXT, pos, name);
            graph.addPoint(point);
        }
        
        Set<ColoredPoint> pointSet = graph.getPoints();
        ColoredPoint[] points = pointSet.toArray(new ColoredPoint[pointSet.size()]);
        
        for(int i = 0; i < 15; i++) {
            ColoredPoint start = points[rng.nextInt(points.length)];
            List<ColoredPoint> endpts = new ArrayList<>(pointSet);
            endpts.remove(start);
            endpts.removeAll(graph.getNeighbors(start));
            if(endpts.size() == 0)
                continue;
            ColoredPoint end = endpts.get(rng.nextInt(endpts.size()));
            graph.addEdge(start, end, new ColoredEdge(DEFAULT_EDGE, rng.nextInt(50) + 50));
        }
    }

    public void paintVisualization(Graphics2D g2d) {
        // Draw Background
        g2d.setColor(BACKGROUND);
        g2d.fillRect(0, 0, Visualizer.DRAW_WIDTH, Visualizer.DRAW_HEIGHT);
        
        FontMetrics metrics = g2d.getFontMetrics();
        
        g2d.setStroke(new BasicStroke(5));
        Set<ColoredPoint> marked = new HashSet<>();
        g2d.setFont(new Font("Helvetica", Font.BOLD, 30));
        for(ColoredPoint point : graph.getPoints()) {
            for(ColoredPoint neighbor : graph.getNeighbors(point)) {
                if(!marked.contains(neighbor)) {
                    Line2D edge = new Line2D.Float(point.getPosition().x, point.getPosition().y, 
                            neighbor.getPosition().x, neighbor.getPosition().y);
                    g2d.setColor(graph.getEdge(point, neighbor).getColor());
                    g2d.draw(edge);
                }
            }
            Ellipse2D circle = new Ellipse2D.Float(point.getPosition().x - CIRCLE_DIAMETER / 2,
                    point.getPosition().y - CIRCLE_DIAMETER / 2, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
            g2d.setColor(point.getFill());
            g2d.fill(circle);
            g2d.setColor(point.getTextColor());
            Rectangle2D stringBounds = metrics.getStringBounds(point.getName(), g2d);
            g2d.drawString(point.getName(), point.getPosition().x - (int)stringBounds.getWidth() / 2,
                    point.getPosition().y + (int) stringBounds.getHeight() / 2);
            
            marked.add(point);
        }
    }
    
    public int getDelay() {
        return 50;
    }
    
    protected class ColoredPoint {
        private Point position;
        private Color fill;
        private Color text;
        private String name;
        
        public ColoredPoint(Color fill, Color text, Point position, String name) {
            this.fill = fill;
            this.text = text;
            this.position = position;
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        public Color getFill() {
            return fill;
        }
        
        public Color getTextColor() {
            return text;
        }
        
        public Point getPosition() {
            return position;
        }
        
        public void setFill(Color fill) {
            this.fill = fill;
        }
        
        public void setTextColor(Color text) {
            this.text = text;
        }
        
        public void setPosition(Point position) {
            this.position = position;
        }
        
        @Override
        public boolean equals(Object other) {
            return other instanceof ColoredPoint && ((ColoredPoint) other).name.equals(name);
        }
        
        @Override
        public int hashCode() {
            if(name == null) {
                return 0;
            }
            return name.hashCode();
        }
    }
    
    protected class ColoredEdge {
        private Color color;
        private Integer weight;
        
        public ColoredEdge(Color color, int weight) {
            this.color = color;
            this.weight = weight;
        }
        
        public int getWeight() {
            return weight;
        }
        
        public Color getColor() {
            return color;
        }
        
        public void setColor(Color color) {
            this.color = color;
        }

        @Override
        public int hashCode() {
            return weight.hashCode();
        }
        
        @Override 
        public boolean equals(Object other) {
            return other instanceof ColoredEdge && ((ColoredEdge) other).weight.equals(weight);
        }
    }

}


