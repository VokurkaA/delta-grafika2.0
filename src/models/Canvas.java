package models;

import enums.DrawingShape;
import models.drawable.Point;
import models.drawable.shape.Shape;
import models.tools.TextTool;
import utils.CanvasSerializer;
import utils.SettingsManager;

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

    public Canvas(DrawingParams drawingParams) {

        int width = SettingsManager.getInt("canvas.width", 1440);
        int height = SettingsManager.getInt("canvas.height", 1080);
        Color backgroundColor = SettingsManager.getColor("canvas.backgroundColor", Color.BLACK);

        setTitle(SettingsManager.getString("app.title", "DELTA grafika"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);

        setResizable(SettingsManager.getBoolean("app.isWindowResizable", true));

        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.drawImage(fillLayer, 0, 0, null);

                for (Shape shape : shapes) {
                    shape.rasterize(g);
                }

                if (drawingParams.drawingTool instanceof TextTool) {
                    ((TextTool) drawingParams.drawingTool).paintCurrentText(g, drawingParams);
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
        int threshold = SettingsManager.getInt("drawing.thresholdDistance", 25);

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

    public List<Shape> getShapes() {
        return shapes;
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

    public void setFillLayer(BufferedImage newFillLayer) {
        this.fillLayer = newFillLayer;
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

    public void save() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Delta Drawing Files (*.delta)", "delta"));
        fileChooser.setSelectedFile(new File("drawing.delta"));
        int choice = fileChooser.showSaveDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            if (!file.getName().toLowerCase().endsWith(".delta")) {
                file = new File(file.getAbsolutePath() + ".delta");
            }

            try {
                CanvasSerializer.saveCanvas(this, file);
                JOptionPane.showMessageDialog(this, "Drawing saved successfully to: " + file.getAbsolutePath(), "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save drawing: " + e.getMessage(), "Save Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void open() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Delta Drawing Files (*.delta)", "delta"));
        int choice = fileChooser.showOpenDialog(this);

        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                CanvasSerializer.loadCanvas(this, file);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to open drawing: " + e.getMessage(), "Open Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}