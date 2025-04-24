package models;

import enums.DrawingShape;
import enums.DrawingTool;
import enums.LineType;
import models.drawable.shape.Shape;
import models.factory.ToolFactory;
import models.tools.Tool;

import java.awt.*;

public class DrawingParams {
    public boolean doAlignLine;
    public LineType lineType;
    public DrawingShape drawingShape;
    public Shape movingShape;
    public int lineWidth;
    public Color drawingColor;
    public Tool drawingTool;


    public DrawingParams(boolean doAlignLine, LineType lineType, DrawingShape drawingShape, int lineWidth, Color drawingColor, DrawingTool drawingTool) {
        this.doAlignLine = doAlignLine;
        this.lineType = lineType;
        this.drawingShape = drawingShape;
        this.movingShape = null;
        this.lineWidth = lineWidth;
        this.drawingColor = drawingColor;
        this.drawingTool = ToolFactory.getToolByEnum(drawingTool);
    }
}