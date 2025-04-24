package models.factory;

import enums.DrawingTool;
import models.tools.*;

import java.util.EnumMap;

public class ToolFactory {
    private static final EnumMap<DrawingTool, ToolCreator> toolMap = new EnumMap<>(DrawingTool.class);

    static {
        toolMap.put(DrawingTool.pen, PenTool::new);
        toolMap.put(DrawingTool.bucket, BucketTool::new);
        toolMap.put(DrawingTool.shape, ShapeTool::new);
        toolMap.put(DrawingTool.rasterizer, RasterizerTool::new);
        toolMap.put(DrawingTool.moveTool, MoveTool::new);
    }

    public static Tool getToolByEnum(DrawingTool drawingTool) {
        ToolCreator creator = toolMap.get(drawingTool);
        return (creator != null) ? creator.create() : null;
    }

    private interface ToolCreator {
        Tool create();
    }
}
