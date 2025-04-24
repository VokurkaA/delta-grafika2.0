package models.tools;

import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;
import models.drawable.shape.Line;
import rasterizers.Rasterizer;
import rasterizers.SimpleLineRasterizer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class PenTool implements Tool {
    private Point lastPoint = null;

    @Override
    public void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        lastPoint = new Point(e.getX(), e.getY());
        Graphics2D g2d = canvas.getFillLayer().createGraphics();
        g2d.setColor(drawingParams.drawingColor);
        g2d.fillRect(lastPoint.getX(), lastPoint.getY(), drawingParams.lineWidth, drawingParams.lineWidth);
        g2d.dispose();
        canvas.repaint();
    }

    @Override
    public void onMouseMove(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {

    }

    @Override
    public void onMouseDrag(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        Point currentPoint = new Point(e.getX(), e.getY());

        Graphics2D g2d = canvas.getFillLayer().createGraphics();
        g2d.setColor(drawingParams.drawingColor);

        Rasterizer rasterizer = new SimpleLineRasterizer();
        Line line = new Line(lastPoint, currentPoint, drawingParams.drawingColor, drawingParams.lineType);
        line.thickness = drawingParams.lineWidth;
        rasterizer.rasterize(g2d, line);

        g2d.dispose();
        lastPoint = currentPoint;
        canvas.repaint();
    }

    @Override
    public void onKeyPress(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {

    }

    @Override
    public void onKeyRelease(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {

    }
}
