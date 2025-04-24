package models.tools;

import models.Canvas;
import models.DrawingParams;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface Tool {
    void onMousePress(Canvas canvas, MouseEvent e, DrawingParams drawingParams);

    void onMouseMove(Canvas canvas, MouseEvent e, DrawingParams drawingParams);

    void onMouseDrag(Canvas canvas, MouseEvent e, DrawingParams drawingParams);

    void onKeyPress(Canvas canvas, KeyEvent e, DrawingParams drawingParams);

    void onKeyRelease(Canvas canvas, KeyEvent e, DrawingParams drawingParams);
}
