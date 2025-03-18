package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon implements Shape {
    private final List<Point> points = new ArrayList<>();
    private Color color;
    private boolean finished = false;

    public Polygon(List<Point> points, Color color) {
        this.color = color;
        this.points.addAll(points);
    }

    public Polygon(Point a) {
        this.points.add(a);
        this.color = Color.red;
    }


    public Polygon() {
        this(new ArrayList<Point>(), Color.red);
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    @Override
    public List<Point> points() {
        return points;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getPointCount() {
        return points.size();
    }

    public boolean isValid() {
        return points.size() >= 3;
    }

    @Override
    public void rasterize(Graphics g) {
        // This would be where you implement custom polygon rasterization
        // For now, we'll rely on the Canvas's paintComponent to draw it
    }
}