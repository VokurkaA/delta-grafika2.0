package models;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static double getDistance(Point a, Point b) {
//        int dx = b.getX() - a.getX();
//        int dy = b.getY() - a.getY();
//        return Math.sqrt(dx * dx + dy * dy);
        return Math.sqrt(getDistanceSquared(a, b));
    }

    public static double getDistanceSquared(Point a, Point b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return dx * dx + dy * dy;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point get() {
        return this;
    }

    public void set(Point point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }
}
