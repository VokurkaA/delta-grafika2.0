package models.drawable.shape;

import enums.LineType;
import models.DrawingParams;
import models.drawable.Point;

import java.awt.*;
import java.util.List;

public class Square extends Rectangle {

    public Square(List<Point> points, Color color, LineType lineType, int thickness) {
        super(points, color, lineType, thickness);
        ensureEqualSides();
    }

    public Square(Point a, DrawingParams drawingParams) {
        super(a, drawingParams);
        ensureEqualSides();
    }

    public Square() {
        super();
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
        lineType = drawingParams.lineType;
        color = drawingParams.drawingColor;
        thickness = drawingParams.lineWidth;

        if (!isFinished) {
            Point first = points.getFirst();
            int maxDelta = Math.max(Math.abs(point.getX() - first.getX()), Math.abs(point.getY() - first.getY()));
            int signX = point.getX() >= first.getX() ? 1 : -1;
            int signY = point.getY() >= first.getY() ? 1 : -1;

            points.set(1, new Point(first.getX() + maxDelta * signX, first.getY()));
            points.set(2, new Point(first.getX() + maxDelta * signX, first.getY() + maxDelta * signY));
            points.set(3, new Point(first.getX(), first.getY() + maxDelta * signY));
            return;
        }

        super.move(point, drawingParams, newPoint);
        ensureEqualSides();
    }

    @Override
    public String toString() {
        return "Square";
    }
}
