package models.drawable.shape;

import models.DrawingParams;
import models.drawable.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon extends Shape {
    private static final int finishDistanceThreshold = 10;

    public Polygon(List<models.drawable.Point> points, Color color, boolean isDashed) {
        this.points = points;
        this.color = color;
        this.isDashed = isDashed;
    }

    public Polygon(Point a) {
        this(new ArrayList<>(Arrays.asList(a, new models.drawable.Point(a.getX(), a.getY()))), Color.red, false);
    }

    @Override
    public void place(models.drawable.Point point, boolean doAlignLine) {
        if (points.size() >= 3 && Math.abs(point.getX() - points.getFirst().getX()) < finishDistanceThreshold && Math.abs(point.getY() - points.getFirst().getY()) < finishDistanceThreshold) {
            points.removeLast();
            isFinished = true;
        } else {
            if (doAlignLine) {
                models.drawable.Point lastFixedPoint = points.get(points.size() - 2);
                points.getLast().set(Line.alignPoint(lastFixedPoint, point));
            } else {
                points.getLast().set(point);
            }
            points.add(new models.drawable.Point(point.getX(), point.getY()));
        }
    }

    @Override
    public void move(models.drawable.Point click, DrawingParams drawingParams, boolean newPoint) {
        isDashed = drawingParams.dashedLine;

        if (newPoint) {
            if (points.isEmpty()) return;

            models.drawable.Point nearestPoint = points.getFirst();
            double minDistance = models.drawable.Point.getDistance(nearestPoint, click);

            for (models.drawable.Point p : points) {
                double distance = models.drawable.Point.getDistance(p, click);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPoint = p;
                }
            }
            movePointIndex = points.indexOf(nearestPoint);
        } else {
            if (movePointIndex < 0 || movePointIndex > points.size() - 1) return;

            models.drawable.Point movePoint = points.get(movePointIndex);
            models.drawable.Point targetPoint;
            if (drawingParams.doAlignLine) {
                models.drawable.Point referencePoint;
                if (movePointIndex == 0) referencePoint = points.get(1);
                else if (movePointIndex == points.size() - 1) referencePoint = points.get(points.size() - 2);
                else referencePoint = points.get(movePointIndex - 1);

                targetPoint = Line.alignPoint(referencePoint, click);
            } else {
                targetPoint = click;
            }
            movePoint.set(targetPoint);
        }
    }

    @Override
    public void rasterize(Graphics g) {
        int n = points.size();
        if (n < 2) return;

        g.setColor(color);

        for (int i = 0; i < n - 1; i++) {
            Line l = new Line(points.get(i), points.get(i + 1), color, isDashed);
            l.rasterize(g);
        }
        if (isFinished) new Line(points.getLast(), points.getFirst(), color, isDashed).rasterize(g);
    }

    @Override
    public double getNearestDistance(models.drawable.Point click) {
        if (points.isEmpty()) return Double.MAX_VALUE;

        double minDistance = models.drawable.Point.getDistance(click, points.getFirst());
        for (models.drawable.Point p : points) {
            double distance = Point.getDistance(click, p);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }

        return minDistance;
    }
}