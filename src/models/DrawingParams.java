package models;

import enums.DrawingShape;

public class DrawingParams {
    public boolean alignLine;
    public boolean dashedLine;
    public DrawingShape drawingShape;
    public MovingParams movingParams;

    public DrawingParams(boolean alignLine, boolean dashedLine, DrawingShape drawingShape) {
        this.alignLine = alignLine;
        this.dashedLine = dashedLine;
        this.drawingShape = drawingShape;
        this.movingParams = new MovingParams(null, null);
    }
}