package models;

import java.awt.*;

public class DashedCircle extends Circle {
    public DashedCircle(Point a, Point b, Color color) {
        super(a, b, color, true);
    }

    public DashedCircle(Point a) {
        super(a, true);
    }

    @Override
    public void rasterize(Graphics g) {
        float radius = getRadius();
        int x = (int) radius;
        int y = 0;

        int centerX = getCenter().getX();
        int centerY = getCenter().getY();

        g.setColor(getColor());

        int P = 1 - (int) radius;
        int dashLength = 10;
        int totalCounter = 0;

        while (x > y) {
            y++;
            if (P <= 0) {
                P = P + 2 * y + 1;
            } else {
                x--;
                P = P + 2 * y - 2 * x + 1;
            }

            if (x < y) break;

            if (totalCounter < dashLength) {
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

            totalCounter++;

            if (totalCounter >= (2 * dashLength)) {
                totalCounter = 0;
            }
        }
    }

}
