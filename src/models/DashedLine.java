package models;

import java.awt.*;

public class DashedLine extends Line {
    private final int dashedLength = 10;

    public DashedLine(Point a, Point b, Color color) {
        super(a, b, color, true);
    }

    public DashedLine(Point a) {
        super(a, true);
    }

    @Override
    public void rasterize(Graphics g) {
        if (this.getA().equals(this.getB())) return;

        g.setColor(super.getColor());
        int dx = getB().getX() - getA().getX();
        int dy = getB().getY() - getA().getY();

        int step = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncr = (float) dx / step;
        float yIncr = (float) dy / step;

        float x = getA().getX();
        float y = getA().getY();

        boolean isDrawingSolid = true;

        for (int i = 0; i < step; i++) {
            if (i % dashedLength == 0) {
                g.setColor(isDrawingSolid ? super.getColor() : Color.black);
                isDrawingSolid = !isDrawingSolid;
            }
            g.fillRect(Math.round(x), Math.round(y), 1, 1);
            x += xIncr;
            y += yIncr;
        }
    }
}