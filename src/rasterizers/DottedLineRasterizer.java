package rasterizers;

import models.drawable.shape.Line;
import models.drawable.shape.Shape;
import utils.SettingsManager;

import java.awt.*;

public class DottedLineRasterizer implements Rasterizer {
    public void rasterize(Graphics g, Shape shape) {
        rasterize(g, shape, shape.color);
    }

    public void rasterize(Graphics g, Shape shape, Color color) {
        if (!(shape instanceof Line line)) return;
        if (line.getA().equals(line.getB())) return;

        g.setColor(color);

        int dx = line.getB().getX() - line.getA().getX();
        int dy = line.getB().getY() - line.getA().getY();

        int step = Math.max(Math.abs(dx), Math.abs(dy));
        float xIncr = (float) dx / step;
        float yIncr = (float) dy / step;

        float x = line.getA().getX();
        float y = line.getA().getY();

        int dottedSpacing = SettingsManager.getInt("tools.line.dottedSpacing", 2);
        int dotSpacing = Math.max(2, shape.thickness * dottedSpacing);

        for (int i = 0; i < step; i++) {
            if (i % dotSpacing == 0) {
                g.fillRect(Math.round(x), Math.round(y), shape.thickness, shape.thickness);
            }
            x += xIncr;
            y += yIncr;
        }
    }
}
