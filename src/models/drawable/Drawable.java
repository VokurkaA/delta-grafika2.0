package models.drawable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Drawable {
    public List<Point> points = new ArrayList<>();
    protected Color color;
}
