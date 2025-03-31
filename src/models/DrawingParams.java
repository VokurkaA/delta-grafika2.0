package models;

import enums.DrawingShape;

public class DrawingParams {
    public boolean doAlignLine;
    public boolean dashedLine;
    public DrawingShape drawingShape;
    public Shape movingShape;

    public DrawingParams(boolean doAlignLine, boolean dashedLine, DrawingShape drawingShape) {
        this.doAlignLine = doAlignLine;
        this.dashedLine = dashedLine;
        this.drawingShape = drawingShape;
        this.movingShape = null;
    }
}