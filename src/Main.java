import models.Canvas;
import models.DrawingParams;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        DrawingParams drawingParams = new DrawingParams();
        Canvas canvas = new Canvas(drawingParams);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                drawingParams.drawingTool.onMousePress(canvas, e, drawingParams);
                canvas.getDrawingPanel().requestFocusInWindow();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                drawingParams.drawingTool.onMouseDrag(canvas, e, drawingParams);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                drawingParams.drawingTool.onMouseMove(canvas, e, drawingParams);
            }
        };

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                drawingParams.drawingTool.onKeyPress(canvas, e, drawingParams);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                drawingParams.drawingTool.onKeyRelease(canvas, e, drawingParams);
            }
        };

        canvas.getDrawingPanel().addMouseListener(mouseAdapter);
        canvas.getDrawingPanel().addMouseMotionListener(mouseAdapter);
        canvas.getDrawingPanel().addKeyListener(keyAdapter);
        canvas.getDrawingPanel().setFocusable(true);
    }
}
