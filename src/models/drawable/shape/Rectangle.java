package models.drawable.shape;

import models.DrawingParams;
import models.drawable.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Rectangle extends Polygon {

    public Rectangle(List<Point> points, Color color, boolean isDashed) {
        super(points, color, isDashed);
    }

    public Rectangle(Point a) {
        this(new ArrayList<>(Arrays.asList(a, new Point(a.getX(), a.getY()), new Point(a.getX(), a.getY()), new Point(a.getX(), a.getY()))), Color.red, false);
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
}
