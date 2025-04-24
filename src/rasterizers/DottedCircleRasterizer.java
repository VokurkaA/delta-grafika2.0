package rasterizers;

import models.drawable.Point;
import models.drawable.shape.Circle;
import models.drawable.shape.Shape;

import java.awt.*;

public class DottedCircleRasterizer implements Rasterizer {
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
        g.fillRect(centerX, centerY, 1, 1);

        int P = 1 - (int) radius;
        int dotSpacing = 5;
        int stepCounter = 0;

        while (x > y) {
            y++;
            if (P <= 0) {
                P += 2 * y + 1;
            } else {
                x--;
                P += 2 * y - 2 * x + 1;
            }

            if (x < y) break;

            if (stepCounter % dotSpacing == 0) {
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

            stepCounter++;
        }
    }
}
