package rasterizers;

import models.drawable.shape.Line;
import models.drawable.shape.Shape;

import java.awt.*;

public class SimpleLineRasterizer implements Rasterizer {
    public void rasterize(Graphics g, Shape shape) {
        rasterize(g, shape, shape.color);
    }

    public void rasterize(Graphics g, Shape shape, Color color) {
        if (!(shape instanceof Line line)) return;

        if (line.getA() == line.getB()) return;

        g.setColor(color);

        int dx = line.getB().getX() - line.getA().getX();
        int dy = line.getB().getY() - line.getA().getY();

        int step = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncr = (float) dx / step;
        float yIncr = (float) dy / step;

        float x = line.getA().getX();
        float y = line.getA().getY();

        for (int i = 0; i < step; i++) {
            g.fillRect(Math.round(x), Math.round(y), shape.thickness, shape.thickness);
            x += xIncr;
            y += yIncr;
        }
    }
}
