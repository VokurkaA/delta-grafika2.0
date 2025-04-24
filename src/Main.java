import enums.DrawingShape;
import enums.DrawingTool;
import enums.LineType;
import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;
import models.drawable.shape.Shape;
import models.factory.ShapeFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        DrawingParams drawingParams = new DrawingParams(false, LineType.solid, DrawingShape.polygon, 1, Color.red, DrawingTool.shape);
        Canvas canvas = new Canvas(1440, 1080, Color.black, drawingParams);

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
                if (shape == null || shape.isFinished)
                    canvas.addShape(ShapeFactory.getShapeByEnum(drawingParams.drawingShape, newPoint, drawingParams));
                else shape.place(newPoint, drawingParams.doAlignLine);
                canvas.repaint();
            }

            private void handleRightMousePress(Canvas canvas, Point newPoint, DrawingParams drawingParams) {
                if (drawingParams.movingShape == null) {
                    Shape shapeToMove = canvas.getNearestShape(newPoint);
                    if (shapeToMove != null) {
                        drawingParams.movingShape = shapeToMove;
                        shapeToMove.move(newPoint, drawingParams, true);
                    }
                } else drawingParams.movingShape = null;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point newPoint = new Point(e.getX(), e.getY());
                Shape latestShape = canvas.getLastShape();

                if (drawingParams.movingShape != null) drawingParams.movingShape.move(newPoint, drawingParams, false);
                else if (latestShape != null && !latestShape.isFinished)
                    latestShape.move(newPoint, drawingParams, false);
                canvas.repaint();
            }
        };

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_SHIFT:
                        drawingParams.doAlignLine = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (canvas.getLastShape() != null && !canvas.getLastShape().isFinished)
                            canvas.removeLastShape();
                        drawingParams.movingShape = null;
                        break;
                }
                canvas.repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    drawingParams.doAlignLine = false;
                }
                canvas.repaint();
            }
        };

        JPanel drawingPanel = canvas.getDrawingPanel();
        drawingPanel.addMouseListener(mouseAdapter);
        drawingPanel.addMouseMotionListener(mouseAdapter);

        drawingPanel.setFocusable(true);

        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                drawingPanel.requestFocusInWindow();
            }
        });

        drawingPanel.addKeyListener(keyAdapter);
        SwingUtilities.invokeLater(drawingPanel::requestFocusInWindow);
    }
}