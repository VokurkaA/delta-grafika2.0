package models.drawable.shape;

import models.DrawingParams;
import models.drawable.Point;
import rasterizers.DashedCircleRasterizer;
import rasterizers.SimpleCircleRasterizer;

import java.awt.*;

public class Circle extends Shape {
    public Circle(models.drawable.Point a, models.drawable.Point b, Color color, boolean isDashed) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.isDashed = isDashed;
    }

    public Circle(models.drawable.Point a) {
        this(a, new models.drawable.Point(a.getX(), a.getY()), Color.red, false);
    }

    @Override
    public void move(models.drawable.Point click, DrawingParams drawingParams, boolean newPoint) {
        isDashed = drawingParams.dashedLine;

        models.drawable.Point centerPoint = points.getFirst();
        models.drawable.Point radiusPoint = points.get(1);

        double radius = getRadius();

        models.drawable.Point movePoint;
        if (!newPoint && movePointIndex >= 0 && movePointIndex < points.size()) {
            movePoint = points.get(movePointIndex);
        } else {
            double distanceToCenter = models.drawable.Point.getDistance(centerPoint, click);
            double distanceToRadius = models.drawable.Point.getDistance(radiusPoint, click);

            if (distanceToCenter < distanceToRadius && distanceToCenter < radius / 2) movePoint = centerPoint;
            else movePoint = radiusPoint;
            movePointIndex = points.indexOf(movePoint);
        }

        if (movePoint == centerPoint) {
            models.drawable.Point d = models.drawable.Point.subtract(click, centerPoint);
            movePoint.set(click);
            radiusPoint.set(models.drawable.Point.add(radiusPoint, d));
        } else {
            movePoint.set(click);
        }
    }

    @Override
    public void place(models.drawable.Point point, boolean doAlignLine) {
        points.getLast().set(point);
        isFinished = true;
    }

    @Override
    public void rasterize(Graphics g) {
        if (isDashed) DashedCircleRasterizer.rasterize(g, this, color);
        else SimpleCircleRasterizer.rasterize(g, this, color);
    }

    @Override
    public double getNearestDistance(models.drawable.Point click) {
        double radius = getRadius();
        double d = models.drawable.Point.getDistance(getCenter(), click);

        return (d <= radius / 2) ? d : Math.abs((d - radius));
    }


    public models.drawable.Point getCenter() {
        return points.getFirst();
    }

    protected double getRadius() {
        return Point.getDistance(points.get(1), points.getFirst());
    }
}
