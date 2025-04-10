package models.drawable.shape;

import models.DrawingParams;
import models.drawable.Drawable;
import models.drawable.Point;

import java.awt.*;

public abstract class Shape extends Drawable {
    public boolean isDashed = false;
    public boolean isFinished = false;
    protected int movePointIndex = -1;

    public abstract void place(models.drawable.Point point, boolean doAlignLine);

    public abstract void move(models.drawable.Point point, DrawingParams drawingParams, boolean newPoint);

    public abstract void rasterize(Graphics g);

    public abstract double getNearestDistance(Point click);
}