package models;

import enums.DrawingShape;
import models.drawable.Point;
import models.drawable.shape.Shape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JFrame {
    private final JPanel panel;
    private final List<Shape> shapes = new ArrayList<>();
    private final DrawingParams drawingParams;
    private BufferedImage fillLayer;

    public Canvas(int width, int height, Color backgroundColor, DrawingParams drawingParams) {
        setTitle("DELTA grafika");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(fillLayer, 0, 0, null);

                for (Shape shape : shapes) {
                    shape.rasterize(g);
                }
            }
        };
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBackground(backgroundColor);

        add(panel);
        setVisible(true);

        this.drawingParams = drawingParams;
        fillLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        ToolBar toolBar = new ToolBar(drawingParams, this::changeShape, this::clear);
        add(toolBar, BorderLayout.WEST);
        revalidate();

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                handleResize();
            }
        });
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

        Graphics2D g = fillLayer.createGraphics();
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, fillLayer.getWidth(), fillLayer.getHeight());
        g.dispose();

        repaint();
    }

    public BufferedImage getFillLayer() {
        return fillLayer;
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    public void handleResize() {
        BufferedImage oldFillLayer = this.fillLayer;

        int newWidth = this.getWidth();
        int newHeight = this.getHeight();
        BufferedImage newFillLayer = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = newFillLayer.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(oldFillLayer, 0, 0, null);
        g.dispose();

        this.fillLayer = newFillLayer;

        repaint();
    }

    public void exportCanvas() {
        int width = panel.getWidth();
        int height = panel.getHeight();
        BufferedImage exportImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g = exportImage.createGraphics();
        g.setColor(panel.getBackground());
        g.fillRect(0, 0, width, height);

        g.drawImage(fillLayer, 0, 0, null);

        for (Shape shape : shapes) {
            shape.rasterize(g);
        }

        g.dispose();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        String filename = "delta-drawing-" + LocalDateTime.now().format(formatter) + ".png";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(filename));
        int choice = fileChooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getAbsolutePath() + ".png");
            }

            try {
                ImageIO.write(exportImage, "png", file);
                JOptionPane.showMessageDialog(this, "Image exported successfully to: " + file.getAbsolutePath(), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to export image: " + e.getMessage(), "Export Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
