package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon implements Shape {
    private static final int finishDistanceThreshold = 10;
    private final List<Point> points = new ArrayList<>();
    private int movePointIndex = -1;
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
    public boolean isDashed() {
        return isDashed;
    }

    @Override
    public void setISDashed(boolean isDashed) {
        this.isDashed = isDashed;
    }

    @Override
    public void place(Point point, boolean doAlignLine) {
        if (points.size() >= 3 && Math.abs(point.getX() - points.getFirst().getX()) < finishDistanceThreshold && Math.abs(point.getY() - points.getFirst().getY()) < finishDistanceThreshold) {
            points.getLast().set(points.getFirst());
            setIsFinished(true);
        } else {
            if (doAlignLine) {
                Point lastFixedPoint = points.get(points.size() - 2);
                points.getLast().set(Line.alignPoint(lastFixedPoint, point));
            } else {
                points.getLast().set(point);
            }
            points.add(new Point(point.getX(), point.getY()));
        }
    }

    @Override
    public void move(Point click, DrawingParams drawingParams, boolean newPoint) {
        isDashed = drawingParams.dashedLine;
        Point nearestPoint = null;
        if (movePointIndex >= 0 && movePointIndex < points.size() && Point.getDistance(click, points.get(movePointIndex)) <= finishDistanceThreshold) {
            nearestPoint = points.get(movePointIndex);
        } else {
            if (!points.isEmpty()) {
                nearestPoint = points.getFirst();
                double minDistance = Point.getDistance(nearestPoint, click);

                for (Point p : points) {
                    double distance = Point.getDistance(p, click);
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestPoint = p;
                    }
                }
                if (nearestPoint != null) {
                    movePointIndex = points.indexOf(nearestPoint);
                }
            }
        }

        if (nearestPoint == null) {
            return;
        }

        Point targetPoint;
        if (drawingParams.doAlignLine) {
            int nearestIndex = points.indexOf(nearestPoint);

            Point referencePoint;
            if (nearestIndex == 0) referencePoint = points.get(1);
            else if (nearestIndex == points.size() - 1) referencePoint = points.get(points.size() - 2);
            else referencePoint = points.get(nearestIndex - 1);

            targetPoint = Line.alignPoint(referencePoint, click);
        } else {
            targetPoint = click;
        }

        nearestPoint.set(targetPoint);

        if (isFinished) {
            int nearestIndex = points.indexOf(nearestPoint);
            if (nearestIndex == 0) {
                points.getLast().set(targetPoint);
            } else if (nearestIndex == points.size() - 1) {
                points.getFirst().set(targetPoint);
            }
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

    @Override
    public double getNearestDistance(Point click) {
        if (points.isEmpty()) return Double.MAX_VALUE;

        double minDistance = Point.getDistance(click, points.getFirst());
        for (Point p : points) {
            double distance = Point.getDistance(click, p);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }
}