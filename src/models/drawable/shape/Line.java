package models.drawable.shape;

import enums.LineType;
import models.DrawingParams;
import models.drawable.Point;
import rasterizers.DashedLineRasterizer;
import rasterizers.DottedLineRasterizer;
import rasterizers.SimpleLineRasterizer;

import java.awt.*;

public class Line extends Shape {
    public Line(Point a, Point b, Color color, LineType lineType) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.lineType = lineType;
    }

    public Line(Point a) {
        this(a, new Point(a.getX(), a.getY()), Color.red, LineType.solid);
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
        lineType = drawingParams.lineType;

        Point nearestPoint = (Point.getDistance(getA(), click) <= Point.getDistance(getB(), click)) ? getA() : getB();

        Point targetPoint = click;
        if (drawingParams.doAlignLine) {
            targetPoint = Line.alignPoint(nearestPoint == getA() ? getB() : getA(), click);
        }

        nearestPoint.set(targetPoint);
    }

    @Override
    public void rasterize(Graphics g) {
        System.out.print(lineType);
        switch (lineType) {
            case solid -> SimpleLineRasterizer.rasterize(g, this, color);
            case dashed -> DashedLineRasterizer.rasterize(g, this, color);
            case dotted -> DottedLineRasterizer.rasterize(g, this, color);
        }
    }

    @Override
    public double getNearestDistance(Point click) {
        return Math.min(Point.getDistance(getA(), click), Point.getDistance(getB(), click));
    }

    @Override
    public String toString() {
        return "Line";
    }
}
