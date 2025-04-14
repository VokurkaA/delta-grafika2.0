// FILE: src/models/factory/ShapeFactory.java
package models.factory;

import enums.DrawingShape;
import models.DrawingParams;
import models.drawable.Point;
import models.drawable.shape.*;

import java.util.EnumMap;

public class ShapeFactory {
    private static final EnumMap<DrawingShape, ShapeCreator> shapeMap = new EnumMap<>(DrawingShape.class);

    static {
        shapeMap.put(DrawingShape.line, (p, params) -> new Line(p, params));
        shapeMap.put(DrawingShape.polygon, (p, params) -> new Polygon(p, params));
        shapeMap.put(DrawingShape.circle, (p, params) -> new Circle(p, params));
        shapeMap.put(DrawingShape.rectangle, (p, params) -> new Rectangle(p, params));
        shapeMap.put(DrawingShape.square, (p, params) -> new Square(p, params));
    }

    public static Shape getShapeByEnum(DrawingShape drawingShape, Point startPoint, DrawingParams params) {
        ShapeCreator creator = shapeMap.get(drawingShape);
        return (creator != null) ? creator.create(startPoint, params) : null;
    }

    private interface ShapeCreator {
        Shape create(Point startPoint, DrawingParams params);
    }
}