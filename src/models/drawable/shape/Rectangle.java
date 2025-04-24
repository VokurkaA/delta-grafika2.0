package models.drawable.shape;

import enums.LineType;
import models.DrawingParams;
import models.drawable.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rectangle extends Polygon {

    public Rectangle(List<Point> points, Color color, LineType lineType) {
        super(points, color, lineType);
    }

    public Rectangle(Point a, DrawingParams drawingParams) {
        this(new ArrayList<>(Arrays.asList(a, new Point(a.getX(), a.getY()), new Point(a.getX(), a.getY()), new Point(a.getX(), a.getY()))), drawingParams.drawingColor, drawingParams.lineType);
    }

    @Override
    public void place(Point point, boolean doAlignLine) {
        Point first = points.getFirst();

        points.set(1, new Point(point.getX(), first.getY()));
        points.set(2, new Point(point.getX(), point.getY()));
        points.set(3, new Point(first.getX(), point.getY()));

        isFinished = true;
    }


    @Override
    public void move(Point click, DrawingParams drawingParams, boolean newPoint) {
        lineType = drawingParams.lineType;
        color = drawingParams.drawingColor;
        thickness = drawingParams.lineWidth;

        if (!isFinished) {
            Point first = points.getFirst();
            points.set(1, new Point(click.getX(), first.getY()));
            points.set(2, new Point(click.getX(), click.getY()));
            points.set(3, new Point(first.getX(), click.getY()));
            return;
        }

        if (newPoint) {
            movePointIndex = points.indexOf(getNearestPoint(click));
        }
        if (movePointIndex < 0 || movePointIndex >= points.size()) return;

        Point movePoint = points.get(movePointIndex);

        int deltaX = click.getX() - movePoint.getX();
        int deltaY = click.getY() - movePoint.getY();

        movePoint.set(new Point(movePoint.getX() + deltaX, movePoint.getY() + deltaY));

        if (movePointIndex == 0) {
            points.set(1, new Point(points.get(1).getX(), points.get(0).getY()));
            points.set(2, new Point(points.get(1).getX(), points.get(3).getY()));
            points.set(3, new Point(points.get(0).getX(), points.get(3).getY()));
        } else if (movePointIndex == 1) {
            points.set(0, new Point(points.get(0).getX(), points.get(1).getY()));
            points.set(2, new Point(points.get(1).getX(), points.get(3).getY()));
            points.set(3, new Point(points.get(0).getX(), points.get(3).getY()));
        } else if (movePointIndex == 2) {
            points.set(1, new Point(points.get(2).getX(), points.get(0).getY()));
            points.set(0, new Point(points.get(0).getX(), points.get(0).getY()));
            points.set(3, new Point(points.get(0).getX(), points.get(2).getY()));
        } else if (movePointIndex == 3) {
            points.set(0, new Point(points.get(0).getX(), points.get(0).getY()));
            points.set(1, new Point(points.get(2).getX(), points.get(0).getY()));
            points.set(2, new Point(points.get(2).getX(), points.get(3).getY()));
        }
    }


    @Override
    public void rasterize(Graphics g) {
        g.setColor(color);

        for (int i = 0; i < points.size(); i++) {
            int nextIndex = (i + 1) % points.size();
            Line l = new Line(points.get(i), points.get(nextIndex), color, lineType);
            l.rasterize(g);
        }
    }

    @Override
    public double getNearestDistance(Point click) {
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < points.size(); i++) {
            int nextIndex = (i + 1) % points.size();
            Line l = new Line(points.get(i), points.get(nextIndex), color, lineType);
            double distance = l.getNearestDistance(click);
            minDistance = Math.min(minDistance, distance);
        }

        return minDistance;
    }

    @Override
    public String toString() {
        return "Rectangle";
    }
}