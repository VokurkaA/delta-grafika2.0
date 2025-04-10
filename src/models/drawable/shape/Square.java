package models.drawable.shape;

import models.DrawingParams;
import models.drawable.Point;

import java.awt.*;
import java.util.List;

public class Square extends Rectangle {

    public Square(List<Point> points, Color color, boolean isDashed) {
        super(points, color, isDashed);
    }

    public Square(Point a) {
        super(a);
    }

    @Override
    public void place(Point point, boolean doAlignLine) {

    }

    @Override
    public void move(Point point, DrawingParams drawingParams, boolean newPoint) {

    }

    @Override
    public void rasterize(Graphics g) {

    }

    @Override
    public double getNearestDistance(Point click) {
        return 0;
    }
}
