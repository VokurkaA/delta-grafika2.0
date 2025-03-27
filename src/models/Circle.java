package models;

import rasterizers.DashedCircleRasterizer;
import rasterizers.SimpleCircleRasterizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Circle implements Shape {
    private final List<Point> points = new ArrayList<>();
    private final Color color;
    private final double radius;
    private boolean isDashed;
    private boolean isFinished = false;

    public Circle(Point a, Point b, Color color, boolean isDashed) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.radius = getRadius();
        this.isDashed = isDashed;
    }


    public Circle(Point a, boolean isDashed) {
        this(a, new Point(a.getX(), a.getY()), Color.red, isDashed);
    }

    @Override
    public void rightClickAction(Point newPoint, DrawingParams drawingParams) {
        if (!points.contains(drawingParams.movingParams.movingPoint)) return;

        isDashed = drawingParams.dashedLine;
        if (drawingParams.movingParams.movingPoint.equals(points.getFirst())) {
            int dX = newPoint.getX() - points.getFirst().getX();
            int dY = newPoint.getY() - points.getFirst().getY();

            points.getFirst().set(newPoint);

            Point radiusPoint = points.get(1);
            radiusPoint.setX(radiusPoint.getX() + dX);
            radiusPoint.setY(radiusPoint.getY() + dY);
        } else {
            points.set(1, newPoint);
        }
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
    public void leftClickAction(Point point, boolean alignLine) {
        this.points.getLast().set(point);
        setIsFinished(true);
    }

    @Override
    public boolean idDashed() {
        return isDashed;
    }

    @Override
    public void setISDashed(boolean isDashed) {
        this.isDashed = isDashed;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void rasterize(Graphics g) {
        if (isDashed) DashedCircleRasterizer.rasterize(g, this, color);
        else SimpleCircleRasterizer.rasterize(g, this, color);
    }

    @Override
    public Point getNearestPoint(Point point) {
        double d = Point.getDistance(getCenter(), point);
        if (d < radius / 2) return getCenter();

        double dx = point.getX() - getCenter().getX();
        double dy = point.getY() - getCenter().getY();

        points.get(1).setX((int) (getCenter().getX() + radius * (dx / d)));
        points.get(1).setY((int) (getCenter().getY() + radius * (dy / d)));
        return points.get(1);
    }

    public Point getCenter() {
        return points.getFirst();
    }

    protected double getRadius() {
        return Point.getDistance(points.get(1), points.getFirst());
    }
}
