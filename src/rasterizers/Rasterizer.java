package rasterizers;

import models.drawable.shape.Shape;

import java.awt.*;

public interface Rasterizer {
    void rasterize(Graphics g, Shape shape);
}