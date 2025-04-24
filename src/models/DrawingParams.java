package models;

import enums.DrawingShape;
import enums.DrawingTool;
import enums.LineType;
import models.drawable.shape.Shape;
import models.factory.ToolFactory;
import models.tools.Tool;
import utils.SettingsManager;

import java.awt.*;

public class DrawingParams {
    public boolean doAlignLine;
    public LineType lineType;
    public DrawingShape drawingShape;
    public Shape movingShape;
    public int lineWidth;
    public Color drawingColor;
    public Tool drawingTool;


    public DrawingParams() {
        this.doAlignLine = false;
        this.lineType = LineType.valueOf(SettingsManager.getString("drawing.defaultLineType", "solid"));
        this.drawingShape = DrawingShape.valueOf(SettingsManager.getString("drawing.defaultShape", "polygon"));
        this.movingShape = null;
        this.lineWidth = SettingsManager.getInt("drawing.defaultLineWidth", 1);
        this.drawingColor = SettingsManager.getColor("drawing.defaultColor", Color.RED);
        this.drawingTool = ToolFactory.getToolByEnum(DrawingTool.valueOf(SettingsManager.getString("drawing.defaultTool", "pen")));
    }
}