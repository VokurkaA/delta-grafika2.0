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

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public Shape getLastShape() {
        if (shapes.isEmpty())
            return null;
        return shapes.getLast();
    }

    public JPanel getDrawingPanel() {
        return panel;
    }

    public void clear() {
        shapes.clear();
        repaint();
    }
}
