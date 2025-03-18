package models;

import enums.DrawingShape;

import java.awt.*;
import java.util.List;

public interface Shape {
    List<Point> points();

    boolean isFinished();

    void setFinished(boolean finished);

    void rasterize(Graphics g);

    public static Shape getShapeByEnum(DrawingShape drawingShape, Point startPoint){
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
}