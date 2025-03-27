package models;

import java.awt.*;
import java.util.List;

public interface Drawable {
    Color color = Color.BLACK;

    List<Point> points();
}
