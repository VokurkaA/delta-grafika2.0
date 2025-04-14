package models.drawable.shape;

import enums.LineType;
import models.DrawingParams;
import models.drawable.Point;
import rasterizers.DashedLineRasterizer;
import rasterizers.DottedLineRasterizer;
import rasterizers.Rasterizer;
import rasterizers.SimpleLineRasterizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon extends Shape {
    private static final int finishDistanceThreshold = 10;

    public Polygon(List<Point> points, Color color, LineType lineType) {
        this.points = points;
        this.color = color;
        this.lineType = lineType;
    }

    public Polygon(Point a, DrawingParams drawingParams) {
        this(new ArrayList<>(Arrays.asList(a, new Point(a.getX(), a.getY()))), drawingParams.drawingColor, drawingParams.lineType);
    }

    @Override
    public void place(Point point, boolean doAlignLine) {
        if (points.size() >= 3 && Math.abs(point.getX() - points.getFirst().getX()) < finishDistanceThreshold && Math.abs(point.getY() - points.getFirst().getY()) < finishDistanceThreshold) {
            points.removeLast();
            isFinished = true;
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
        lineType = drawingParams.lineType;

        if (newPoint) {
            if (points.isEmpty()) return;
            movePointIndex = points.indexOf(getNearestPoint(click));
        } else {
            if (movePointIndex < 0 || movePointIndex > points.size() - 1) return;

            Point movePoint = points.get(movePointIndex);
            Point targetPoint;
            if (drawingParams.doAlignLine) {
                Point referencePoint;
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
        Rasterizer rasterizer = new SimpleLineRasterizer();
        switch (lineType) {
            case dashed -> rasterizer = new DashedLineRasterizer();
            case dotted -> rasterizer = new DottedLineRasterizer();
        }

        for (int i = 0; i < n - 1; i++) {
            Line l = new Line(points.get(i), points.get(i + 1), color, lineType);
            rasterizer.rasterize(g, l);
            l.rasterize(g);
        }
        if (isFinished) new Line(points.getLast(), points.getFirst(), color, lineType).rasterize(g);
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

    protected Point getNearestPoint(Point click) {
        Point nearestPoint = points.getFirst();
        double minDistance = Point.getDistance(nearestPoint, click);

        movePointIndex = points.indexOf(nearestPoint);
        for (Point p : points) {
            double distance = Point.getDistance(p, click);
            if (distance < minDistance) {
                minDistance = distance;
                nearestPoint = p;
            }
        }
        return nearestPoint;
    }

    @Override
    public String toString() {
        return "Polygon";
    }
}