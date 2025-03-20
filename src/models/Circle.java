package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Circle implements Shape {
    private final List<Point> points = new ArrayList<>();
    private final Color color;
    private float radius;
    private boolean isDashed;
    private boolean isFinished = false;


    public Circle(Point a, Point b, Color color, boolean isDashed) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.radius = getRadius();
        this.isDashed = isDashed;
    }

    public Circle(Point a, boolean isDashed) {
        this(a, new Point(a.getX(), a.getY()), Color.red, isDashed);
    }

    @Override
    public List<Point> points() {
        return points;
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void setIsFinished(boolean finished) {
        this.isFinished = finished;
    }

    @Override
    public void leftClickAction(Point point, boolean alignLine) {
        this.points.getLast().set(point);
        setIsFinished(true);
    }

    @Override
    public boolean idDashed() {
        return isDashed;
    }

    @Override
    public void setISDashed(boolean isDashed) {
        this.isDashed = isDashed;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void rasterize(Graphics g) {
        if (isDashed) {
            new DashedCircle(getCenter(), points.get(1), getColor()).rasterize(g);
            return;
        }

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

    protected float getRadius() {
        float dx = points.get(1).getX() - points.get(0).getX();
        float dy = points.get(1).getY() - points.get(0).getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
