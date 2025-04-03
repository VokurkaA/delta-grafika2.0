package models;

import rasterizers.DashedLineRasterizer;
import rasterizers.SimpleLineRasterizer;

import java.awt.*;

public class Line extends Shape {
    public Line(Point a, Point b, Color color, boolean isDashed) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.isDashed = isDashed;
    }

    public Line(Point a) {
        this(a, new Point(a.getX(), a.getY()), Color.red, false);
    }

    public static Point alignPoint(Point a, Point b) {
        float d = (float) Math.hypot(b.getX() - a.getX(), b.getY() - a.getY());
        float angle = (float) Math.toDegrees(Math.atan2(b.getY() - a.getY(), b.getX() - a.getX()));

        float alignedAngle = ((Math.round(angle / 45) * 45) + 360) % 360;

        return new Point((int) (a.getX() + d * Math.cos(Math.toRadians(alignedAngle))), (int) (a.getY() + d * Math.sin(Math.toRadians(alignedAngle))));
    }

    public Point getA() {
        return points.getFirst();
    }

    public void setA(Point point) {
        points.set(0, point);
    }

    public Point getB() {
        return points.get(1);
    }

    public void setB(Point point) {
        points.set(1, point);
    }

    @Override
    public void place(Point point, boolean doAlignLine) {
        if (doAlignLine) setB(Line.alignPoint(getA(), point));
        else setB(point);
        isFinished = true;
    }

    @Override
    public void move(Point click, DrawingParams drawingParams, boolean newPoint) {
        isDashed = drawingParams.dashedLine;

        Point nearestPoint = (Point.getDistance(getA(), click) <= Point.getDistance(getB(), click)) ? getA() : getB();

        Point targetPoint = click;
        if (drawingParams.doAlignLine) {
            targetPoint = Line.alignPoint(nearestPoint == getA() ? getB() : getA(), click);
        }

        nearestPoint.set(targetPoint);
    }

    @Override
    public void rasterize(Graphics g) {
        if (isDashed) DashedLineRasterizer.rasterize(g, this, color);
        else SimpleLineRasterizer.rasterize(g, this, color);
    }

    @Override
    public double getNearestDistance(Point click) {
        return Math.min(Point.getDistance(getA(), click), Point.getDistance(getB(), click));
    }
}
