package rasterizers;

import models.drawable.Point;
import models.drawable.shape.Circle;
import models.drawable.shape.Shape;

import java.awt.*;

public class SimpleCircleRasterizer implements Rasterizer {
    public void rasterize(Graphics g, Shape shape) {
        rasterize(g, shape, shape.color);
    }

    public void rasterize(Graphics g, Shape shape, Color color) {
        if (!(shape instanceof Circle circle)) return;


        double radius = Point.getDistance(circle.points.get(1), circle.points.getFirst());
        int x = (int) radius;
        int y = 0;

        int centerX = circle.getCenter().getX();
        int centerY = circle.getCenter().getY();

        g.setColor(color);
        g.fillRect(centerX, centerY, shape.thickness, shape.thickness);

        g.fillRect(x + centerX, y + centerY, shape.thickness, shape.thickness);
        if (radius > 0) {
            g.fillRect(x + centerX, y + centerY, shape.thickness, shape.thickness);
            g.fillRect(-x + centerX, y + centerY, shape.thickness, shape.thickness);
            g.fillRect(x + centerX, -y + centerY, shape.thickness, shape.thickness);
            g.fillRect(-x + centerX, -y + centerY, shape.thickness, shape.thickness);
            g.fillRect(y + centerX, x + centerY, shape.thickness, shape.thickness);
            g.fillRect(-y + centerX, x + centerY, shape.thickness, shape.thickness);
            g.fillRect(y + centerX, -x + centerY, shape.thickness, shape.thickness);
            g.fillRect(-y + centerX, -x + centerY, shape.thickness, shape.thickness);
        }


        int P = 1 - (int) radius;
        while (x > y) {
            y++;
            if (P <= 0) P = P + 2 * y + 1;
            else {
                x--;
                P = P + 2 * y - 2 * x + 1;
            }
            if (x < y) break;

            g.fillRect(x + centerX, y + centerY, shape.thickness, shape.thickness);
            g.fillRect(-x + centerX, y + centerY, shape.thickness, shape.thickness);
            g.fillRect(x + centerX, -y + centerY, shape.thickness, shape.thickness);
            g.fillRect(-x + centerX, -y + centerY, shape.thickness, shape.thickness);

            if (x != y) {
                g.fillRect(y + centerX, x + centerY, shape.thickness, shape.thickness);
                g.fillRect(-y + centerX, x + centerY, shape.thickness, shape.thickness);
                g.fillRect(y + centerX, -x + centerY, shape.thickness, shape.thickness);
                g.fillRect(-y + centerX, -x + centerY, shape.thickness, shape.thickness);
            }
        }
    }
}
