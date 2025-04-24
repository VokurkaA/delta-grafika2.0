package models.tools;

import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;
import models.drawable.shape.Shape;
import models.factory.ShapeFactory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ShapeTool implements Tool {

    @Override
    public void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        Point point = new Point(e.getX(), e.getY());

        if (SwingUtilities.isRightMouseButton(e)) {
            if (drawingParams.movingShape == null) {
                Shape shapeToMove = canvas.getNearestShape(point);
                if (shapeToMove != null) {
                    drawingParams.movingShape = shapeToMove;
                    shapeToMove.move(point, drawingParams, true);
                }
            } else {
                drawingParams.movingShape = null;
            }
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (drawingParams.movingShape != null) {
                drawingParams.movingShape = null;
                return;
            }

            Shape shape = canvas.getLastShape();
            if (shape == null || shape.isFinished) {
                canvas.addShape(ShapeFactory.getShapeByEnum(drawingParams.drawingShape, point, drawingParams));
            } else {
                shape.place(point, drawingParams.doAlignLine);
            }
        }

        canvas.repaint();
    }

    @Override
    public void onMouseMove(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        Point point = new Point(e.getX(), e.getY());
        Shape latestShape = canvas.getLastShape();

        if (drawingParams.movingShape != null) {
            drawingParams.movingShape.move(point, drawingParams, false);
        } else if (latestShape != null && !latestShape.isFinished) {
            latestShape.move(point, drawingParams, false);
        }

        canvas.repaint();
    }

    @Override
    public void onMouseDrag(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        onMouseMove(canvas, e, drawingParams);
    }

    @Override
    public void onKeyPress(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SHIFT:
                drawingParams.doAlignLine = true;
                break;
            case KeyEvent.VK_ESCAPE:
                if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished) canvas.removeLastShape();
                drawingParams.movingShape = null;
                break;
        }

        canvas.repaint();
    }

    @Override
    public void onKeyRelease(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            drawingParams.doAlignLine = false;
        }

        canvas.repaint();
    }
}
