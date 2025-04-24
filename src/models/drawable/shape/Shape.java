package models.drawable.shape;

import enums.LineType;
import models.DrawingParams;
import models.drawable.Drawable;
import models.drawable.Point;

import java.awt.*;

public abstract class Shape extends Drawable {
    public LineType lineType;
    public int thickness = 1;
    public boolean isFinished = false;
    protected int movePointIndex = -1;

    public abstract void place(Point point, boolean doAlignLine);

    public abstract void move(Point point, DrawingParams drawingParams, boolean newPoint);

    public void moveEntireShape(Point origin, Point destination) {
        Point moveVector = new Point(destination.getX() - origin.getX(), destination.getY() - origin.getY());
        for (Point point : points) {
            point.set(Point.add(point, moveVector));
        }
    }

    public abstract void rasterize(Graphics g);

    public abstract double getNearestDistance(Point click);

    @Override
    public abstract String toString();
}