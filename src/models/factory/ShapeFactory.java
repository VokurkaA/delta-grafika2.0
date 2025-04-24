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
        shapeMap.put(DrawingShape.line, Line::new);
        shapeMap.put(DrawingShape.polygon, Polygon::new);
        shapeMap.put(DrawingShape.circle, Circle::new);
        shapeMap.put(DrawingShape.rectangle, Rectangle::new);
        shapeMap.put(DrawingShape.square, Square::new);
    }

    public static Shape getShapeByEnum(DrawingShape drawingShape, Point startPoint, DrawingParams params) {
        ShapeCreator creator = shapeMap.get(drawingShape);
        return (creator != null) ? creator.create(startPoint, params) : null;
    }

    private interface ShapeCreator {
        Shape create(Point startPoint, DrawingParams params);
    }
}