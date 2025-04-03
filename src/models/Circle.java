package models;

import rasterizers.DashedCircleRasterizer;
import rasterizers.SimpleCircleRasterizer;

import java.awt.*;

public class Circle extends Shape {
    public Circle(Point a, Point b, Color color, boolean isDashed) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.isDashed = isDashed;
    }

    public Circle(Point a) {
        this(a, new Point(a.getX(), a.getY()), Color.red, false);
    }

    @Override
    public void move(Point click, DrawingParams drawingParams, boolean newPoint) {
        isDashed = drawingParams.dashedLine;

        Point centerPoint = points.getFirst();
        Point radiusPoint = points.get(1);

        double radius = getRadius();

        Point movePoint;
        if (!newPoint && movePointIndex >= 0 && movePointIndex < points.size()) {
            movePoint = points.get(movePointIndex);
        } else {
            double distanceToCenter = Point.getDistance(centerPoint, click);
            double distanceToRadius = Point.getDistance(radiusPoint, click);

            if (distanceToCenter < distanceToRadius && distanceToCenter < radius / 2) movePoint = centerPoint;
            else movePoint = radiusPoint;
            movePointIndex = points.indexOf(movePoint);
        }

        if (movePoint == centerPoint) {
            Point d = Point.subtract(click, centerPoint);
            movePoint.set(click);
            radiusPoint.set(Point.add(radiusPoint, d));
        } else {
            movePoint.set(click);
        }
    }

    @Override
    public void place(Point point, boolean doAlignLine) {
        points.getLast().set(point);
        isFinished = true;
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

        return (d <= radius / 2) ? d : Math.abs((d - radius));
    }


    public Point getCenter() {
        return points.getFirst();
    }

    protected double getRadius() {
        return Point.getDistance(points.get(1), points.getFirst());
    }
}
