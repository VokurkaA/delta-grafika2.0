package models.tools;

import enums.LineType;
import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;
import models.drawable.shape.Line;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class EraserTool implements Tool {
    private Point lastPoint = null;

    @Override
    public void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        lastPoint = new Point(e.getX(), e.getY());
        Graphics2D g2d = canvas.getFillLayer().createGraphics();
        g2d.setComposite(AlphaComposite.Clear);

        int eraserSize = drawingParams.lineWidth * 2;
        int x = lastPoint.getX() - eraserSize / 2;
        int y = lastPoint.getY() - eraserSize / 2;
        g2d.fillOval(x, y, eraserSize, eraserSize);

        g2d.dispose();
        canvas.repaint();
    }

    @Override
    public void onMouseDrag(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        Point currentPoint = new Point(e.getX(), e.getY());

        if (lastPoint != null) {
            Line line = new Line(lastPoint, currentPoint, new Color(0, 0, 0, 0), LineType.solid, drawingParams.lineWidth);
            Graphics2D g2d = canvas.getFillLayer().createGraphics();
            g2d.setComposite(AlphaComposite.Clear);
            g2d.setStroke(new BasicStroke(drawingParams.lineWidth * 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            g2d.drawLine(line.getA().getX(), line.getA().getY(), line.getB().getX(), line.getB().getY());

            g2d.dispose();
        }

        lastPoint = currentPoint;
        canvas.repaint();
    }

    @Override
    public void onMouseMove(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
    }

    @Override
    public void onKeyPress(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
    }

    @Override
    public void onKeyRelease(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
    }
}