package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon implements Shape {
    private static final int finishDistanceThreshold = 10;
    private final List<Point> points = new ArrayList<>();
    private Color color;
    private boolean isDashed;
    private boolean isFinished = false;

    public Polygon(List<Point> points, Color color, boolean isDashed) {
        this.color = color;
        this.points.addAll(points);
        this.isDashed = isDashed;
    }

    public Polygon(Point a, boolean isDashed) {
        this.points.add(a);
        this.points.add(new Point(a.getX(), a.getY()));
        this.color = Color.red;
        this.isDashed = isDashed;
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
        return isFinished;
    }

    @Override
    public void setIsFinished(boolean finished) {
        this.isFinished = finished;
    }

    @Override
    public boolean idDashed() {
        return isDashed;
    }

    @Override
    public void setISDashed(boolean isDashed) {
        this.isDashed = isDashed;
    }

    @Override
    public void leftClickAction(Point point, boolean alignLine) {
        if (points.size() >= 3 && Math.abs(point.getX() - points.getFirst().getX()) < finishDistanceThreshold && Math.abs(point.getY() - points.getFirst().getY()) < finishDistanceThreshold) {
            points.getLast().set(points.getFirst());
            setIsFinished(true);
        } else {
            if (alignLine) {
                Point lastFixedPoint = points.get(points.size() - 2);
                points.getLast().set(Line.alignPoint(lastFixedPoint, point));
            } else {
                points.getLast().set(point);
            }
            points.add(new Point(point.getX(), point.getY()));
        }
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

        g.setColor(color);

        for (int i = 0; i < n - 1; i++) {
            Line l = new Line(points.get(i), points.get(i + 1), color, isDashed);
            l.rasterize(g);
        }
    }
}