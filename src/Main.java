import enums.DrawingShape;
import models.*;
import models.Canvas;
import models.Point;
import models.Shape;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        Canvas canvas = new Canvas(1200, 900, Color.black);
        DrawingParams drawingParams = new DrawingParams(false, false, DrawingShape.line);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point newPoint = new Point(e.getX(), e.getY());
                Shape shape = canvas.getLastShape();

                if (shape == null || shape.isFinished()) {
                    shape = Shape.getShapeByEnum(drawingParams.drawingShape, newPoint, drawingParams.dashedLine);
                    canvas.addShape(shape);
                } else {
                    shape.leftClickAction(newPoint, drawingParams.alignLine);
                }
                canvas.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Shape latestShape = canvas.getLastShape();
                if (latestShape != null && !latestShape.isFinished()) {
                    Point movingPoint = latestShape.points().getLast();

                    if (drawingParams.alignLine) {
                        Point lastFixedPoint = latestShape.points().get(latestShape.points().size() - 2);
                        movingPoint.set(Line.alignPoint(lastFixedPoint, new Point(e.getX(), e.getY())));
                    } else movingPoint.set(new Point(e.getX(), e.getY()));
                    canvas.repaint();
                }
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
                        }
                        break;
                    case KeyEvent.VK_L:
                        drawingParams.drawingShape = DrawingShape.line;
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                            canvas.removeLastShape();
                        }
                        break;
                    case KeyEvent.VK_O:
                        drawingParams.drawingShape = DrawingShape.circle;
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished()) {
                            canvas.removeLastShape();
                        }
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
