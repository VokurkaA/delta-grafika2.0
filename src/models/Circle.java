package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Circle implements Shape {
    private final List<Point> points = new ArrayList<>();
    private final Color color;
    private float radius;
    private boolean finished = false;

    public Circle(Point a, Point b, Color color) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.radius = getRadius();
    }

    public Circle(Point a) {
        this(a, new Point(a.getX(), a.getY()), Color.red);
    }

    @Override
    public List<Point> points() {
        return points;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void rasterize(Graphics g) {
        radius = getRadius();
        int x = (int) radius;
        int y = 0;

        int centerX = getCenter().getX();
        int centerY = getCenter().getY();

        g.setColor(color);

        g.fillRect(x + centerX, y + centerY, 1, 1);
        if (radius > 0) {
            g.fillRect(x + centerX, -y + centerY, 1, 1);
            g.fillRect(y + centerX, x + centerY, 1, 1);
            g.fillRect(-y + centerX, x + centerY, 1, 1);
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
    }

    public Point getCenter() {
        return points.getFirst();
    }

    private float getRadius() {
        float dx = points.get(1).getX() - points.get(0).getX();
        float dy = points.get(1).getY() - points.get(0).getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
