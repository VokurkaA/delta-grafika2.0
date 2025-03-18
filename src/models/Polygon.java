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
        int n = getPointCount();
        if (n < 2) return;

        Line l = new Line(points.get(0), points.get(1));

        for (int i = 1; i < n; i++) {
            l.setA(l.getB());
            l.setB(points.get(i));
            l.rasterize(g);
        }

        if (isFinished() && n > 2) {
            l.setA(points.get(n - 1));
            l.setB(points.get(0));
            l.rasterize(g);
        }
    }
}