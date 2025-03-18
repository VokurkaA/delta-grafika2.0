import enums.DrawingShape;
import models.Canvas;
import models.DrawingParams;
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
        DrawingParams drawingParams = new DrawingParams(false, false, DrawingShape.circle);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Shape shape = canvas.getLastShape();
                if (shape == null || shape.isFinished()) {
                    shape = Shape.getShapeByEnum(drawingParams.drawingShape, new Point(e.getX(), e.getY()));
                    canvas.addShape(shape);
                    canvas.repaint();
                } else {
                    Point lastPoint = shape.points().getLast();
                    lastPoint.set(new Point(e.getX(), e.getY()));
                    shape.setFinished(true);
                    canvas.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Shape latestShape = canvas.getLastShape();
                if (latestShape != null && !latestShape.isFinished()) {
                    Point lastPoint = latestShape.points().getLast();
                    lastPoint.set(new Point(e.getX(), e.getY()));
                    canvas.repaint();
                }
            }
        };

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        System.out.print("ctrl\n");
                        break;
                    case KeyEvent.VK_SHIFT:
                        System.out.print("shift\n");
                        break;
                    case KeyEvent.VK_C:
                        canvas.clear();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_CONTROL:
                        break;
                    case KeyEvent.VK_SHIFT:
                        break;
                }
            }
        };

        canvas.getDrawingPanel().addMouseListener(mouseAdapter);
        canvas.getDrawingPanel().addMouseMotionListener(mouseAdapter);

        canvas.addKeyListener(keyAdapter);
    }
}
