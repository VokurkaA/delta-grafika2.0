package models.drawable.shape;

import models.DrawingParams;
import models.drawable.Point;
import rasterizers.DashedLineRasterizer;
import rasterizers.SimpleLineRasterizer;

import java.awt.*;

public class Line extends Shape {
    public Line(models.drawable.Point a, models.drawable.Point b, Color color, boolean isDashed) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.isDashed = isDashed;
    }

    public Line(models.drawable.Point a) {
        this(a, new models.drawable.Point(a.getX(), a.getY()), Color.red, false);
    }

    public static models.drawable.Point alignPoint(models.drawable.Point a, models.drawable.Point b) {
        float d = (float) Math.hypot(b.getX() - a.getX(), b.getY() - a.getY());
        float angle = (float) Math.toDegrees(Math.atan2(b.getY() - a.getY(), b.getX() - a.getX()));

        float alignedAngle = ((Math.round(angle / 45) * 45) + 360) % 360;

        return new models.drawable.Point((int) (a.getX() + d * Math.cos(Math.toRadians(alignedAngle))), (int) (a.getY() + d * Math.sin(Math.toRadians(alignedAngle))));
    }

    public models.drawable.Point getA() {
        return points.getFirst();
    }

    public void setA(models.drawable.Point point) {
        points.set(0, point);
    }

    public models.drawable.Point getB() {
        return points.get(1);
    }

    public void setB(models.drawable.Point point) {
        points.set(1, point);
    }

    @Override
    public void place(models.drawable.Point point, boolean doAlignLine) {
        if (doAlignLine) setB(Line.alignPoint(getA(), point));
        else setB(point);
        isFinished = true;
    }

    @Override
    public void move(models.drawable.Point click, DrawingParams drawingParams, boolean newPoint) {
        isDashed = drawingParams.dashedLine;

        models.drawable.Point nearestPoint = (models.drawable.Point.getDistance(getA(), click) <= models.drawable.Point.getDistance(getB(), click)) ? getA() : getB();

        models.drawable.Point targetPoint = click;
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
    public double getNearestDistance(models.drawable.Point click) {
        return Math.min(models.drawable.Point.getDistance(getA(), click), Point.getDistance(getB(), click));
    }
}
