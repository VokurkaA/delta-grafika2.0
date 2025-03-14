import models.Canvas;
import models.Line;
import models.Point;
import models.Shape;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Main {
    public static void main(String[] args) {
        Canvas canvas = new Canvas(1200, 900, Color.black);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Line temp = new Line(new Point(e.getX(), e.getY()));
                canvas.addShape(temp);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Shape temp = canvas.getLastShape();
                temp.points().getLast().set(new Point(e.getX(), e.getY()));
                canvas.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }
        };

        canvas.getDrawingPanel().addMouseListener(mouseAdapter);
        canvas.getDrawingPanel().addMouseMotionListener(mouseAdapter);
    }
}
