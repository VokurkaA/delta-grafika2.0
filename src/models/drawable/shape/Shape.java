package models.drawable.shape;

import enums.LineType;
import models.DrawingParams;
import models.drawable.Drawable;
import models.drawable.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Shape extends Drawable {
    public LineType lineType;
    public int thickness = 1;
    public boolean isFinished = false;
    protected int movePointIndex = -1;

    public Shape() {
        this.points = new ArrayList<>();
        this.color = Color.RED;
        this.lineType = LineType.solid;
    }

    public abstract void place(Point point, boolean doAlignLine);

    public abstract void move(Point point, DrawingParams drawingParams, boolean newPoint);

    public void moveEntireShape(Point destination) {
        int xSum = 0, ySum = 0;
        for (Point point : points) {
            xSum += point.getX();
            ySum += point.getY();
        }
        Point origin = new Point(xSum / points.size(), ySum / points.size());
        Point moveVector = new Point(destination.getX() - origin.getX(), destination.getY() - origin.getY());

        for (Point point : points) {
            point.set(Point.add(point, moveVector));
        }
    }

    public abstract void rasterize(Graphics g);

    public abstract double getNearestDistance(Point click);

    @Override
    public abstract String toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (this.hashCode() != o.hashCode()) return false;
        Shape shape = (Shape) o;
        return thickness == shape.thickness && isFinished == shape.isFinished && movePointIndex == shape.movePointIndex && lineType == shape.lineType && color.equals(shape.color) && points.equals(shape.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineType, thickness, isFinished, movePointIndex, color, points);
    }
}