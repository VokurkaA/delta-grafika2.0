package models;

import rasterizers.DashedCircleRasterizer;
import rasterizers.SimpleCircleRasterizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Circle implements Shape {
    private final List<Point> points = new ArrayList<>();
    private final Color color;
    private boolean isDashed;
    private boolean isFinished = false;

    public Circle(Point a, Point b, Color color, boolean isDashed) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.isDashed = isDashed;
    }


    public Circle(Point a, boolean isDashed) {
        this(a, new Point(a.getX(), a.getY()), Color.red, isDashed);
    }

    @Override
    public void moveShape(Point point, DrawingParams drawingParams) {
        isDashed = drawingParams.dashedLine;

        Point centerPoint = points.getFirst();
        Point radiusPoint = points.get(1);

        double centerDistanceSq = Point.getDistanceSquared(centerPoint, point);
        double currentRadiusSq = Point.getDistanceSquared(centerPoint, radiusPoint);

        if (centerDistanceSq <= currentRadiusSq) {
            int dX = point.getX() - centerPoint.getX();
            int dY = point.getY() - centerPoint.getY();

            centerPoint.setX(centerPoint.getX() + dX);
            centerPoint.setY(centerPoint.getY() + dY);

            radiusPoint.setX(radiusPoint.getX() + dX);
            radiusPoint.setY(radiusPoint.getY() + dY);
        } else {
            radiusPoint.set(point);
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
    public void place(Point point, boolean doAlignLine) {
        this.points.getLast().set(point);
        setIsFinished(true);
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
    public void rasterize(Graphics g) {
        if (isDashed) DashedCircleRasterizer.rasterize(g, this, color);
        else SimpleCircleRasterizer.rasterize(g, this, color);
    }

    @Override
    public double getNearestDistance(Point click) {
        double radius = getRadius();
        double d = Point.getDistance(getCenter(), click);

//        return (d <= radius) ? d : (d - radius);
        return (d <= radius / 2) ? d : Math.abs((d - radius));
    }


    public Point getCenter() {
        return points.getFirst();
    }

    protected double getRadius() {
        return Point.getDistance(points.get(1), points.getFirst());
    }
}
