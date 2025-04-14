package models;

import enums.*;
import models.factory.MenuFactory;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ToolBar extends JToolBar {

    public ToolBar(DrawingParams drawingParams, Consumer<DrawingShape> changeShape, Runnable clearCanvas) {
        super(JToolBar.VERTICAL);

        add(MenuFactory.createMenu("Canvas", CanvasMenuOptions.values(), MenuType.generic, e -> {
            switch (CanvasMenuOptions.valueOf(e.getActionCommand())) {
                case clear -> clearCanvas.run();
                case export -> System.out.println("Export triggered!");
            }
        }));

        add(MenuFactory.createMenu("Line", LineType.values(), MenuType.generic, e -> {
            drawingParams.lineType = LineType.valueOf(e.getActionCommand());
        }));

        add(MenuFactory.createMenu("Tools", DrawingTool.values(), MenuType.generic, e -> {
            System.out.println("Tool selected: " + e.getActionCommand());
        }));

        add(MenuFactory.createMenu("Shapes", DrawingShape.values(), MenuType.generic, e -> {
            changeShape.accept(DrawingShape.valueOf(e.getActionCommand()));
        }));

        add(MenuFactory.createMenu("Thickness", MenuType.slider, e -> {
            drawingParams.lineWidth = Math.max(1, Integer.parseInt(e.getActionCommand()));
            System.out.println("Thickness selected: " + drawingParams.lineWidth);
        }));


        add(MenuFactory.createMenu("Color", MenuType.color, e -> {
            Color selectedColor = new Color(Integer.parseInt(e.getActionCommand()));
            drawingParams.drawingColor = selectedColor;
            System.out.println("Color chosen: " + selectedColor);
        }));
    }

}
