package models.tools;

import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class BucketTool implements Tool {
    @Override
    public void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        int x = e.getX();
        int y = e.getY();
        BufferedImage fillLayer = canvas.getFillLayer();

        if (x < 0 || x >= fillLayer.getWidth() || y < 0 || y >= fillLayer.getHeight()) {
            return;
        }

        int targetRGB = fillLayer.getRGB(x, y);
        Color targetColor = new Color(targetRGB, true);

        if (targetColor.equals(drawingParams.drawingColor)) {
            return;
        }

        floodFill(fillLayer, x, y, targetColor, drawingParams.drawingColor);
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
        int width = image.getWidth();
        int height = image.getHeight();
        int targetRGB = targetColor.getRGB();
        int replacementRGB = replacementColor.getRGB();

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            int currentX = p.getX();
            int currentY = p.getY();

            if (currentX < 0 || currentX >= width || currentY < 0 || currentY >= height) {
                continue;
            }

            if (image.getRGB(currentX, currentY) != targetRGB) {
                continue;
            }

            image.setRGB(currentX, currentY, replacementRGB);

            if (currentX + 1 < width) queue.add(new Point(currentX + 1, currentY));
            if (currentX - 1 >= 0) queue.add(new Point(currentX - 1, currentY));
            if (currentY + 1 < height) queue.add(new Point(currentX, currentY + 1));
            if (currentY - 1 >= 0) queue.add(new Point(currentX, currentY - 1));
        }
    }
}