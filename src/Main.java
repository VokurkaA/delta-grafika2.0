import enums.DrawingShape;
import models.*;
import models.Canvas;
import models.Point;
import models.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        Canvas canvas = new Canvas(1440, 1080, Color.black);
        DrawingParams drawingParams = new DrawingParams(false, false, DrawingShape.circle);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point newPoint = new Point(e.getX(), e.getY());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (drawingParams.movingParams.movingPoint != null) {
                        drawingParams.movingParams.clear();
                        return;
                    }

                    Shape shape = canvas.getLastShape();

                    if (shape == null || shape.isFinished()) {
                        canvas.addShape(Shape.getShapeByEnum(drawingParams.drawingShape, newPoint, drawingParams.dashedLine));
                    } else {
                        shape.leftClickAction(newPoint, drawingParams.alignLine);
                    }
                    canvas.repaint();
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    Shape shapeToMove = canvas.getShapeWithNearestPoint(newPoint);
                    if (shapeToMove != null) {
                        Point nearestPoint = shapeToMove.getNearestPoint(newPoint);

                        if (drawingParams.movingParams.movingPoint == null) {
                            drawingParams.movingParams.movingPoint = nearestPoint;
                            drawingParams.movingParams.movingShape = shapeToMove;
                            shapeToMove.rightClickAction(newPoint, drawingParams);
                        } else {
                            drawingParams.movingParams.clear();
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point newPoint = new Point(e.getX(), e.getY());
                Shape latestShape = canvas.getLastShape();

                if (drawingParams.movingParams.movingPoint != null && drawingParams.movingParams.movingShape != null) {
                    drawingParams.movingParams.movingShape.rightClickAction(newPoint, drawingParams);
                } else if (latestShape != null && !latestShape.isFinished()) {
                    if (drawingParams.alignLine && latestShape.points().size() >= 2) {
                        Point previousPoint = latestShape.points().get(latestShape.points().size() - 2);
                        newPoint = Line.alignPoint(previousPoint, newPoint);
                    }
                    latestShape.points().set(latestShape.points().size() - 1, newPoint);
                }
                canvas.repaint();
            }
        };

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                            canvas.getLastShape().setISDashed(true);
                        }
                        drawingParams.dashedLine = true;
                        break;
                    case KeyEvent.VK_SHIFT:
                        drawingParams.alignLine = true;
                        break;
                    case KeyEvent.VK_C:
                        canvas.clear();
                        break;
                    case KeyEvent.VK_P:
                        drawingParams.drawingShape = DrawingShape.polygon;
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                            canvas.removeLastShape();
                            drawingParams.movingParams.clear();
                        }
                        break;
                    case KeyEvent.VK_L:
                        drawingParams.drawingShape = DrawingShape.line;
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                            canvas.removeLastShape();
                            drawingParams.movingParams.clear();
                        }
                        break;
                    case KeyEvent.VK_O:
                        drawingParams.drawingShape = DrawingShape.circle;
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                            canvas.removeLastShape();
                            drawingParams.movingParams.clear();
                        }
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                            canvas.removeLastShape();
                        }
                        drawingParams.movingParams.clear();
                        break;
                }
                canvas.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                            canvas.getLastShape().setISDashed(false);
                        }
                        drawingParams.dashedLine = false;
                        break;
                    case KeyEvent.VK_SHIFT:
                        drawingParams.alignLine = false;
                        break;
                }
                canvas.repaint();
            }
        };

        canvas.getDrawingPanel().addMouseListener(mouseAdapter);
        canvas.getDrawingPanel().addMouseMotionListener(mouseAdapter);

        canvas.addKeyListener(keyAdapter);
    }
}
