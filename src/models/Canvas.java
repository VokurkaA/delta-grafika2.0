package models;

import models.drawable.shape.Shape;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JFrame {
    private final JPanel panel;
    private final ToolBar toolBar;
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

        this.toolBar = new ToolBar();
        add(toolBar, BorderLayout.WEST);
        revalidate();
    }

    public Shape getNearestShape(models.drawable.Point click) {
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
