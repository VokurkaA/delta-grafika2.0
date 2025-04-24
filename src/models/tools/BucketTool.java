package models.tools;

import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Stack;

public class BucketTool implements Tool {
    @Override
    public void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        Point clickPoint = new Point(e.getX(), e.getY());
        BufferedImage fillLayer = canvas.getFillLayer();

        int targetRGB = fillLayer.getRGB(clickPoint.getX(), clickPoint.getY());
        Color targetColor = new Color(targetRGB, true);

        floodFill(fillLayer, clickPoint.getX(), clickPoint.getY(), targetColor, drawingParams.drawingColor);
        canvas.repaint();
    }

    @Override
    public void onMouseMove(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {

    }

    @Override
    public void onMouseDrag(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {

    }

    @Override
    public void onKeyPress(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {

    }

    @Override
    public void onKeyRelease(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {

    }

    private void floodFill(BufferedImage image, int x, int y, Color targetColor, Color replacementColor) {
        Stack<Point> stack = new Stack<>();
        stack.push(new Point(x, y));

        int width = image.getWidth();
        int height = image.getHeight();
        int targetRGB = targetColor.getRGB();
        int replacementRGB = replacementColor.getRGB();

        while (!stack.isEmpty()) {
            Point p = stack.pop();
            int currentX = p.getX();
            int currentY = p.getY();

            if (currentX < 0 || currentX >= width || currentY < 0 || currentY >= height) continue;

            if (image.getRGB(currentX, currentY) != targetRGB) continue;

            image.setRGB(currentX, currentY, replacementRGB);

            stack.push(new Point(currentX + 1, currentY));
            stack.push(new Point(currentX - 1, currentY));
            stack.push(new Point(currentX, currentY + 1));
            stack.push(new Point(currentX, currentY - 1));
        }
    }
}
