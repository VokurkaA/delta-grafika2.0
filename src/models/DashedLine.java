package models;

import java.awt.*;

public class DashedLine extends Line {
    private final int dashedLength = 10;

    public DashedLine(Point a, Point b, Color color) {
        super(a, b, color);
    }

    @Override
    public void rasterize(Graphics g) {
        // Implement dashed line rasterization here
        // This would override the parent's rasterize method
    }
}