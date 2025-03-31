package models;

import enums.DrawingShape;

import java.awt.*;
import java.util.List;

public interface Shape extends Drawable {
    static Shape getShapeByEnum(DrawingShape drawingShape, Point startPoint, boolean isDashed) {
        switch (drawingShape) {
            case line -> {
                return new Line(startPoint, isDashed);
            }
            case polygon -> {
                return new Polygon(startPoint, isDashed);
            }
            case circle -> {
                return new Circle(startPoint, isDashed);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    List<Point> points();

    boolean isFinished();

    void setIsFinished(boolean finished);

    boolean isDashed();

    void setISDashed(boolean isDashed);

    void place(Point point, boolean doAlignLine);

    void moveShape(Point point, DrawingParams drawingParams);

    void rasterize(Graphics g);

    double getNearestDistance(Point click);
}