package models;

public class MovingParams {
    public Point movingPoint;
    public Shape movingShape;

    public MovingParams(Point movingPoint, Shape movingShape) {
        this.movingPoint = movingPoint;
        this.movingShape = movingShape;
    }

    public void clear() {
        movingPoint = null;
        movingShape = null;
    }
}
