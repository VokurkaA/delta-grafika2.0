package models;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JFrame {
    private final JPanel panel;
    private final List<Shape> shapes = new ArrayList<>();

    public Canvas(int width, int height, Color backgroundColor) {
        setTitle("DELTA grafika");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Shape shape : shapes) {
                    shape.rasterize(g);
                }
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(backgroundColor);

        add(panel);
        setVisible(true);
    }

    public Point getNearestPoint(Point point) {
        Point nearestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Shape shape : shapes) {
            Point candidate = shape.getNearestPoint(point);
            if (candidate != null) {
                double distance = Point.getDistance(point, candidate);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestPoint = candidate;
                }
            }
        }
        if (minDistance > 25) return null;
        return nearestPoint;
    }

    public Shape getShapeWithNearestPoint(Point point) {
        Shape shapeWithNearestPoint = null;
        double minDistance = Double.MAX_VALUE;

        for (Shape shape : shapes) {
            Point candidate = shape.getNearestPoint(point);
            if (candidate != null) {
                double distance = Point.getDistance(point, candidate);
                if (distance < minDistance && distance < 25) {
                    minDistance = distance;
                    shapeWithNearestPoint = shape;
                }
            }
        }

        return shapeWithNearestPoint;
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
