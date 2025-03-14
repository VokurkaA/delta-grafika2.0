package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Line implements Shape {
    private final List<Point> points = new ArrayList<>();
    private final Color color;

    public Line(Point a, Point b, Color color) {
        points.add(a);
        points.add(b);
        this.color = color;
    }

    public Line(Point a, Point b) {
        this(a, b, Color.RED);
    }

    public Line(Point a) {
        this(a, new Point(a.getX(), a.getY()), Color.RED);
    }

    public Point getA() {
        return points.get(0);
    }

    public Point getB() {
        return points.get(1);
    }

    public Color getColor() {
        return color;
    }

    @Override
    public List<Point> points() {
        return points;
    }

    @Override
    public void rasterize(Graphics g) {
        if (this.getA().equals(this.getB())) return;

        g.setColor(color);
        int dx = getB().getX() - getA().getX();
        int dy = getB().getY() - getA().getY();

        int step = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncr = (float) dx / step;
        float yIncr = (float) dy / step;

        float x = getA().getX();
        float y = getA().getY();

        for (int i = 0; i < step; i++) {
            g.fillRect(Math.round(x), Math.round(y), 1, 1);
            x += xIncr;
            y += yIncr;
        }
    }
}
