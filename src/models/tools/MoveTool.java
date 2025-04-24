package models.tools;

import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;
import models.drawable.shape.Shape;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MoveTool implements Tool {
    Point moveOffset = null;

    @Override
    public void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        Point clickedPoint = new Point(e.getX(), e.getY());

        if (SwingUtilities.isLeftMouseButton(e)) {
            Shape shapeToMove = canvas.getNearestShape(clickedPoint);

            if (shapeToMove != null) {
                if (shapeToMove.equals(drawingParams.movingShape)) {
                    drawingParams.movingShape = null;
                    moveOffset = null;
                } else {
                    drawingParams.movingShape = shapeToMove;

                    int xSum = 0, ySum = 0;
                    for (Point p : shapeToMove.points) {
                        xSum += p.getX();
                        ySum += p.getY();
                    }
                    Point center = new Point(xSum / shapeToMove.points.size(), ySum / shapeToMove.points.size());
                    moveOffset = new Point(clickedPoint.getX() - center.getX(), clickedPoint.getY() - center.getY());
                }
            } else {
                drawingParams.movingShape = null;
                moveOffset = null;
            }
        }
        canvas.repaint();
    }


    @Override
    public void onMouseMove(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        if (drawingParams.movingShape != null && moveOffset != null) {
            Point target = new Point(e.getX() - moveOffset.getX(), e.getY() - moveOffset.getY());

            drawingParams.movingShape.moveEntireShape(target);
            canvas.repaint();
        }
    }


    @Override
    public void onMouseDrag(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
    }

    @Override
    public void onKeyPress(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            drawingParams.movingShape = null;
            canvas.repaint();
        }
    }

    @Override
    public void onKeyRelease(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
    }
}
