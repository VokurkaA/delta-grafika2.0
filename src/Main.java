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
        DrawingParams drawingParams = new DrawingParams(false, false, DrawingShape.polygon);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point newPoint = new Point(e.getX(), e.getY());

                if (SwingUtilities.isLeftMouseButton(e)) handleLeftMousePress(canvas, newPoint, drawingParams);
                else if (SwingUtilities.isRightMouseButton(e)) handleRightMousePress(canvas, newPoint, drawingParams);
            }

            private void handleLeftMousePress(Canvas canvas, Point newPoint, DrawingParams drawingParams) {
                if (drawingParams.movingShape != null) {
                    drawingParams.movingShape = null;
                    return;
                }

                Shape shape = canvas.getLastShape();
                if (shape == null || shape.isFinished()) {
                    canvas.addShape(Shape.getShapeByEnum(drawingParams.drawingShape, newPoint, drawingParams.dashedLine));
                } else {
                    shape.place(newPoint, drawingParams.doAlignLine);
                }
                canvas.repaint();
            }

            private void handleRightMousePress(Canvas canvas, Point newPoint, DrawingParams drawingParams) {
                if (drawingParams.movingShape == null) {
                    Shape shapeToMove = canvas.getNearestShape(newPoint);
                    if (shapeToMove != null) {
                        drawingParams.movingShape = shapeToMove;
                        shapeToMove.move(newPoint, drawingParams, true);
                    }
                } else {
                    drawingParams.movingShape = null;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point newPoint = new Point(e.getX(), e.getY());
                Shape latestShape = canvas.getLastShape();

                if (drawingParams.movingShape != null) {
                    drawingParams.movingShape.move(newPoint, drawingParams, false);
                } else if (latestShape != null && !latestShape.isFinished()) {
                    if (drawingParams.doAlignLine && latestShape.points().size() >= 2) {
                        Point previousPoint = latestShape.points().get(latestShape.points().size() - 2);
                        newPoint = Line.alignPoint(previousPoint, newPoint);
                    }
                    latestShape.points().set(latestShape.points().size() - 1, newPoint);
                }
                canvas.repaint();
            }
        };

        KeyAdapter keyAdapter = new KeyAdapter() {

            void changeDrawingShape(Canvas canvas, DrawingShape newShape) {
                if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                    canvas.removeLastShape();
                    drawingParams.movingShape = null;
                }
                drawingParams.drawingShape = newShape;
            }

            void toggleDashedLine(Canvas canvas, boolean isDashed) {
                if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                    canvas.getLastShape().setISDashed(isDashed);
                }
                drawingParams.dashedLine = isDashed;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_C:
                        canvas.clear();
                        break;
                    case KeyEvent.VK_P:
                        changeDrawingShape(canvas, DrawingShape.polygon);
                        break;
                    case KeyEvent.VK_L:
                        changeDrawingShape(canvas, DrawingShape.line);
                        break;
                    case KeyEvent.VK_O:
                        changeDrawingShape(canvas, DrawingShape.circle);
                        break;
                    case KeyEvent.VK_SHIFT:
                        drawingParams.doAlignLine = true;
                        break;
                    case KeyEvent.VK_CONTROL:
                        toggleDashedLine(canvas, true);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished())
                            canvas.removeLastShape();
                        drawingParams.movingShape = null;
                        break;
                }
                canvas.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        toggleDashedLine(canvas, false);
                        break;
                    case KeyEvent.VK_SHIFT:
                        drawingParams.doAlignLine = false;
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
