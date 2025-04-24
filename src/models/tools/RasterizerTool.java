package models.tools;

import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;
import models.drawable.shape.Shape;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class RasterizerTool implements Tool {
    private Shape hoveredShape = null;
    private Color originalColor = null;

    @Override
    public void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        Point clickPoint = new Point(e.getX(), e.getY());
        Shape shapeToRasterize = canvas.getNearestShape(clickPoint);

        resetHoveredShapeColor();

        if (shapeToRasterize != null) {
            Graphics2D g2d = canvas.getFillLayer().createGraphics();
            shapeToRasterize.rasterize(g2d);
            g2d.dispose();

            canvas.removeShape(shapeToRasterize);
            hoveredShape = null;
            canvas.repaint();
        }
    }

    @Override
    public void onMouseMove(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        Point hoverPoint = new Point(e.getX(), e.getY());
        Shape nearestShape = canvas.getNearestShape(hoverPoint);

        if (nearestShape != hoveredShape) {
            resetHoveredShapeColor();

            hoveredShape = nearestShape;
            if (hoveredShape != null) {
                originalColor = hoveredShape.color;
                hoveredShape.color = new Color(255 - originalColor.getRed(), 255 - originalColor.getGreen(), 255 - originalColor.getBlue(), originalColor.getAlpha());
            }
            canvas.repaint();
        }
    }

    private void resetHoveredShapeColor() {
        if (hoveredShape != null && originalColor != null) {
            hoveredShape.color = originalColor;
            originalColor = null;
        }
    }

    @Override
    public void onMouseDrag(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        onMouseMove(canvas, e, drawingParams);
    }

    @Override
    public void onKeyPress(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            resetHoveredShapeColor();
            hoveredShape = null;
            canvas.repaint();
        }
    }

    @Override
    public void onKeyRelease(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
    }
}