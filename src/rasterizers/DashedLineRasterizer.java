package rasterizers;

import models.drawable.shape.Line;
import models.drawable.shape.Shape;
import utils.SettingsManager;

import java.awt.*;

public class DashedLineRasterizer implements Rasterizer {
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

        boolean isDrawingSolid = true;
        int dashLength = SettingsManager.getInt("tools.line.dashedLength", 5);
        int dashedLength = Math.max(5, shape.thickness * dashLength);

        for (int i = 0; i < step; i++) {
            if (i % dashedLength == 0) {
                g.setColor(isDrawingSolid ? color : Color.black);
                isDrawingSolid = !isDrawingSolid;
            }
            g.fillRect(Math.round(x), Math.round(y), shape.thickness, shape.thickness);
            x += xIncr;
            y += yIncr;
        }
    }
}
