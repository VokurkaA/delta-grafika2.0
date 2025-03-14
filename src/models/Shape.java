package models;

import java.awt.*;
import java.util.List;

public interface Shape {
    List<Point> points();

    void rasterize(Graphics g);
}
