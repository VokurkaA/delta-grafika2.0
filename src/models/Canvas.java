package models;

import enums.DrawingShape;
import models.drawable.Point;
import models.drawable.shape.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JFrame {
    private final JPanel panel;
    private final List<Shape> shapes = new ArrayList<>();
    private final DrawingParams drawingParams;

    public Canvas(int width, int height, Color backgroundColor, DrawingParams drawingParams) {
        setTitle("DELTA grafika");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Shape shape : shapes) {
                    if (shape == null) break;
                    shape.rasterize(g);
                }
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(backgroundColor);

        add(panel);
        setVisible(true);

        this.drawingParams = drawingParams;

        ToolBar toolBar = new ToolBar(drawingParams, this::changeShape, this::clear);
        add(toolBar, BorderLayout.WEST);
        revalidate();
    }

    public Shape getNearestShape(Point click) {
        Shape nearestShape = null;
        double minDistance = Double.MAX_VALUE;
        int threshold = 25;

        for (Shape shape : shapes) {
            double distance = shape.getNearestDistance(click);
            if (distance < minDistance && distance < threshold) {
                minDistance = distance;
                nearestShape = shape;
            }
        }

        return nearestShape;
    }

    public void changeShape(DrawingShape newShape) {
        if (getLastShape() != null && getLastShape().toString().toLowerCase().equals(newShape.name())) return;
        if (getLastShape() != null && !getLastShape().isFinished) {
            removeLastShape();
            drawingParams.movingShape = null;
        }
        drawingParams.drawingShape = newShape;
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public Shape getLastShape() {
        if (shapes.isEmpty()) return null;
        return shapes.getLast();
    }

    public void removeLastShape() {
        if (!shapes.isEmpty()) this.shapes.removeLast();
    }

    public JPanel getDrawingPanel() {
        return panel;
    }

    public void clear() {
        shapes.clear();
        repaint();
    }
}
