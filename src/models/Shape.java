package models;

import enums.DrawingShape;

import java.awt.*;

public abstract class Shape extends Drawable {
    public boolean isDashed = false;
    public boolean isFinished = false;
    protected int movePointIndex = -1;

    public static Shape getShapeByEnum(DrawingShape drawingShape, Point startPoint) {
        switch (drawingShape) {
            case line -> {
                return new Line(startPoint);
            }
            case polygon -> {
                return new Polygon(startPoint);
            }
            case circle -> {
                return new Circle(startPoint);
            }
            default -> {
                return null;
            }
        }
    }

    public abstract void place(Point point, boolean doAlignLine);

    public abstract void move(Point point, DrawingParams drawingParams, boolean newPoint);

    abstract void rasterize(Graphics g);

    abstract double getNearestDistance(Point click);
}