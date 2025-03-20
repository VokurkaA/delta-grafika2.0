package models;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Line implements Shape {
    private final List<Point> points = new ArrayList<>();
    private final Color color;
    private boolean isDashed;
    private boolean isFinished = false;

    public Line(Point a, Point b, Color color, boolean isDashed) {
        points.add(a);
        points.add(b);
        this.color = color;
        this.isDashed = isDashed;
    }

    public Line(Point a, boolean isDashed) {
        this(a, new Point(a.getX(), a.getY()), Color.red, isDashed);
    }

    public static Point alignPoint(Point a, Point b) {
        float d = (float) Math.hypot(b.getX() - a.getX(), b.getY() - a.getY());
        float angle = (float) Math.toDegrees(Math.atan2(b.getY() - a.getY(), b.getX() - a.getX()));

        float alignedAngle = ((Math.round(angle / 45) * 45) + 360) % 360;

        return new Point((int) (a.getX() + d * Math.cos(Math.toRadians(alignedAngle))), (int) (a.getY() + d * Math.sin(Math.toRadians(alignedAngle))));
    }

    public Point getA() {
        return points.getFirst();
    }

    public void setA(Point point) {
        points.set(0, point);
    }

    public Point getB() {
        return points.get(1);
    }

    public void setB(Point point) {
        points.set(1, point);
    }

    public Color getColor() {
        return color;
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
    public boolean idDashed() {
        return isDashed;
    }

    @Override
    public void setISDashed(boolean isDashed) {
        this.isDashed = isDashed;
    }

    @Override
    public void leftClickAction(Point point, boolean alignLine) {
        if (alignLine) this.setB(Line.alignPoint(getA(), point));
        else this.setB(point);
        setIsFinished(true);
    }

    @Override
    public void rasterize(Graphics g) {
        if (isDashed) {
            new DashedLine(getA(), getB(), getColor()).rasterize(g);
            return;
        }
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

//    @Override
//    public void rasterize(Graphics g) {
//        if (this.getA().equals(this.getB())) return;
//
//        g.setColor(color);
//        int dx = getB().getX() - getA().getX();
//        int dy = getB().getY() - getA().getY();
//
//        int step = Math.max(Math.abs(dx), Math.abs(dy));
//
//        float xIncr = (float) dx / step;
//        float yIncr = (float) dy / step;
//
//        float x = getA().getX();
//        float y = getA().getY();
//
//        for (int i = 0; i < step; i++) {
//            g.fillRect(Math.round(x), Math.round(y), 1, 1);
//            x += xIncr;
//            y += yIncr;
//        }
//    }
}
