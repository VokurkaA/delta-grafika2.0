package models.tools;

import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;
import models.drawable.shape.Shape;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class RasterizerTool implements Tool {
    @Override
    public void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        Point clickPoint = new Point(e.getX(), e.getY());
        Shape shapeToRasterize = canvas.getNearestShape(clickPoint);

        if (shapeToRasterize != null) {
            Graphics2D g2d = canvas.getFillLayer().createGraphics();
            shapeToRasterize.rasterize(g2d);
            g2d.dispose();

            canvas.removeShape(shapeToRasterize);
            canvas.repaint();
        }
    }

    @Override
    public void onMouseMove(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {

    }

    @Override
    public void onMouseDrag(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {

    }

    @Override
    public void onKeyPress(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {

    }

    @Override
    public void onKeyRelease(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {

    }
}
