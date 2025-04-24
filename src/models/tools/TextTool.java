package models.tools;

import models.Canvas;
import models.DrawingParams;
import models.drawable.Point;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class TextTool implements Tool {
    private final int CURSOR_BLINK_RATE = 500; // milliseconds
    private Point textPosition;
    private StringBuilder currentText = new StringBuilder();
    private boolean isEditing = false;
    private long lastBlinkTime = 0;
    private boolean showCursor = true;

    @Override
    public void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        textPosition = new Point(e.getX(), e.getY());
        isEditing = true;
        currentText = new StringBuilder();
        canvas.repaint();
    }

    @Override
    public void onMouseMove(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
        if (isEditing) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastBlinkTime > CURSOR_BLINK_RATE) {
                showCursor = !showCursor;
                lastBlinkTime = currentTime;
                canvas.repaint();
            }
        }
    }

    @Override
    public void onMouseDrag(Canvas canvas, MouseEvent e, DrawingParams drawingParams) {
    }

    @Override
    public void onKeyPress(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
        if (!isEditing) return;

        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_ENTER) {
            finishTextEditing(canvas, drawingParams);
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            isEditing = false;
            currentText = new StringBuilder();
            canvas.repaint();
        } else if (keyCode == KeyEvent.VK_BACK_SPACE) {
            if (!currentText.isEmpty()) {
                currentText.deleteCharAt(currentText.length() - 1);
                canvas.repaint();
            }
        } else {
            char c = e.getKeyChar();
            if (Character.isDefined(c) && !e.isControlDown()) {
                currentText.append(c);
                canvas.repaint();
            }
        }
    }

    @Override
    public void onKeyRelease(Canvas canvas, KeyEvent e, DrawingParams drawingParams) {
    }

    private void finishTextEditing(Canvas canvas, DrawingParams drawingParams) {
        if (textPosition == null || currentText.isEmpty()) {
            isEditing = false;
            return;
        }

        BufferedImage fillLayer = canvas.getFillLayer();
        Graphics2D g2d = fillLayer.createGraphics();
        g2d.setColor(drawingParams.drawingColor);

        Font font = new Font("SansSerif", Font.PLAIN, Math.max(12, drawingParams.lineWidth * 12));
        g2d.setFont(font);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.drawString(currentText.toString(), textPosition.getX(), textPosition.getY());
        g2d.dispose();

        isEditing = false;
        currentText = new StringBuilder();
        canvas.repaint();
    }

    public void paintCurrentText(Graphics g, DrawingParams drawingParams) {
        if (!isEditing || textPosition == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(drawingParams.drawingColor);

        Font font = new Font("SansSerif", Font.PLAIN, Math.max(12, drawingParams.lineWidth * 12));
        g2d.setFont(font);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        String text = currentText.toString();
        g2d.drawString(text, textPosition.getX(), textPosition.getY());

        if (showCursor) {
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2d.drawLine(textPosition.getX() + textWidth, textPosition.getY() - fm.getAscent(), textPosition.getX() + textWidth, textPosition.getY() + fm.getDescent());
        }
    }
}