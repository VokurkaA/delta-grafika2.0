package rasterizers;

import models.drawable.Point;
import models.drawable.shape.Circle;
import models.drawable.shape.Shape;

import java.awt.*;

public class DottedCircleRasterizer {
    public static void rasterize(Graphics g, Shape shape, Color color) {
        if (!(shape instanceof Circle circle)) return;

        double radius = Point.getDistance(circle.points.get(1), circle.points.getFirst());
        int x = (int) radius;
        int y = 0;

        int centerX = circle.getCenter().getX();
        int centerY = circle.getCenter().getY();

        g.setColor(color);
        g.fillRect(centerX, centerY, 1, 1); // Optional: center dot

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
                // Plot all 8 symmetrical points
                g.fillRect(x + centerX, y + centerY, 1, 1);
                g.fillRect(-x + centerX, y + centerY, 1, 1);
                g.fillRect(x + centerX, -y + centerY, 1, 1);
                g.fillRect(-x + centerX, -y + centerY, 1, 1);

                if (x != y) {
                    g.fillRect(y + centerX, x + centerY, 1, 1);
                    g.fillRect(-y + centerX, x + centerY, 1, 1);
                    g.fillRect(y + centerX, -x + centerY, 1, 1);
                    g.fillRect(-y + centerX, -x + centerY, 1, 1);
                }
            }

            stepCounter++;
        }
    }
}
