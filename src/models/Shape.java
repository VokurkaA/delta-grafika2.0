package models;

import enums.DrawingShape;

import java.awt.*;
import java.util.List;

public interface Shape {
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

    List<Point> points();

    boolean isFinished();

    void setIsFinished(boolean finished);

    boolean idDashed();

    void setISDashed(boolean isDashed);

    void leftClickAction(Point point, boolean alignLine);

    void rasterize(Graphics g);
}