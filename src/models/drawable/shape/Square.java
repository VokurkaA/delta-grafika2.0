package models.drawable.shape;

import enums.LineType;
import models.DrawingParams;
import models.drawable.Point;

import java.awt.*;
import java.util.List;

public class Square extends Rectangle {

    public Square(List<Point> points, Color color, LineType lineType) {
        super(points, color, lineType);
        ensureEqualSides();
    }

    public Square(Point a) {
        super(a);
        ensureEqualSides();
    }

    private void ensureEqualSides() {
        int sideLengthX = Math.abs(points.get(0).getX() - points.get(1).getX());
        int sideLengthY = Math.abs(points.get(0).getY() - points.get(3).getY());

        int sideLength = Math.max(sideLengthX, sideLengthY);

        Point first = points.get(0);
        points.set(1, new Point(first.getX() + sideLength, first.getY()));
        points.set(2, new Point(first.getX() + sideLength, first.getY() + sideLength));
        points.set(3, new Point(first.getX(), first.getY() + sideLength));
    }

    @Override
    public void place(Point point, boolean doAlignLine) {
        super.place(point, doAlignLine);
        ensureEqualSides();
    }

    @Override
    public void move(Point point, DrawingParams drawingParams, boolean newPoint) {
        super.move(point, drawingParams, newPoint);
        ensureEqualSides();
    }

    @Override
    public String toString() {
        return "Square";
    }
}
